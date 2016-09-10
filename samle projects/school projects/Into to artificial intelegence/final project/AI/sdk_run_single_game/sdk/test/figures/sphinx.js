var chai = require('chai'),
    expect = chai.expect,

    orientations = require('../../sdk/orientations'),
    colors = require('../../sdk/colors'),

    Sphinx = require('.././sphinx');

describe('sphinx', function () {
    describe('toString', function () {
        it('white', function () {
            expect(new Sphinx(orientations.North, colors.White).toString()).to.equal('S1');
        });

        it('red', function () {
            expect(new Sphinx(orientations.North, colors.Red).toString()).to.equal('s1');
        });

        it('north', function () {
            expect(new Sphinx(orientations.North, colors.White).toString()).to.equal('S1');
        });

        it('east', function () {
            expect(new Sphinx(orientations.East, colors.White).toString()).to.equal('S2');
        });

        it('south', function () {
            expect(new Sphinx(orientations.South, colors.White).toString()).to.equal('S3');
        });

        it('west', function () {
            expect(new Sphinx(orientations.West, colors.White).toString()).to.equal('S4');
        });
    });

    describe('doesReflectLaserFrom', function () {
        var sphinx = new Sphinx(orientations.North, colors.White);

        it('north', function () {
            expect(sphinx.doesReflectLaserFrom(orientations.North)).to.be.false;
        });

        it('east', function () {
            expect(sphinx.doesReflectLaserFrom(orientations.East)).to.be.false;
        });

        it('south', function () {
            expect(sphinx.doesReflectLaserFrom(orientations.South)).to.be.false;
        });

        it('west', function () {
            expect(sphinx.doesReflectLaserFrom(orientations.West)).to.be.false;
        });
    });

    describe('willBeKilledByLaserFrom', function () {
        var sphinx = new Sphinx(orientations.North, colors.White);

        it('north', function () {
            expect(sphinx.willBeKilledByLaserFrom(orientations.North)).to.be.false;
        });

        it('east', function () {
            expect(sphinx.willBeKilledByLaserFrom(orientations.East)).to.be.false;
        });

        it('south', function () {
            expect(sphinx.willBeKilledByLaserFrom(orientations.South)).to.be.false;
        });

        it('west', function () {
            expect(sphinx.willBeKilledByLaserFrom(orientations.West)).to.be.false;
        });
    });
});