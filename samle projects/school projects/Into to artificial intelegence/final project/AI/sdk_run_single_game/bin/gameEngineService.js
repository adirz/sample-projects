global.__game_engine_logger = '../bin/';

var gameEngineSvc = require('../gameEngineSvc'),
    gameEngine = require('../gameEngineSvc/GameEngine'),
    colors = require('../sdk/colors'),
    config = require('../config');

var gameData = {
    roundId : 1,
    id: 1234,
    bots :[{userId: 1, color: colors.Red}, {userId: 2, color: colors.White}],
    boardNotation: 'PH 74, S1 79, C1 44, C2 45, A1 73, A1 75, P1 23, P1 39, P1 42, P1 72, ' +
    'P2 67, P4 32, P4 49, ph 05, s3 00, c1 35, c2 34, a3 04, a3 06, p2 30, p2 47, p3 07, p3 37, p3 40, p3 56, p4 12'
};

gameEngine.init(gameData, config.gameEngineSvc.botMoveTimeout, config.gameEngineSvc.isInLocalMode);
gameEngineSvc(config.gameEngineSvc.isInLocalMode);