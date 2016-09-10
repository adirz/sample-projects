package com.epam.runner;

import com.epam.modifiers.Grades;
import com.epam.bot.UserBot;
import com.epam.bot.HeuristicBot;
import com.epam.sdk.IBot;
import com.epam.sdk.Triple;
import com.epam.sdk.Tuple;

import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;

import java.io.IOException;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.Files;

import java.lang.ProcessBuilder;

import com.google.gson.Gson;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * master of all- runs everything important in the code: we call it to imrprove the grading genetically or stochastic hill climbingly, and to test our outcomes.
 */
public class OptimizationModifier {
    // the size of the population we check
    private static int kidsInGeneration;
    // how many generation we check we check
    private static int generationNum;
    // the name of the folders and description of each bot in the test
    private static final String[] TYPES =  new String[]{"lowPopGenUserbot","highPopGenUserbot","lowPopSHCUserbot","highPopSHCUserbot",
    													"lowPopGenHeubot","highPopGenHeubot","lowPopSHCHeubot","highPopSHCHeubot"};
    // the types corresponding to the matching 
    private static final Class[] classes = new Class[]{UserBot.class, UserBot.class, UserBot.class, UserBot.class, HeuristicBot.class,
    													HeuristicBot.class, HeuristicBot.class, HeuristicBot.class};
    private static final int BOTS_PER_TYPE = 2;
    private static final int BOTS_TYPES = 8; //excluding our own made bots
    private static final double BASE_MUTATION_FACTOR = 0.99;
    private static final double RATE_MUTATION_FACTOR = 0.8;
    private static final int GAMES_IN_MATCH = 1;// games as WHITE and as RED

    private static final String USAGE_TXT = "Usage: write in file AI/Optimization/args.txt: \n" +
            "kidsInGeneration\ngenerationNum\noptimizationType\nprinting\nbotType\n" +
            "where kidsInGeneration and generationNum are ints, optimizationType is genetic/stoHillClimb, \n" +
            "printing is true/false, botType is UserBot/HeuristicBot.";

    // we change this part when moving the peoject
    private static final String BASE_FOLDER = "/tmp/AI";
    //
    private static final String GAME_SERVER_DIR = BASE_FOLDER + "/sdk_run_single_game";
    private static final String ARGS_PATH = BASE_FOLDER + "/Optimization/args.txt";
    private static final String RUN_GAME = GAME_SERVER_DIR + "/run_game_engine_service.sh";
    private static final String RESULTS_DIR = BASE_FOLDER + "/results";
    private static final String BOT_TYPES_PATH = RESULTS_DIR + "/bot_types.txt";
    private static final String BOT1_PATH = RESULTS_DIR + "/bot1.json";
    private static final String BOT2_PATH = RESULTS_DIR + "/bot2.json";
    private static final String BEST_PATH = RESULTS_DIR + "/best.json";
    private static final String OUTCOMES_PATH = RESULTS_DIR + "/winsLog.txt";
    private static final String TEST_LOG_PATH = RESULTS_DIR + "/testLog.txt";
    private static final String TIE = "DEADHEAT";
    private static final String WINNER = "WHITE";

    // how we save the type of bots to transfer to the game
    private static final String USER_BOT = "UserBot";
    private static final String HEURISTIC_BOT = "HeuristicBot";

    // place of each param in the received arguments
    private static final int KIDS_NUM_PARAM = 0;
    private static final int GENERATION_NUM_PARAM = 1;
    private static final int OPTIMIZATION_PARAM = 2;
    private static final int PRINT_PARAM = 3;
    private static final int TYPE_PARAM = 4;
    private static final int PARAM_SIZE = 5;

    // states and nodes of the stochastic hill climb/gentic search
    private static IBot[] generation;
    private static Grades[] grades;
    private static boolean printing;
    private static String[] botType = new String[2];

    /**
    * return a new bot by the type
    */
    private static IBot newBot(String myType, Grades grades) {
        if (myType.equals(USER_BOT)) {
            return new UserBot(grades);
        } else if (myType.equals(HEURISTIC_BOT)) {
            return new HeuristicBot(grades);
        }
        return null;
    }

