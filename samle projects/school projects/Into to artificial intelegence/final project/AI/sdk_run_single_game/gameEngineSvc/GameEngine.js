var util = require('util'),
    rest = require('restler'),
    utils = require('../sdk/utils'),
    colors = require('../sdk/colors'),
    Pharaoh = require('../sdk/figures/pharaoh'),
    logger = require(__game_engine_logger+'logger'),
    EventEmitter = require('events').EventEmitter,
    fs = require('fs');

var BASE_FOLDER = "/tmp/AI";

function GameEngine(){
    var self = this;
    this.movesLog = [];
    this.pastMoves = {};

    EventEmitter.call(this);

    this.on('gameover', function(gameoverData) {
        self.isGameOver = true;

        logger.info('--- GAME OVER --- round=' + gameoverData.round + ' game='+gameoverData.game+ ' type='+gameoverData.type+
        ' player1.Id='+gameoverData.player1.id + ' player1.color='+gameoverData.player1.color +
        ' player2.Id='+gameoverData.player2.id + ' player2.color='+gameoverData.player2.color );

        logger.debug(gameoverData);
        self.gameOver = gameoverData;

	var colorWon = "DEADHEAT";
   	if (gameoverData.player1.score > gameoverData.player2.score) {
   	     colorWon = "WHITE";
	} else if (gameoverData.player1.score < gameoverData.player2.score) {
		colorWon = "RED";
	}
	fs.appendFileSync(self.WIN_LOG, colorWon); 

	// Kill old game and start a new one
	self.gameClient.kill("SIGINT");
	self.timesToRun--;
	if (self.timesToRun > 0) {
		self.emit('startNewGame');
	} else {
		process.exit();
	}
    });
	
   // create the win log file
   this.WIN_LOG = BASE_FOLDER + "/results/winsLog.txt";
   fs.closeSync(fs.openSync(this.WIN_LOG, 'w'));
   this.timesToRun = 1;
   this.gameClient = null;
	
   this.on('startNewGame', function() {
	self.gameClient = require('child_process').spawn(BASE_FOLDER + '/Java/gradlew', ['clean','run'], {
	  cwd: BASE_FOLDER + "/Java/",
	  env: process.env
	});
	self.gameClient.stdout.on('data', function(data) {
		console.log('stdout: ' + data);
	});
	self.gameClient.stderr.on('data', function(data) {
		console.log('stderr: ' + data);
	});
	self.gameClient.stdout.on('close', function(data) {
		console.log('closing: ' + data);
	});
   });
   self.emit('startNewGame');
}

util.inherits(GameEngine, EventEmitter);

