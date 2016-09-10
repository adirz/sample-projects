var chai = require('chai'),
    expect = chai.expect,
    colors = require('../../sdk/colors'),

    Anubis = require('.././anubis'),
    Square = require('../../sdk/square');

describe('square', function () {
    describe('hasFigure', function () {
        it('should hot have figure initially', function () {
            var square = new Square(0, 0, colors.Neutral);
            expect(square.hasFigure()).to.be.false;
        });

        it('should have figure if set it', function () {
            var square = new Square(0, 0, colors.Neutral);
            square.setFigure(new Anubis());
            expect(square.hasFigure()).to.be.true;
        });
    });

    describe('getFigure', function () {
        it('should return null initially', function () {
            var square = new Square(0, 0, colors.Neutral);
            expect(square.getFigure()).to.be.null;
        });

        it('should return the same figure as was set', function () {
            var square = new Square(0, 0, colors.Neutral),
                figure = new Anubis();
            square.setFigure(figure);
            expect(square.getFigure()).to.be.equal(figure);
        });
    });

    describe('removeFigure', function () {
        it('getFigure should return null after removeFigure was called', function () {
            var square = new Square(0, 0, colors.Neutral);
            square.setFigure(new Anubis());
            square.removeFigure();
            expect(square.getFigure()).to.be.null;
        });
    });
});