/*var chai = require('chai'),
    expect = chai.expect,

    utils = require('../../utils'),
    Board = require('../../board'),
    colors = require('../../colors'),
    orientations = require('../../orientations'),

    Anubis = require('../../figures/anubis'),
    Pharaoh = require('../../figures/pharaoh'),
    Sphinx = require('../../figures/sphinx'),
    Scarab = require('../../figures/scarab'),
    Pyramid = require('../../figures/pyramid'),

    ShiftMove = require('../../moves/shift'),
    helper = require('../helper');

describe('ShiftMove', function () {
    describe('isValid', function () {
        var board = helper.createStandardBoard();

        it('some legal moves', function () {
            expect(new ShiftMove(5, 0, 5, 1).isValid(board, colors.Red)).to.be.true;
        });

        describe('some illegal moves', function () {
            it('not neighbour destination square', function () {
                expect(new ShiftMove(0, 0, 9, 9).isValid(board, colors.Red)).to.be.false;
            });

            it('wrong square color', function () {
                expect(new ShiftMove(0, 0, 1, 0).isValid(board, colors.Red)).to.be.false;
            });

            it('red figure to white square', function () {
                expect(new ShiftMove(2, 1, 1, 0).isValid(board, colors.Red)).to.be.false;
            });

            it('white figure to red square', function () {
                expect(new ShiftMove(7, 6, 8, 7).isValid(board, colors.White)).to.be.false;
            });
        });

        describe('scarab swap', function () {
            it('swap with anubis', function () {
                var board = new Board(2, 2),
                    scarab = new Scarab(orientations.North, colors.White),
                    anubis = new Anubis(orientations.North, colors.White);
                board.getSquare(scarab.x, scarab.y).setFigure(scarab);
                board.getSquare(anubis.x, anubis.y).setFigure(anubis);

                expect(new ShiftMove(0, 0, 1, 1).isValid(board, colors.White)).to.be.true;
            });

            it('swap with pyramid', function () {
                var board = new Board(2, 2),
                    scarab = new Scarab(orientations.North, colors.White),
                    pyramid = new Pyramid(orientations.North, colors.White);
                board.getSquare(scarab.x, scarab.y).setFigure(scarab);
                board.getSquare(pyramid.x, pyramid.y).setFigure(pyramid);

                expect(new ShiftMove(0, 0, 1, 1).isValid(board, colors.White)).to.be.true;
            });

            it('should not swap with scarab because of squares color', function () {
                var board = new Board(2, 2),
                    scarab = new Scarab(orientations.North, colors.White),
                    scarab2 = new Scarab(orientations.North, colors.White);
                board.getSquare(scarab.x, scarab.y).setFigure(scarab);
                board.getSquare(scarab2.x, scarab2.y).setFigure(scarab2);

                expect(new ShiftMove(0, 0, 1, 1).isValid(board, colors.White)).to.be.false;
            });

            it('swap with pharaoh', function () {
                var board = new Board(2, 2),
                    scarab = new Scarab(orientations.North, colors.White),
                    pharaoh = new Pharaoh(orientations.North, colors.White);
                board.getSquare(scarab.x, scarab.y).setFigure(scarab);
                board.getSquare(pharaoh.x, pharaoh.y).setFigure(pharaoh);

                expect(new ShiftMove(0, 0, 1, 1).isValid(board, colors.White)).to.be.false;
            });

            it('swap with sphinx', function () {
                var board = new Board(2, 2),
                    scarab = new Scarab(orientations.North, colors.White),
                    sphinx = new Sphinx(orientations.North, colors.White);
                board.getSquare(scarab.x, scarab.y).setFigure(scarab);
                board.getSquare(sphinx.x, sphinx.y).setFigure(sphinx);

                expect(new ShiftMove(0, 0, 1, 1).isValid(board, colors.White)).to.be.false;
            });
        });
    });
});*/