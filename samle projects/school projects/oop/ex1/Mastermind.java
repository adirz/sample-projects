import il.ac.huji.cs.oop.mastermind.*;


public class Mastermind {
	private static final String CODE_LEN = "Enter code length:";
	private static final String POSS_VAL = "Enter number of values:";
	private static final String MAX_GUESS = "Enter max number of guesses:";
	private static final String USER_GUESS = "Enter guess:";
	private static final String CONTINUE = "Another game?";
	private static final
	String CHANGE_GAME = "Do you want to change the game options?";
	private static final String ERROR_MSG = "Value must be positive:";
	private static final String WIN_START = "You won in ";
	private static final String WIN_END = " turns!";	
	private static final 
	String LOSE = "You lost! You failed to find the code!";
	
	public static class Statistics{
		public int gamesPlayed;
		public int gamesWon;
		public double winRate;
		public int totalWinLen;
		public double avrgWinLen;
		
		public Statistics(){
			gamesPlayed = 0;
			gamesWon = 0;
			totalWinLen = 0;
			winRate = 0;
			avrgWinLen = 0;
		}
		
		public void add_game(){
			gamesPlayed ++;
		}
		
		public void won(int winLen){
			totalWinLen += winLen;
			gamesWon ++;
		}
		
		public void refresh(){
			winRate = gamesWon/gamesPlayed;
			if(gamesWon != 0)
				avrgWinLen = totalWinLen/gamesWon;
			else{
				avrgWinLen = Double.NaN;
			}
		}
	}
	
	public static void main(String[] args) {
		boolean gameContinue = true, won = false, changeOpt = true;	
		Statistics stats = new Statistics();
		MastermindUI ui = MastermindUIFactory.newMastermindUI();	
		int codeLen = -1, numPosVal = -1, max_guess = -1;
		Code code, guess;
		
		int index;
		int bulls, cows;
		
		while(gameContinue){
			if(changeOpt){
				stats = new Statistics();
				changeOpt = false;
				codeLen = -1;
				numPosVal = -1;
				max_guess = -1;
				while(codeLen < 0){
					codeLen = ui.askNumber(CODE_LEN);
					if(codeLen < 0){
						ui.displayErrorMessage(ERROR_MSG);
					}
				}
				while(numPosVal <0){
					numPosVal = ui.askNumber(POSS_VAL);
					if(numPosVal <0){
						ui.displayErrorMessage(ERROR_MSG);
					}
				}
				while(max_guess <0){
					max_guess = ui.askNumber(MAX_GUESS);
					if(max_guess <0){
						ui.displayErrorMessage(ERROR_MSG);
					}
				}
				ui.reset(codeLen, numPosVal, max_guess);
			}
			
			stats.add_game();

			code = CodeGenerator.newCode(codeLen,numPosVal);
			bulls = 0;
			cows = 0;
			for(index = 0; index < max_guess && !won; index ++){
				guess = ui.askGuess(USER_GUESS,codeLen);
				if(guess.equals(code)){
					won = true;
					bulls = codeLen;
				}else{
					for(int pos = 1; pos <= codeLen ; pos ++){
						if(guess.getValue(pos) == code.getValue(pos)){
							bulls++;
						}else{
							for(int cowPos = 1;cowPos <= codeLen;cowPos ++){
								if(guess.getValue(pos)==code.getValue(cowPos)
									&& guess.getValue(cowPos)!=
										code.getValue(cowPos)){
									
									cows ++;
									cowPos = codeLen + 1;
								}
							}
						}
					}
					for(int val = 0;val <= numPosVal;val ++){
						int diff = 0;
						boolean isCow = false;
						for(int cowPos = 1;cowPos <= codeLen;cowPos ++){
							if(guess.getValue(cowPos) != code.getValue(cowPos)){
								if(guess.getValue(cowPos) == val){
									diff ++;
								}
								if(code.getValue(cowPos) == val){
									isCow = true;
									diff --;
								}
							}
						}
						if(diff > 0 && isCow){
							cows -= diff;
						}
					}
				}
				ui.showGuessResult(guess, bulls, cows);
				bulls = 0;
				cows = 0;
			}
			if(won){
				ui.displayMessage(WIN_START+index+WIN_END);
				stats.won(index);
				won = false;
			}else{
				ui.displayMessage(LOSE);
			}
			won = false;
			stats.refresh();
			ui.showStats(stats.gamesPlayed, stats.gamesWon, stats.winRate, 
					stats.avrgWinLen);
			gameContinue = ui.askYesNo(CONTINUE);
			if(gameContinue){
				if(ui.askYesNo(CHANGE_GAME)){
					changeOpt = true;
				}else{
					ui.clear();
				}
			}
		}
		ui.close();
	}

}