    /**
    * writes what we whant to the file we want
    */
    private static void writeToFile(String path, String[] text, StandardOpenOption opt) {
        try {
            Files.write(Paths.get(path), Arrays.asList(text), Charset.forName("UTF-8"), opt);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     *
     * @param bot1
     * @param bot2
     * @return the average score for bot1 (as white) (average score for bot2 is 1- this)
     */
    private static double match(IBot bot1, IBot bot2) {
        Gson g = new Gson();
        writeToFile(BOT_TYPES_PATH, new String[]{botType[0], botType[1]}, TRUNCATE_EXISTING);
        writeToFile(BOT1_PATH, new String [] {g.toJson(bot1)}, TRUNCATE_EXISTING);
        writeToFile(BOT2_PATH, new String [] {g.toJson(bot2)}, TRUNCATE_EXISTING);

        try {
            ProcessBuilder pb = new ProcessBuilder(RUN_GAME);
            pb.directory(new File(GAME_SERVER_DIR));
            Process process;
            if (printing) {
                process = pb.inheritIO().start();
            } else {
                process = pb.start();
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        try {
            String[] outcomes = new String(Files.readAllBytes(Paths.get(OUTCOMES_PATH)),
                    StandardCharsets.UTF_8).split("\n");
            double score = 0;
            for (String outcome: outcomes) {
                if (outcome.equals(TIE)) {
                    score += 0.5;
                } else if (outcome.equals(WINNER)) {
                    score++;
                }
            }
            return score / GAMES_IN_MATCH;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return 0;
    }

    /**
     * takes GAMES_IN_MATCH matches as white and GAMES_IN_MATCH as red and return the average score
     */
    private static double[] combat() {
        double[] scores = new double[kidsInGeneration];
        for (int i = 0; i < kidsInGeneration; i ++) {
            scores[i] = 0;
        }
        for (int i = 0; i < kidsInGeneration; i ++) {
            for (int j = i + 1; j < kidsInGeneration; j ++) {
                double m1 = match(generation[i], generation[j]) , m2 = match(generation[j], generation[i]);
                scores[i] += m1 + (GAMES_IN_MATCH -  m2);
                scores[j] += m2 + (GAMES_IN_MATCH -  m1);
            }
        }
        return scores;
    }

    /**
     * chooses a bot on a direct probability to how much he won (multinum probabiliry)
     * @param scores
     */
    private static int chooseBot(double[] scores) {
        double sumScore = 0, sumP = 0;
        for (double s : scores) {
            sumScore += s;
        }
        Random r = new Random();
        double chosen = r.nextDouble() * sumScore;
        for (int i = 0; i < kidsInGeneration; i++) {
            sumP += scores[i];
            if (chosen <= sumP) {
//                writeToFile("/tmp/genetic.txt", new String[] {"\nCHOSEN "+ i +"\n"}, APPEND);
                return i;
            }
        }
        return -1; // can't reach here, laws of probability
    }

    /**
     * couples nodes and creates the next generation in genetic
     */
    private static void nextGenerationGenetic(double[] scores, double mutationFactor) {
        Grades[] nextGen = new Grades[kidsInGeneration];
        for (int i = 0; i < kidsInGeneration; i++) {
            int bot1 = chooseBot(scores), bot2 = chooseBot(scores);
            nextGen[i] = new Grades(grades[bot1], scores[bot1], grades[bot2], scores[bot2], mutationFactor);
        }
        grades = nextGen;
        for (int i = 0; i < kidsInGeneration; i ++) {
            generation[i] = newBot(botType[0], grades[i]);
        }
    }

    /**
     * couples nodes and creates the next generation in genetic
     */
    private static void nextGenerationStochasticHillClimb(double[] scores, double mutationFactor) {
        Grades[] nextGen = new Grades[kidsInGeneration];
        int maxi = 0;
        for (int j = 1; j < kidsInGeneration; j++){
            if (scores[j] > scores[maxi]) {
                maxi = j;
            }
        }
        Grades best = grades[maxi];
        for (int i = 0; i < kidsInGeneration; i++) {
	    if(i != maxi) {
        	nextGen[i] = new Grades(best);
       		nextGen[i].mutate(mutationFactor);
	    }
        }
        grades = nextGen;
        for (int i = 0; i < kidsInGeneration; i ++) {
	    if (i != maxi) {
		generation[i] = newBot(botType[0], grades[i]);
	    }
        }
    }
    
    /**
    * return the type of bot with "name" as its description
    */
    private static String getType(String name) {
    	if (name.indexOf("User") > -1) {
    		return USER_BOT;
    	}
    	return HEURISTIC_BOT;
    }
    
    /**
    * tests all of the best bots we've put in the bests folders to fight
    */
    private static void testBots(){
    	//grade, type
    	Gson g = new Gson();
    	ArrayList<Triple<IBot, String, Double>> bots = new ArrayList<Triple<IBot, String, Double>>();
    	for(int t = 0; t < TYPES.length; t ++) {
    		for(int i = 0; i < BOTS_PER_TYPE; i ++) {
    			try{
    				String path = RESULTS_DIR +"/" + TYPES[t] + "/" + i + ".json";
    				IBot bot = (IBot) g.fromJson(new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8), classes[t]);
    				bots.add(new Triple<IBot, String, Double>(bot, TYPES[t], 0.0));
    			} catch(IOException e) {
      			System.exit(0);
    			}
    		}
    	}
    	
    	bots.add(new Triple<IBot, String, Double>(new UserBot(Grades.getPlannedGrades()), "planned UserBot", 0.0));
    	bots.add(new Triple<IBot, String, Double>(new HeuristicBot(Grades.getPlannedGrades()), "planned HeuristicBor", 0.0));
    	
    	writeToFile(TEST_LOG_PATH, new String[]{"*TEST*"}, TRUNCATE_EXISTING);
    	for (Triple<IBot, String, Double> bot1: bots) {
    		for (Triple<IBot, String, Double> bot2: bots) {
            botType[0] = getType(bot1.y);
            botType[1] = getType(bot2.y);
    			double score = match(bot1.x, bot2.x);
    			bot1.z += score;
    			bot2.z += ( - score);
    			writeToFile(TEST_LOG_PATH, new String[]{bot1.y + "as white vs " + bot2.y + " as red: " + score}, APPEND);
	    	}
    	}
    	Triple<IBot, String, Double> best = bots.get(0);
    	for (Triple<IBot, String, Double> bot: bots) {
    		if (bot.z > best.z) {
    			best = bot;
    		}
    	}
    	writeToFile(TEST_LOG_PATH, new String[]{"\n\n\nbest: " + best.y + "\nscore: " + best.z + "\n"}, APPEND);
    	writeToFile(TEST_LOG_PATH, new String[]{"grades:\n" + g.toJson((best.x).getGrades())}, APPEND);
    }

    /**
     * run eveerything
     */
    public static void main(String[] args) {
        try {
            args = new String(Files.readAllBytes(Paths.get(ARGS_PATH)), StandardCharsets.UTF_8).split("\n");
        } catch (Exception e) {
            System.out.println("Problem with /AI/Optimization/args.txt");
            System.out.println(USAGE_TXT);
            e.printStackTrace();
            System.exit(0);
        }
        if (args.length != PARAM_SIZE) {
        		if (args.length == 1 && args[0].equals("Test")){
        			printing = true;
        			testBots();
        		} else {
            	System.out.println(USAGE_TXT);
        		}
            System.exit(0);
        }
        if (USER_BOT.equals(args[TYPE_PARAM]) || HEURISTIC_BOT.equals(args[TYPE_PARAM])) {
            botType[0] = args[TYPE_PARAM];
            botType[1] = args[TYPE_PARAM];
        } else {
            System.out.println("Usage: type need to be \"" + USER_BOT + "\" or \"" + HEURISTIC_BOT + "\"");
            System.out.println("type is " + args[TYPE_PARAM]);
            System.exit(0);
        }
        Gson g = new Gson();
        kidsInGeneration = Integer.valueOf(args[KIDS_NUM_PARAM]);
        generationNum = Integer.valueOf(args [GENERATION_NUM_PARAM]);
        printing = (args [PRINT_PARAM].equals("true"));
        generation = new IBot[kidsInGeneration];
        grades = new Grades[kidsInGeneration];
        for (int i = 0; i < kidsInGeneration; i ++) {
            grades[i] = new Grades();
            generation[i] = newBot(botType[0], grades[i]);
        }
        double mutationFactor = BASE_MUTATION_FACTOR;
        double[] scores;
        for (int i = 0; i < generationNum; i ++) {
            scores = combat();
            if (args[OPTIMIZATION_PARAM].equals("genetic")) {
                nextGenerationGenetic(scores, mutationFactor);
            } else if (args[OPTIMIZATION_PARAM].equals("stoHillClimb")) {
                nextGenerationStochasticHillClimb(scores, mutationFactor);
            }
            mutationFactor *= RATE_MUTATION_FACTOR;
            int maxi = 0;
            for (int j = 1; j < kidsInGeneration; j++){
                if (scores[j] > scores[maxi]) {
                    maxi = j;
                }
            }

            writeToFile(BEST_PATH, new String[] {g.toJson(generation[maxi])}, TRUNCATE_EXISTING);
            writeToFile(BEST_PATH, new String[] {"generation " + i}, APPEND);
        }
    }
}
