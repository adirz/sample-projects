var Figure = require('./figure'),
    extend = require('util').inherits,
    colors = require('../colors'),
    orientations = require('../orientations');

function Pyramid(orientation, color) {
    Figure.call(this, orientation, color);
}

extend(Pyramid, Figure);

Pyramid.prototype.doesReflectLaserFrom = function (orientation) {
    return this.orientation === orientations.reverse(orientation) || orientations.clockwise(this.orientation) === orientation;
};

Pyramid.prototype.willBeKilledByLaserFrom = function (orientation) {
    return this.orientation !== orientations.reverse(orientation) && orientations.clockwise(this.orientation) !== orientation;
};

Pyramid.prototype.getOrientationOfReflectedLaserFrom = function (orientation) {
    return this.orientation === orientations.reverse(orientation) ? orientations.counterclockwise(this.orientation) : this.orientation;
};

Pyramid.prototype.toString = function () {
    return (this.color === colors.White ? 'P' : 'p') + this.orientation;
};

module.exports = Pyramid;
