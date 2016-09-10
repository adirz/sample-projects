/*var chai = require('chai'),
    expect = chai.expect,

    Move = require('../../moves/move'),
    ShiftMove = require('../../moves/shift'),
    RotateClockwiseMove = require('../../moves/rotate-clockwise'),
    RotateCounterclockwiseMove = require('../../moves/rotate-counterclockwise');

describe('Move', function () {
    describe('generateMoveByString', function () {
        it('should generate shift move', function () {
            expect(Move.generateMoveByString('1234').equal(new ShiftMove(2, 1, 4, 3))).to.be.true;
        });

        it('should generate clockwise rotate move', function () {
            expect(Move.generateMoveByString('12+1').equal(new RotateClockwiseMove(2, 1))).to.be.true;
        });

        it('should generate counterclockwise rotate move', function () {
            expect(Move.generateMoveByString('10-1').equal(new RotateCounterclockwiseMove(0, 1))).to.be.true;
        });

        it('should throw error', function () {
            expect(Move.generateMoveByString.bind(Move, '12+2')).to.throw(Error);
        });
    });
});*/