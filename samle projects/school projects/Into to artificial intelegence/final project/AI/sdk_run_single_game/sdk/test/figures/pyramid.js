var chai = require('chai'),
    expect = chai.expect,

    orientations = require('../../sdk/orientations'),
    colors = require('../../sdk/colors'),

    Pyramid = require('.././pyramid');

describe('pyramid', function () {
    describe('toString', function () {
        it('white', function () {
            expect(new Pyramid(orientations.North, colors.White).toString()).to.equal('P1');
        });

        it('red', function () {
            expect(new Pyramid(orientations.North, colors.Red).toString()).to.equal('p1');
        });

        it('north', function () {
            expect(new Pyramid(orientations.North, colors.White).toString()).to.equal('P1');
        });

        it('east', function () {
            expect(new Pyramid(orientations.East, colors.White).toString()).to.equal('P2');
        });

        it('south', function () {
            expect(new Pyramid(orientations.South, colors.White).toString()).to.equal('P3');
        });

        it('west', function () {
            expect(new Pyramid(orientations.West, colors.White).toString()).to.equal('P4');
        });
    });

    describe('doesReflectLaserFrom', function () {
        var pyramid = new Pyramid(orientations.North, colors.White);

        it('north', function () {
            expect(pyramid.doesReflectLaserFrom(orientations.North)).to.be.false;
        });

        it('east', function () {
            expect(pyramid.doesReflectLaserFrom(orientations.East)).to.be.true;
        });

        it('south', function () {
            expect(pyramid.doesReflectLaserFrom(orientations.South)).to.be.true;
        });

        it('west', function () {
            expect(pyramid.doesReflectLaserFrom(orientations.West)).to.be.false;
        });
    });

    describe('willBeKilledByLaserFrom', function () {
        var pyramid = new Pyramid(orientations.North, colors.White);

        it('north', function () {
            expect(pyramid.willBeKilledByLaserFrom(orientations.North)).to.be.true;
        });

        it('east', function () {
            expect(pyramid.willBeKilledByLaserFrom(orientations.East)).to.be.false;
        });

        it('south', function () {
            expect(pyramid.willBeKilledByLaserFrom(orientations.South)).to.be.false;
        });

        it('west', function () {
            expect(pyramid.willBeKilledByLaserFrom(orientations.West)).to.be.true;
        });
    });

    describe('getOrientationOfReflectedLaserFrom', function () {
        var pyramid = new Pyramid(orientations.North, colors.White);

        it('east', function () {
            expect(pyramid.getOrientationOfReflectedLaserFrom(orientations.East)).to.equal(orientations.North);
        });

        it('south', function () {
            expect(pyramid.getOrientationOfReflectedLaserFrom(orientations.South)).to.equal(orientations.West);
        });
    });
});