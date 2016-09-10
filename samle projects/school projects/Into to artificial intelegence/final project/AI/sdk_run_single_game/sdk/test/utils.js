var chai = require('chai'),
    expect = chai.expect,

    utils = require('../sdk/utils'),
    Board = require('../sdk/board'),
    colors = require('../sdk/colors'),
    orientations = require('../sdk/orientations'),

    Anubis = require('./anubis'),
    Pharaoh = require('./pharaoh'),
    Sphinx = require('./sphinx'),
    Scarab = require('./scarab'),
    Pyramid = require('./pyramid'),

    helper = require('./helper');

describe('utils', function () {
    describe('getAvailableMoves', function () {
        it('consider color of squares and other figures', function () {
            var board = new Board();
            board.getSquare(1, 1).setFigure(new Pyramid(orientations.North, colors.White));
            board.getSquare(2, 1).setFigure(new Pyramid(orientations.North, colors.Red));

            var actualMoves = utils.getAvailableMoves(board, colors.White);
            var expectedMoves = ['11+1', '11-1', '1101', '1102', '1121', '1122'];

            expect(actualMoves).to.have.length(expectedMoves.length);
            expect(actualMoves).to.have.members(expectedMoves);
        });
    });

    describe('getFigureToBeKilledBySphinx', function () {
        it('should return null', function () {
            var board = new Board();
            /*
             >.\
             ...
             \./
             */
            board.getSquare(0, 0).setFigure(new Sphinx(orientations.East, colors.White));
            board.getSquare(2, 0).setFigure(new Pyramid(orientations.West, colors.White));
            board.getSquare(2, 2).setFigure(new Scarab(orientations.North, colors.Red));
            board.getSquare(0, 2).setFigure(new Pyramid(orientations.East, colors.Red));

            expect(utils.getFigureToBeKilledBySphinx(board, colors.White)).to.be.null;
        });

        it('should return the first pyramid', function () {
            var board = new Board();
            /*
             >pp
             */
            board.getSquare(0, 0).setFigure(new Sphinx(orientations.East, colors.White));
            board.getSquare(1, 0).setFigure(new Pyramid(orientations.East, colors.White));
            board.getSquare(2, 0).setFigure(new Pyramid(orientations.East, colors.Red));

            expect(utils.getFigureToBeKilledBySphinx(board, colors.White)).to.be.equal(board.getSquare(1, 0).getFigure());
        });
    });

    describe('serializeBoard', function () {
        it('standard position', function () {
            var board = helper.createStandardBoard();

            expect(utils.serializeBoard(board).replace(/\s/g, '').split(',')).to.have.length(helper.getStandardPosition().replace(/\s/g, '').split(',').length);
            expect(utils.serializeBoard(board).replace(/\s/g, '').split(',')).to.have.members(helper.getStandardPosition().replace(/\s/g, '').split(','));
        });
    });

    describe('createBoardFromPosition', function () {
        it('standard board', function () {
            var board = helper.createStandardBoard();

            function checkFigureAt (x, y, type, orientation, color) {
                var figure = board.getSquare(x, y).getFigure();
                expect(figure).to.be.instanceOf(type);
                expect(figure.orientation).to.be.equal(orientation);
                expect(figure.color).to.be.equal(color);
            }

            checkFigureAt(4, 7, Pharaoh, orientations.North, colors.White);
            checkFigureAt(9, 7, Sphinx, orientations.North, colors.White);
            checkFigureAt(4, 4, Scarab, orientations.North, colors.White);
            checkFigureAt(5, 4, Scarab, orientations.East, colors.White);
            checkFigureAt(3, 7, Anubis, orientations.North, colors.White);
            checkFigureAt(5, 7, Anubis, orientations.North, colors.White);
            checkFigureAt(3, 2, Pyramid, orientations.North, colors.White);
            checkFigureAt(9, 3, Pyramid, orientations.North, colors.White);
            checkFigureAt(2, 4, Pyramid, orientations.North, colors.White);
            checkFigureAt(2, 7, Pyramid, orientations.North, colors.White);
            checkFigureAt(7, 6, Pyramid, orientations.East, colors.White);
            checkFigureAt(2, 3, Pyramid, orientations.West, colors.White);
            checkFigureAt(9, 4, Pyramid, orientations.West, colors.White);
            checkFigureAt(5, 0, Pharaoh, orientations.North, colors.Red);
            checkFigureAt(0, 0, Sphinx, orientations.South, colors.Red);
            checkFigureAt(5, 3, Scarab, orientations.North, colors.Red);
            checkFigureAt(4, 3, Scarab, orientations.East, colors.Red);
            checkFigureAt(4, 0, Anubis, orientations.South, colors.Red);
            checkFigureAt(6, 0, Anubis, orientations.South, colors.Red);
            checkFigureAt(0, 3, Pyramid, orientations.East, colors.Red);
            checkFigureAt(7, 4, Pyramid, orientations.East, colors.Red);
            checkFigureAt(7, 0, Pyramid, orientations.South, colors.Red);
            checkFigureAt(7, 3, Pyramid, orientations.South, colors.Red);
            checkFigureAt(0, 4, Pyramid, orientations.South, colors.Red);
            checkFigureAt(6, 5, Pyramid, orientations.South, colors.Red);
            checkFigureAt(2, 1, Pyramid, orientations.West, colors.Red);
        });
    });

    describe('getCoordinatesOfFigure', function () {
        it('should return (0, 0)', function () {
            var board = helper.createStandardBoard();
            var coordinates = utils.getCoordinatesOfFigure(board, board.getSquare(0, 0).getFigure());

            expect(coordinates.x).to.be.equal(0);
            expect(coordinates.y).to.be.equal(0);
        });

        it('should return null', function () {
            var board = helper.createStandardBoard();
            var coordinates = utils.getCoordinatesOfFigure(board, board.getSquare(1, 0).getFigure());

            expect(coordinates).to.be.null;
        });
    });

    describe('getLaserPathSegmentsFromSphinx', function () {
        it('standard position, red sphinx', function () {
            var actualSegments = utils.getLaserPathSegmentsFromSphinx(helper.createStandardBoard(), colors.Red);
            var expectedSegments = [
                { x: 0, y: 1, type: 'vertical' },
                { x: 0, y: 2, type: 'vertical' },
                { x: 0, y: 3, type: 'down-right' },
                { x: 1, y: 3, type: 'horizontal' },
                { x: 2, y: 3, type: 'right-down' },
                { x: 2, y: 4, type: 'right-up' },
                { x: 1, y: 4, type: 'horizontal' },
                { x: 0, y: 4, type: 'up-right' },
                { x: 0, y: 5, type: 'vertical' },
                { x: 0, y: 6, type: 'vertical' },
                { x: 0, y: 7, type: 'vertical' }
            ];

            expect(actualSegments).to.have.length(expectedSegments.length);
            expect(actualSegments).to.deep.have.members(expectedSegments);
        });
    });
});