var colors = require('./colors'),
    orientations = require('./orientations'),
    rotations = require('./rotations'),
    
    Anubis = require('./figures/anubis'),
    Pharaoh = require('./figures/pharaoh'),
    Sphinx = require('./figures/sphinx'),
    Scarab = require('./figures/scarab'),
    Pyramid = require('./figures/pyramid'),
    
    Board = require('./board');
    
var utils = {
    getCoordinatesOfFigure: function (board, figure) {
        for (var y = 0; y < board.height; ++y) {
            for (var x = 0; x < board.width; ++x) {
                var square = board.getSquare(x, y);
                if (square.hasFigure() && square.getFigure() === figure) {
                    return {
                        x: x,
                        y: y
                    };
                }
            }
        }

        return null;
    },
    serializeBoard: function (board) {
        var figures = [];

        for (var y = 0; y < board.height; ++y) {
            for (var x = 0; x < board.width; ++x) {
                var square = board.getSquare(x, y);
                if (square.hasFigure()) {
                    figures.push(square.getFigure() + ' ' + this.generateCoordinatesNotation(x, y));
                }
            }
        }

        return figures.join(', ');
    },
    serializeBoardForDrawCheck: function (board) {
        var figures = [];

        for (var y = 0; y < board.height; ++y) {
            for (var x = 0; x < board.width; ++x) {
                var square = board.getSquare(x, y);
                if (square.hasFigure()) {
                    var figure = square.getFigure();
                    figures.push(figure + (figure instanceof Pharaoh ? figure.orientation : '') + ' ' + this.generateCoordinatesNotation(x, y));
                }
            }
        }

        return figures.join(', ');
    },
    parseMove: function (move) {
        if (move == null || move.length<4)
            throw Error("Can't parse move. Length less than required. Move: '"+move+'"');

        var parsedMove = {};

        if (move[2] === '-' && move[3] === '1') {
            parsedMove = {
                x: parseInt(move[1]),
                y: parseInt(move[0]),
                rotation: rotations.counterclockwise
            };
        }
        else if (move[2] === '+' && move[3] === '1') {
            parsedMove = {
                x: parseInt(move[1]),
                y: parseInt(move[0]),
                rotation: rotations.clockwise
            };
        }
        else {
            parsedMove = {
                x: parseInt(move[1]),
                y: parseInt(move[0]),
                x2: parseInt(move[3]),
                y2: parseInt(move[2])
            };
        }
        
        if (isNaN(parsedMove.x) || isNaN(parsedMove.y) ||
            (typeof parsedMove.x2 !== 'undefined' && isNaN(parsedMove.x2)) ||
            (typeof parsedMove.y2 !== 'undefined' && isNaN(parsedMove.y2))){
            throw new Error("Can't parse move: '"+move+'"');
        }
        
        return parsedMove;
    },
    isRotateMove: function (move) {
        return move[2] === '-' || move[2] === '+';
    },
    isMoveValid: function (board, color, moveNotation) {

       function isColorAvailableForFigure(square, figure) {
            return square.color === colors.Neutral || square.color === figure.color;
       }

        var move = {};
        try{
            move = this.parseMove(moveNotation);
        } catch (err)
        {
            return false;
        }

        if (board.outOfBounds(move.x, move.y))  return false;

        var source = board.getSquare(move.x, move.y);
        if (!source.hasFigure()) return false;
        
        var figure = source.getFigure();
        if (figure.color !== color) return false;         
        var figureString = figure.toString();  
        
        if (this.isRotateMove(moveNotation)) {
            var rot = figureString+moveNotation[2];  
            if ((rot =='S1+') || (rot == 'S4-') || (rot =='s3+') || (rot == 's2-')) return false;
            return true;
        }
        
        if (board.outOfBounds(move.x2, move.y2)) return false;
        if (figure instanceof Sphinx) return false;
        
        if (Math.abs(move.x - move.x2) > 1 || Math.abs(move.y - move.y2) > 1 || (move.x === move.x2 && move.y === move.y2)) {
           return false;
        }
        
        var destination = board.getSquare(move.x2, move.y2);
        
        if (!isColorAvailableForFigure(destination, figure)) return false;
        
        if (destination.hasFigure()) {
            var figure2 = destination.getFigure();
            return figure instanceof Scarab && (figure2 instanceof Pyramid || figure2 instanceof Anubis) && isColorAvailableForFigure(source, figure2);
        }

        return true;
    },
    generateCoordinatesNotation: function (x, y) {
        return '' + y + x;
    },
    generateRotateMoveNotation: function (x, y, rotation) {
        return this.generateCoordinatesNotation(x, y) + (rotation === rotations.clockwise ? '+1' : '-1');
    },
    generateShiftMoveNotation: function (x1, y1, x2, y2) {
        return this.generateCoordinatesNotation(x1, y1) + this.generateCoordinatesNotation(x2, y2);
    },
    makeMove: function (board, moveNotation) {
        var move = {};
        try{
            move = this.parseMove(moveNotation);
        } catch (err)
        {
            return;
        }

        if (this.isRotateMove(moveNotation)) {
            board.getSquare(move.x, move.y).getFigure().rotate(move.rotation);
        }
        else {
            var source = board.getSquare(move.x, move.y),
                destination = board.getSquare(move.x2, move.y2);

            if (destination.hasFigure()) {
                var figure = source.getFigure();
                source.setFigure(destination.getFigure());
                destination.setFigure(figure);
            }
            else {
                destination.setFigure(source.getFigure());
                source.removeFigure();
            }
        }
    },
    getAvailableMoves: function (board, color) {
        var moves = [];
        var rots = [ rotations.clockwise, rotations.counterclockwise ]; 
        var move; 
        for (var y = 0; y < board.height; ++y) {
            for (var x = 0; x < board.width; ++x) {
                var square = board.getSquare(x, y);
                if (square.hasFigure() && square.getFigure().color === color) {
                    
                    for(var r = rots.length-1; r >= 0 ; r--) {
                      move = this.generateRotateMoveNotation(x, y, rots[r]);
                      if (this.isMoveValid(board, color, move)) 
                          moves.push(move);
                    }
                    
                    
                    for (var dx = -1; dx <= 1; ++dx) {
                        for (var dy = -1; dy <= 1; ++dy) {
                            if (!board.outOfBounds(x + dx, y + dy)) {
                                move = this.generateShiftMoveNotation(x, y, x + dx, y + dy);
                                if (this.isMoveValid(board, color, move)) {
                                    moves.push(move);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return moves;
    },
    
    
    getDeltaX: function (orientation) {
        switch (orientation) {
            case orientations.East:
                return 1;
            case orientations.West:
                return -1;
            default:
                return 0;
        }
    },
    getDeltaY: function (orientation) {
        switch (orientation) {
            case orientations.South:
                return 1;
            case orientations.North:
                return -1;
            default:
                return 0;
        }
    },
    // DRY
    getLaserPathSegments: function (board, x, y, orientation) {
        if (board.outOfBounds(x, y)) {
            return [];
        }

        if (board.getSquare(x, y).hasFigure()) {
            var figure = board.getSquare(x, y).getFigure();

            if (figure.doesReflectLaserFrom(orientation)) {
                var newOrientation = figure.getOrientationOfReflectedLaserFrom(orientation);
                var pathSegmentType;

                if ((orientation === orientations.East && newOrientation === orientations.North) || (orientation === orientations.South && newOrientation === orientations.West)) {
                    pathSegmentType = 'right-up';
                }
                else if ((orientation === orientations.South && newOrientation === orientations.East) || (orientation === orientations.West && newOrientation === orientations.North)) {
                    pathSegmentType = 'down-right';
                }
                else if ((orientation === orientations.East && newOrientation === orientations.South) || (orientation === orientations.North && newOrientation === orientations.West)) {
                    pathSegmentType = 'right-down';
                }
                else if ((orientation === orientations.North && newOrientation === orientations.East) || (orientation === orientations.West && newOrientation === orientations.South)) {
                    pathSegmentType = 'up-right';
                }
                
                return [{
                    x: x,
                    y: y,
                    type: pathSegmentType
                }].concat(this.getLaserPathSegments(board, x + this.getDeltaX(newOrientation), y + this.getDeltaY(newOrientation), newOrientation));
            }
            else {
                return [];
            }
        }
        else {
            return [{
                x: x,
                y: y,
                type: (orientation === orientations.North || orientation === orientations.South) ? 'vertical' : 'horizontal'
            }].concat(this.getLaserPathSegments(board, x + this.getDeltaX(orientation), y + this.getDeltaY(orientation), orientation));
        }
    },
    getSphinxFigure : function(board, color) {
        var  x, y;
        if (color == colors.White) {
           x = 9;
           y = 7;               
        } else {
           x = 0;
           y = 0;               
        }
        var figure = board.getSquare(x, y).getFigure();
        if (figure instanceof Sphinx && figure.color === color) {
            return { x: x, y : y, figure : figure };
        } else {
            return null;
        }
    },
    // DRY
    getLaserPathSegmentsFromSphinx: function (board, color) {
        var sphinx = this.getSphinxFigure(board, color);
        if (sphinx == null) return null;
        var orientation = sphinx.figure.orientation;
        return [sphinx].concat(this.getLaserPathSegments(board, sphinx.x + this.getDeltaX(orientation), sphinx.y + this.getDeltaY(orientation), orientation));
    },
    // DRY
    getFigureToBeKilledByLaser: function (board, x, y, orientation) {
        if (board.outOfBounds(x, y)) {
            return null;
        }

        if (board.getSquare(x, y).hasFigure()) {
            var figure = board.getSquare(x, y).getFigure();

            if (figure.doesReflectLaserFrom(orientation)) {
                var newOrientation = figure.getOrientationOfReflectedLaserFrom(orientation);
                return this.getFigureToBeKilledByLaser(board, x + this.getDeltaX(newOrientation), y + this.getDeltaY(newOrientation), newOrientation);
            }
            else if (figure.willBeKilledByLaserFrom(orientation)) {
                return figure;
            }
            
            return null;
        }
        
        return this.getFigureToBeKilledByLaser(board, x + this.getDeltaX(orientation), y + this.getDeltaY(orientation), orientation);
    },
    getFigureToBeKilledBySphinx: function (board, color) {
        var sphinx = this.getSphinxFigure(board, color);
        if (sphinx == null) return null;
        var orientation = sphinx.figure.orientation;
        return this.getFigureToBeKilledByLaser(board, sphinx.x + this.getDeltaX(orientation), sphinx.y + this.getDeltaY(orientation), orientation);
    },
    removeFigureFromBoard: function (board, figure) {
        for (var y = 0; y < board.height; ++y) {
            for (var x = 0; x < board.width; ++x) {
                var square = board.getSquare(x, y);
                if (square.hasFigure() && square.getFigure() === figure) {
                    square.removeFigure();
                }
            }
        }
    },
    createBoardFromPosition: function (position) {
        var board = new Board();
        position.replace(/\s/g, '').split(',').forEach(function (figureNotation) {
            var x = parseInt(figureNotation[3]),
                y = parseInt(figureNotation[2]),
                orientation = figureNotation[1].toLowerCase() === 'h' ? orientations.North : parseInt(figureNotation[1]),
                type = figureNotation[1].toLowerCase() === 'h' ? 'ph' : figureNotation[0].toLowerCase(),
                color = figureNotation[0] === figureNotation[0].toLowerCase() ? colors.Red : colors.White,
                figureClass;

            switch (type) {
                case 'a':
                    figureClass = Anubis;
                    break;
                case 'ph':
                    figureClass = Pharaoh;
                    break;
                case 'c':
                    figureClass = Scarab;
                    break;
                case 'p':
                    figureClass = Pyramid;
                    break;
                case 's':
                    figureClass = Sphinx;
                    break;
            }

            board.getSquare(x, y).setFigure(new figureClass(orientation, color));
        });
        return board;
    }
};

module.exports = utils;