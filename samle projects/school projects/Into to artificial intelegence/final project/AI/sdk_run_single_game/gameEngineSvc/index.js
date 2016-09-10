var express = require('express'),
    config = require('../config'),
    logger = require(__game_engine_logger + 'logger'),
    socket = require('socket.io'),
    server, io;

module.exports = function(isInLocalMode) {
    var app = express();

    app.set('port', config.gameEngineSvc.port);
    server = app.listen(app.get('port'), function() {
        logger.info('Game Engine started on port: ' + app.get('port'));
        isInLocalMode && logger.info('Visualizer mode ON');
    });

    isInLocalMode && app.use('/', require('./visualizerSvc')(socket.listen(server)));
    app.use('/api', require('./app/localRunnerApi'));
};
