var chai = require('chai'),
    expect = chai.expect,

    orientations = require('../../sdk/orientations'),
    colors = require('../../sdk/colors'),

    Scarab = require('.././scarab');

describe('scarab', function () {
    describe('toString', function () {
        it('white', function () {
            expect(new Scarab(orientations.North, colors.White).toString()).to.equal('C1');
        });

        it('red', function () {
            expect(new Scarab(orientations.North, colors.Red).toString()).to.equal('c1');
        });

        it('north', function () {
            expect(new Scarab(orientations.North, colors.White).toString()).to.equal('C1');
        });

        it('east', function () {
            expect(new Scarab(orientations.East, colors.White).toString()).to.equal('C2');
        });

        it('south', function () {
            expect(new Scarab(orientations.South, colors.White).toString()).to.equal('C1');
        });

        it('west', function () {
            expect(new Scarab(orientations.West, colors.White).toString()).to.equal('C2');
        });
    });

    describe('doesReflectLaserFrom', function () {
        var scarab = new Scarab(orientations.North, colors.White);

        it('north', function () {
            expect(scarab.doesReflectLaserFrom(orientations.North)).to.be.true;
        });

        it('east', function () {
            expect(scarab.doesReflectLaserFrom(orientations.East)).to.be.true;
        });

        it('south', function () {
            expect(scarab.doesReflectLaserFrom(orientations.South)).to.be.true;
        });

        it('west', function () {
            expect(scarab.doesReflectLaserFrom(orientations.West)).to.be.true;
        });
    });

    describe('willBeKilledByLaserFrom', function () {
        var scarab = new Scarab(orientations.North, colors.White);

        it('north', function () {
            expect(scarab.willBeKilledByLaserFrom(orientations.North)).to.be.false;
        });

        it('east', function () {
            expect(scarab.willBeKilledByLaserFrom(orientations.East)).to.be.false;
        });

        it('south', function () {
            expect(scarab.willBeKilledByLaserFrom(orientations.South)).to.be.false;
        });

        it('west', function () {
            expect(scarab.willBeKilledByLaserFrom(orientations.West)).to.be.false;
        });
    });

    describe('getOrientationOfReflectedLaserFrom', function () {
        var scarab = new Scarab(orientations.North, colors.White);

        it('north', function () {
            expect(scarab.getOrientationOfReflectedLaserFrom(orientations.North)).to.equal(orientations.East);
        });

        it('east', function () {
            expect(scarab.getOrientationOfReflectedLaserFrom(orientations.East)).to.equal(orientations.North);
        });

        it('south', function () {
            expect(scarab.getOrientationOfReflectedLaserFrom(orientations.South)).to.equal(orientations.West);
        });

        it('west', function () {
            expect(scarab.getOrientationOfReflectedLaserFrom(orientations.West)).to.equal(orientations.South);
        });
    });
});