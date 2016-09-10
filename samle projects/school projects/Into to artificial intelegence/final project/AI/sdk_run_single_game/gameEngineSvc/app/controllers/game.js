var gameEngine = require('../../GameEngine')

exports.init =  function(req, res) {
    gameEngine.restart(req.body);
    res.end();
};

exports.connectionBot = function(req, res) {
    gameEngine.botConnected(req.connection.remoteAddress, req.body);
    res.end();
};