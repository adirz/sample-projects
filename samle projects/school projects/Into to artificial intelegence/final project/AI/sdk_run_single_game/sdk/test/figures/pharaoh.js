var chai = require('chai'),
    expect = chai.expect,

    orientations = require('../../sdk/orientations'),
    colors = require('../../sdk/colors'),

    Pharaoh = require('.././pharaoh');

describe('pharaoh', function () {
    describe('toString', function () {
        it('white', function () {
            expect(new Pharaoh(orientations.North, colors.White).toString()).to.equal('PH');
        });

        it('red', function () {
            expect(new Pharaoh(orientations.North, colors.Red).toString()).to.equal('ph');
        });

        it('north', function () {
            expect(new Pharaoh(orientations.North, colors.White).toString()).to.equal('PH');
        });

        it('east', function () {
            expect(new Pharaoh(orientations.East, colors.White).toString()).to.equal('PH');
        });

        it('south', function () {
            expect(new Pharaoh(orientations.South, colors.White).toString()).to.equal('PH');
        });

        it('west', function () {
            expect(new Pharaoh(orientations.West, colors.White).toString()).to.equal('PH');
        });
    });

    describe('doesReflectLaserFrom', function () {
        var pharaoh = new Pharaoh(orientations.North, colors.White);

        it('north', function () {
            expect(pharaoh.doesReflectLaserFrom(orientations.North)).to.be.false;
        });

        it('east', function () {
            expect(pharaoh.doesReflectLaserFrom(orientations.East)).to.be.false;
        });

        it('south', function () {
            expect(pharaoh.doesReflectLaserFrom(orientations.South)).to.be.false;
        });

        it('west', function () {
            expect(pharaoh.doesReflectLaserFrom(orientations.West)).to.be.false;
        });
    });

    describe('willBeKilledByLaserFrom', function () {
        var pharaoh = new Pharaoh(orientations.North, colors.White);

        it('north', function () {
            expect(pharaoh.willBeKilledByLaserFrom(orientations.North)).to.be.true;
        });

        it('east', function () {
            expect(pharaoh.willBeKilledByLaserFrom(orientations.East)).to.be.true;
        });

        it('south', function () {
            expect(pharaoh.willBeKilledByLaserFrom(orientations.South)).to.be.true;
        });

        it('west', function () {
            expect(pharaoh.willBeKilledByLaserFrom(orientations.West)).to.be.true;
        });
    });
});