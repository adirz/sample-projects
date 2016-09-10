var orientations = require('../orientations'),
    rotations = require('../rotations');

function Figure(orientation, color) {
    this.orientation = orientation;
    this.color = color;
}

Figure.prototype = {
    rotate: function (rotation) {
        this.orientation = (rotation === rotations.clockwise ? orientations.clockwise : orientations.counterclockwise).call(orientations, this.orientation);
    },
    doesReflectLaserFrom: function () {
        throw new Error("Not implemented");
    },
    willBeKilledByLaserFrom: function () {
        throw new Error("Not implemented");
    },
    getOrientationOfReflectedLaserFrom: function () {
        throw new Error("Not implemented");
    },
    toString: function () {
        throw new Error('Not implemented');
    }
};

module.exports = Figure;