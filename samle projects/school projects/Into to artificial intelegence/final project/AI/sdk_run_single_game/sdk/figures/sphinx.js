var Figure = require('./figure'),
    extend = require('util').inherits,
    colors = require('../colors'),
    orientations = require('../orientations');

function Sphinx(orientation, color) {
    Figure.call(this, orientation, color);
}

extend(Sphinx, Figure);

Sphinx.prototype.doesReflectLaserFrom = function () {
    return false;
};

Sphinx.prototype.willBeKilledByLaserFrom = function () {
    return false;
};

Sphinx.prototype.toString = function () {
    return (this.color === colors.White ? 'S' : 's') + this.orientation;
};

module.exports = Sphinx;
