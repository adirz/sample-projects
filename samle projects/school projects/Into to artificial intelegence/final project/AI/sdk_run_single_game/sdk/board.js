var colors = require('./colors'),
    Square = require('./square');

function Board() {
    this.width = 10;
    this.height = 8;

    this.squares = [];
    for (var y = 0; y < this.height; ++y) {
        this.squares[y] = [];
        for (var x = 0; x < this.width; ++x) {
            var squareColor;
            if (x === 0 || (x == 8 && (y === 0 || y === 7))) {
                squareColor = colors.Red;
            }
            else if (x === 9 || (x == 1 && (y === 0 || y === 7))) {
                squareColor = colors.White;
            }
            else {
                squareColor = colors.Neutral;
            }
            this.squares[y][x] = new Square(x, y, squareColor);
        }
    }
}

Board.prototype = {
    getSquare: function (x, y) {
        return this.squares[y][x];
    },
    outOfBounds: function (x, y) {
        return x < 0 || x >= this.width || y < 0 || y >= this.height;
    }
};

module.exports = Board;