util._extend(GameEngine.prototype,{
    GameEngine: GameEngine,
    
    init: function (gameData, botMoveTimeout, isInLocalMode) {
        logger.debug("Init game");

        this.botMoveTimeout = botMoveTimeout;
        this.isInLocalMode = isInLocalMode;
        this.gameData = gameData;
        this.initialState = util._extend({}, gameData);

        this.gameData.bots.forEach(function(bot){ bot.log = ''; });
        this.gameData.connectedBots = 0;

        this.isGameOver = false;
        this.movesLog = [];
        this.gameOver = null;
        this.pastMoves = {};

        this.board = utils.createBoardFromPosition(this.gameData.boardNotation);
        this.boardInitalState = utils.serializeBoard(this.board);
    },

    restart: function (bots) {
        if (!this.isInLocalMode){
            logger.warn("Somebody trying to re-start the game. That is not allowed on server mode");
            return;
        }

        logger.debug("Re-start game");

        var gameData = util._extend({},this.initialState);
        gameData.bots = bots;
        this.init(gameData, this.botMoveTimeout, this.isInLocalMode);
        this.emit('newgame', bots);
    },

    botConnected: function(botAddress, botData)
    {
        var self = this;
        logger.info("Bot connected from " + botAddress);
        if (this.isGameOver){
            logger.info("Game is over. Not accepting new bots");
            return;
        }

        if (this.gameData.connectedBots > 1) {
            logger.warn("Only two bots are allowed to play");
            return;
        }

        if (this.isInLocalMode) { // LocalRunner mode
            this.gameData.bots.forEach(function(bot){
                if (bot.userId == botData.userId){
                    bot.address = "localhost";
                    self.gameData.connectedBots++;
                }
            });
        }
        else {
            this.gameData.bots.forEach(function(bot){
                if (bot.container.details.NetworkSettings.IPAddress == botAddress){
                    bot.address = botAddress;
                    self.gameData.connectedBots++;
                }


                // TODO: possibly we don't need that. Get a coffee, have a rest and think
                if (bot.died){ // check if there is a died bot, so connected one wins
                    self.isGameOver = true;

                    logger.info("GameEngine: One of connected bots is died, address:", bot.folder);
                    var gameOverData  = self.prepareGameOverData("gameover", bot.color);
                    self.emit("gameover", gameOverData);
                    return;
                }
            })
        }

        logger.debug("Connected bots:", this.gameData.connectedBots);

        if (this.gameData.connectedBots === 2) {
            this.gameData.currentTurn = this.gameData.bots[0].color === colors.White ? 0 : 1;
            this.getNextBotMove();
        }
    },

    prepareGameOverData: function(gameOverType, lostColor){
        var bots = this.gameData.bots;
        var type = gameOverType.toLowerCase();

        function preparePlayerData(bot) {
            var data = {
                id: bot.userId,
                score: type == "botsdied" ? 0 : (type == "draw" ? 0.5 : (bot.color == lostColor ? 0 : 1)),
                color: bot.color,
                log: bot.log || '',
                botDied: bot.died || false
            };

            return data;
        }

        var gameOverData = {
            round: this.gameData.roundId,
            game: this.gameData.id,
            type: gameOverType,
            player1: preparePlayerData(bots[0]),
            player2: preparePlayerData(bots[1]),
            boardNotation : this.boardInitalState,
            moves : this.movesLog
        };

        return gameOverData;
    },

    getNextBotMove: function() {
        var self = this;

        function emitGameOver(gameOverType, lostColor) {
            self.emit("gameover", self.prepareGameOverData(gameOverType, lostColor));
        }

        function playRound(botMove) {
            logger.debug("Move from bot color: " + currentBot.color +" move: "+ botMove);  

            if (utils.isMoveValid(this.board, currentBot.color, botMove))
            {
                utils.makeMove(this.board, botMove);
                var figure = utils.getFigureToBeKilledBySphinx(this.board, currentBot.color);
                var moveData = {
                    move: botMove,
                    laser: utils.getLaserPathSegmentsFromSphinx(this.board, currentBot.color).map(function(el) { return '' + el.y + el.x;}),
                    killedFigure: figure && utils.getCoordinatesOfFigure(this.board, figure)
                };
                this.movesLog.push(moveData);
                this.emit('move');
                if (figure != null)
                {
                    utils.removeFigureFromBoard(this.board, figure);
                    if (figure instanceof Pharaoh){
                        logger.info("Game #"+this.gameData.id+" is finished by killing pharaon." );
                        emitGameOver("gameover",figure.color);
                        return;
                    }
                }
                
                var serializedBoard = utils.serializeBoardForDrawCheck(this.board);
                
                //check for draw
                if(! this.pastMoves[serializedBoard]){
                    this.pastMoves[serializedBoard] = 1;
                }
                else{
                    this.pastMoves[serializedBoard]++;
                    if (this.pastMoves[serializedBoard] == 3) {
                        logger.info("Game #"+this.gameData.id+" exceeds 3 repetitions moves. Draw assigned." );
                        emitGameOver("draw");
                        return;
                    }
                }
                if (this.movesLog.length>=100) {
                        logger.info("Game #"+this.gameData.id+" exceeds 100 moves. Draw assigned." );
                        emitGameOver("draw");
                        return;
                }
               
                this.gameData.boardNotation = utils.serializeBoard(this.board);
            }
            else
            {
		logger.warn("[Invalid bot move] Move from bot color: " + currentBot.color +" move: "+ botMove, this.board);  
                logger.info("Game #"+this.gameData.id+" is finished because of invalid move." );
                emitGameOver("invalidMove",currentBot.color);
                return;   
            }
            
            this.getNextBotMove();
        }
        
        var currentBot = this.gameData.bots[this.gameData.currentTurn++];
        if (this.gameData.currentTurn > 1){this.gameData.currentTurn = 0; }
        
        var address = "http://"+currentBot.address+":8081/move";
        logger.debug("Get move from: "+ address);
        
        rest.postJson(address, { board: this.gameData.boardNotation, color: currentBot.color }, {timeout: this.botMoveTimeout})
            .on("complete", playRound.bind(this))
            .on("timeout", function(){
                emitGameOver("timeout",currentBot.color);
                logger.warn("Bot timeout");
            });
    }
});

module.exports = new GameEngine();
