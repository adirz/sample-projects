var Figure = require('./figure'),
    extend = require('util').inherits,
    colors = require('../colors'),
    orientations = require('../orientations');

function Scarab(orientation, color) {
    Figure.call(this, orientation, color);
}

extend(Scarab, Figure);

Scarab.prototype.doesReflectLaserFrom = function () {
    return true;
};

Scarab.prototype.willBeKilledByLaserFrom = function () {
    return false;
};

Scarab.prototype.getOrientationOfReflectedLaserFrom = function (orientation) {
    return orientation === this.orientation || orientation === orientations.reverse(this.orientation) ? orientations.clockwise(orientation) : orientations.counterclockwise(orientation);
};

Scarab.prototype.toString = function () {
    return (this.color === colors.White ? 'C' : 'c') + (this.orientation === orientations.North || this.orientation === orientations.South ? orientations.North : orientations.East);
};

module.exports = Scarab;
