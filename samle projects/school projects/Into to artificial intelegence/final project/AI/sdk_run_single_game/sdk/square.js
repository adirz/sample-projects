function Square(x, y, color) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.figure = null;
}

Square.prototype.hasFigure = function () {
    return this.figure !== null;
};

Square.prototype.getFigure = function () {
    return this.figure;
};

Square.prototype.removeFigure = function () {
    this.figure = null;
};

Square.prototype.setFigure = function (figure) {
    this.figure = figure;
};

module.exports = Square;
