/*var chai = require('chai'),
    expect = chai.expect,

    utils = require('../../utils'),
    colors = require('../../colors'),

    RotateMove = require('../../moves/rotate'),
    helper = require('../helper');

describe('RotateMove', function () {
    var board = helper.createStandardBoard();

    describe('isValid', function () {
        it('should be able to rotate own figures', function () {
            expect(new RotateMove(0, 0).isValid(board, colors.Red)).to.be.true;
        });

        it('should not be able to rotate opponent\'s figures', function () {
            expect(new RotateMove(0, 0).isValid(board, colors.White)).to.be.false;
        });

        it('should not be able to rotate nonexistent figure', function () {
            expect(new RotateMove(9, 0).isValid(board, colors.White)).to.be.false;
        });

        it('out of bounds case', function () {
            expect(new RotateMove(10, 0).isValid(board, colors.White)).to.be.false;
        });
    });
});*/