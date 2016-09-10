var Figure = require('./figure'),
    extend = require('util').inherits,
    colors = require('../colors'),
    orientations = require('../orientations');

function Anubis(orientation, color) {
    Figure.call(this, orientation, color);
}

extend(Anubis, Figure);

Anubis.prototype.doesReflectLaserFrom = function () {
    return false;
};

Anubis.prototype.willBeKilledByLaserFrom = function (orientation) {
    return this.orientation !== orientations.reverse(orientation);
};

Anubis.prototype.toString = function () {
    return (this.color === colors.White ? 'A' : 'a') + this.orientation;
};

module.exports = Anubis;