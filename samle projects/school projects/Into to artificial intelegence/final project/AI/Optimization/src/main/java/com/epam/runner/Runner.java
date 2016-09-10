package com.epam.runner;

import com.epam.bot.DefaultBot;
import com.epam.bot.UserBot;
import com.epam.bot.HeuristicBot;
import com.epam.sdk.Board;
import com.epam.sdk.Color;
import com.epam.sdk.GameState;
import com.epam.sdk.IBot;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.epam.sdk.Color.WHITE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.Collections;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.Files;

import com.google.gson.Gson;


/**
 * This class run bot.
 */
public class Runner {
  private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

  //
  public static final long MAX_MOVE_TIME = TimeUnit.SECONDS.toMillis(15);

  //
  private static final String BOT_TYPES_PATH = "/tmp/AI/results/bot_types.txt";
  private static final String BOT1_PATH = "/tmp/AI/results/bot1.json";
  private static final String BOT2_PATH = "/tmp/AI/results/bot2.json";

  //
  private static final String USER_BOT = "UserBot";
  private static final String DEFAULT_BOT = "DefaultBot";
  private static final String HEURISTIC_BOT = "HeuristicBot";

  //
  public static final Color USER_BOT_COLOR = WHITE;

  //
  private static final Map<String, Class> getClass;

  // init the getClass map
  static {
    Map<String, Class> tempMap = new HashMap<>();
    tempMap.put(USER_BOT, UserBot.class);
    tempMap.put(DEFAULT_BOT, DefaultBot.class);
    tempMap.put(HEURISTIC_BOT, HeuristicBot.class);
    getClass = Collections.unmodifiableMap(tempMap);
  }

  final List<IBot> bots = new ArrayList<>();
  final int botCount;
  int currentTurn = 0;

  Runner() {
    Gson g = new Gson();
    String[] types;

    // Read the bots from the input files
    try {
      types = new String(Files.readAllBytes(Paths.get(BOT_TYPES_PATH)), StandardCharsets.UTF_8).split("\n");
      bots.add((IBot) g.fromJson(new String(Files.readAllBytes(Paths.get(BOT1_PATH)),
                                 StandardCharsets.UTF_8), getClass.get(types[0])));
      bots.add((IBot) g.fromJson(new String(Files.readAllBytes(Paths.get(BOT2_PATH)),
                                 StandardCharsets.UTF_8), getClass.get(types[1])));
    } catch (IOException e) {
      System.exit(0);
    }

//      bots.add(new HeuristicBot());
//      bots.add(new UserBot());
//    bots.add(new DefaultBot());

    botCount = isLocaleMode() ? 2 : 1;
  }

  private boolean isLocaleMode() {
    String localMode = System.getenv("IS_IN_LOCAL_MODE");
    return localMode == null || Boolean.valueOf(localMode);
  }
	
  /**
   * Method switch bot, and build move from current bot.
   * This method runs camel.
   */
  public String getMove(Exchange exchange) {
    Map<String, Object> body = (Map) exchange.getIn().getBody(); //Raw data from server
    GameState gameState = GameState.parse(body);
    Board board = gameState.getBoard();
    long start = System.currentTimeMillis();
    String currentMove = bots.get(currentTurn).nextMove( board, gameState.getColor());
    long end = System.currentTimeMillis();
    long calculatedTime = end - start;
    LOG.info("Elapsed time for move is {} ms. (max " + MAX_MOVE_TIME + " ms)", calculatedTime);

    if (calculatedTime > MAX_MOVE_TIME) {
      LOG.info( "[BOT MOVE TIMEOUT]" );
      if (isLocaleMode()) {
        System.exit(1);
      }
    }
    //Choose other bot
    currentTurn = (currentTurn + 1) % botCount;
    return currentMove;
  }
}
