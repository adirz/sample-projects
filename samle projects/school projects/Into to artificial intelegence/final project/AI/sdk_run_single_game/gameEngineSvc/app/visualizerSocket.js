var GameEngine = require('../GameEngine');

module.exports = function(io) {
    GameEngine.on('newgame', function() {
        io.emit('new_game');
    });
    GameEngine.on('move', function() {
        io.emit('move', GameEngine.movesLog);
    });
    GameEngine.on('gameover', function(gameOver) {
        io.emit('game_over', gameOver);
    });
    io.on('connection', function(socket) {
        socket.emit('new_game', {
            boardNotation: GameEngine.initialState.boardNotation,
            bots: GameEngine.gameData.bots
        });
        socket.emit('move', GameEngine.movesLog);
        socket.emit('game_over', GameEngine.gameOver);
    });
};