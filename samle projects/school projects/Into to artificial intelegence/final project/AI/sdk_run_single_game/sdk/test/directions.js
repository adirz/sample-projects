var chai = require('chai'),
    expect = chai.expect,

    orientations = require('../orientations');

describe('orientations', function () {
    describe('reverse', function () {
        it('north to south', function () {
            expect(orientations.reverse(orientations.North)).to.equal(orientations.South);
        });

        it('east to west', function () {
            expect(orientations.reverse(orientations.East)).to.equal(orientations.West);
        });

        it('south to north', function () {
            expect(orientations.reverse(orientations.South)).to.equal(orientations.North);
        });

        it('west to east', function () {
            expect(orientations.reverse(orientations.West)).to.equal(orientations.East);
        });
    });

    describe('clockwise', function () {
        it('north to east', function () {
            expect(orientations.clockwise(orientations.North)).to.equal(orientations.East);
        });

        it('east to south', function () {
            expect(orientations.clockwise(orientations.East)).to.equal(orientations.South);
        });

        it('south to west', function () {
            expect(orientations.clockwise(orientations.South)).to.equal(orientations.West);
        });

        it('west to north', function () {
            expect(orientations.clockwise(orientations.West)).to.equal(orientations.North);
        });
    });

    describe('counterclockwise', function () {
        it('north to west', function () {
            expect(orientations.counterclockwise(orientations.North)).to.equal(orientations.West);
        });

        it('east to north', function () {
            expect(orientations.counterclockwise(orientations.East)).to.equal(orientations.North);
        });

        it('south to east', function () {
            expect(orientations.counterclockwise(orientations.South)).to.equal(orientations.East);
        });

        it('west to south', function () {
            expect(orientations.counterclockwise(orientations.West)).to.equal(orientations.South);
        });
    });
});