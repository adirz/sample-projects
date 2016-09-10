var chai = require('chai'),
    expect = chai.expect,

    utils = require('../sdk/utils'),
    orientations = require('../sdk/orientations'),
    Board = require('../sdk/board'),
    colors = require('../sdk/colors'),
    helper = require('./helper');

describe('board', function () {
    describe('outOfBounds', function () {
        var board = helper.createStandardBoard();
        expect(board.outOfBounds(-1, 0)).to.be.true;
        expect(board.outOfBounds(1, -10)).to.be.true;
        expect(board.outOfBounds(10, 10)).to.be.true;

        expect(board.outOfBounds(5, 5)).to.be.false;
        expect(board.outOfBounds(0, 0)).to.be.false;
        expect(board.outOfBounds(9, 7)).to.be.false;
    });
});