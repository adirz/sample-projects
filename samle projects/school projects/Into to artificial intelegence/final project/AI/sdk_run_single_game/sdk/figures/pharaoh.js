var Figure = require('./figure'),
    extend = require('util').inherits,
    colors = require('../colors'),
    orientations = require('../orientations');

function Pharaoh(orientation, color) {
    Figure.call(this, orientation, color);
}

extend(Pharaoh, Figure);

Pharaoh.prototype.doesReflectLaserFrom = function () {
    return false;
};

Pharaoh.prototype.willBeKilledByLaserFrom = function () {
    return true;
};

Pharaoh.prototype.toString = function () {
    return (this.color === colors.White ? 'PH' : 'ph');
};

module.exports = Pharaoh;
