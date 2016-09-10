var chai = require('chai'),
    expect = chai.expect,

    colors = require('../../sdk/colors'),
    orientations = require('../../sdk/orientations'),
    rotations = require('../../sdk/rotations'),

    Scarab = require('.././scarab');

describe('figure', function () {
    describe('rotate', function () {
        it('clockwise: North to East', function () {
            var figure = new Scarab(orientations.North, colors.Red);

            figure.rotate(rotations.clockwise);

            expect(figure.orientation).to.equal(orientations.East);
        });

        it('counterclockwise: South to East', function () {
            var figure = new Scarab(orientations.South, colors.Red);

            figure.rotate(rotations.counterclockwise);

            expect(figure.orientation).to.equal(orientations.East);
        });
    });
});

