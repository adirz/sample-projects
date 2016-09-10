var chai = require('chai'),
    expect = chai.expect,

    orientations = require('../../sdk/orientations'),
    colors = require('../../sdk/colors'),

    Anubis = require('.././anubis');

describe('anubis', function () {
    describe('toString', function () {
        it('white', function () {
            expect(new Anubis(orientations.North, colors.White).toString()).to.equal('A1');
        });

        it('red', function () {
            expect(new Anubis(orientations.North, colors.Red).toString()).to.equal('a1');
        });

        it('north', function () {
            expect(new Anubis(orientations.North, colors.White).toString()).to.equal('A1');
        });

        it('east', function () {
            expect(new Anubis(orientations.East, colors.White).toString()).to.equal('A2');
        });

        it('south', function () {
            expect(new Anubis(orientations.South, colors.White).toString()).to.equal('A3');
        });

        it('west', function () {
            expect(new Anubis(orientations.West, colors.White).toString()).to.equal('A4');
        });
    });

    describe('doesReflectLaserFrom', function () {
        var anubis = new Anubis(orientations.North, colors.White);

        it('north', function () {
            expect(anubis.doesReflectLaserFrom(orientations.North)).to.be.false;
        });

        it('east', function () {
            expect(anubis.doesReflectLaserFrom(orientations.East)).to.be.false;
        });

        it('south', function () {
            expect(anubis.doesReflectLaserFrom(orientations.South)).to.be.false;
        });

        it('west', function () {
            expect(anubis.doesReflectLaserFrom(orientations.West)).to.be.false;
        });
    });

    describe('willBeKilledByLaserFrom', function () {
        var anubis = new Anubis(orientations.North, colors.White);

        it('north', function () {
            expect(anubis.willBeKilledByLaserFrom(orientations.North)).to.be.true;
        });

        it('east', function () {
            expect(anubis.willBeKilledByLaserFrom(orientations.East)).to.be.true;
        });

        it('south', function () {
            expect(anubis.willBeKilledByLaserFrom(orientations.South)).to.be.false;
        });

        it('west', function () {
            expect(anubis.willBeKilledByLaserFrom(orientations.West)).to.be.true;
        });
    });
});