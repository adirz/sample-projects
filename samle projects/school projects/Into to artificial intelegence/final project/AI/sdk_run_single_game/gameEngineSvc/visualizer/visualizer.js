$.fn.scrollIntoView = function() {
    var parentElement = this.get(0).parentNode;
    var elY = this.get(0).offsetTop - parentElement.offsetTop;
    var elH, parentShift, parentHeight;

    elY === 0 || (elH = elY + this.get(0).offsetHeight);
    elH -= elY;
    parentShift = parentElement.scrollTop;
    parentHeight = parentElement.clientHeight;
    if (elY <= parentShift) {
        parentElement.scrollTop =  elY;
    } else if ((elY + elH) > (parentShift + parentHeight)) {
        parentElement.scrollTop =  elY + elH - parentHeight;
    }
    return this;
};
var socket = io(window.location.origin);
var resumeBtn = $('#resume');
var pauseBtn = $('#pause');
var app = (function() {
    function Visualizer() {
        var BOARD_PARAMS = [8,10];
        var LASER_COORDINATES = [
            '30 30', '90 30', '150 30', '210 30', '270 30', '330 30', '390 30', '450 30', '510 30', '570 30',
            '30 90', '90 90', '150 90', '210 90', '270 90', '330 90', '390 90', '450 90', '510 90', '570 90',
            '30 150', '90 150', '150 150', '210 150', '270 150', '330 150', '390 150', '450 150', '510 150', '570 150',
            '30 210', '90 210', '150 210', '210 210', '270 210', '330 210', '390 210', '450 210', '510 210', '570 210',
            '30 270', '90 270', '150 270', '210 270', '270 270', '330 270', '390 270', '450 270', '510 270', '570 270',
            '30 330', '90 330', '150 330', '210 330', '270 330', '330 330', '390 330', '450 330', '510 330', '570 330',
            '30 390', '90 390', '150 390', '210 390', '270 390', '330 390', '390 390', '450 390', '510 390', '570 390',
            '30 450', '90 450', '150 450', '210 450', '270 450', '330 450', '390 450', '450 450', '510 450', '570 450'
        ];
        var _path = $('path');
        var _logBox = $('#logs');
        var _gameProgress = $('#progress');
        var _end = $('#end');
        var _currentStep = 0;
        var _pieces = [];
        var _turn = false;
        var _cachedState = [];
        var _cachedLog = [];
        var _speed = 3000;
        var _initialState,_movesLog, _action, _timeout, _gameOver;

        var events = this.events = new EventEmitter();

        this.nextStep = function() {
            var step;
            var n = _currentStep;
            if(_movesLog.length) {
                step = _movesLog[n] || null;
                step && events.emit('step', step);
                _currentStep++;
                if(!_movesLog[n + 1]) {
                    return null;
                }
            }
            return step;
        };

        this.logger = function(step) {
            var n = _currentStep + 1;
            var logItem = $('<li></li>');

            $('li.active').removeClass('active');
            _gameProgress.val(n);
            if(_cachedLog[n]) {
                _cachedLog[n].addClass('active');
                $(_cachedLog[n][0]).scrollIntoView();
                return;
            }
            logItem.addClass(function() {
                return ((_turn = !_turn) ? 'silver' : 'red') + ' active';
            });
            logItem.text('move: ' + step.move + ', laser: ' + step.laser + ', ' + (step.killedFigure ? 'kill figure in square: '  + step.killedFigure.y  + step.killedFigure.x : 'miss'));
            logItem.attr('data-stepNum', n);
            _logBox.append(logItem);
            $(logItem[0]).scrollIntoView();
            _cachedLog[n] = logItem;
        };

        this.renderState = function(state) {
            var df = $(document.createDocumentFragment());
            var tbody = $('tbody').html(''), tr, td;

            for(var i = 0, ly = BOARD_PARAMS[0]; i < ly; i++) {
                tr = $('<tr></tr>');
                for(var j = 0, lx = BOARD_PARAMS[1]; j < lx; j++) {
                    td = $('<td class="piece"></td>');
                    tr.append(_pieces[_pieces.length] = td);
                }
                df.append(tr);
            }
            state.forEach(function(p) {
                var position = p.split(' ');
                _pieces[+position[1]].attr('data-p', position[0]);
            });
            tbody.append(df);
        };

        this.renderStep = function(step) {
            var laser = '';
            var parsedStep = step.move.match(/[\d\+-]{2}/g);
            var position = parseFloat(parsedStep[0]);
            var action = parseFloat(parsedStep[1]);
            var figure = _pieces[position];
            var self = this;
            var attr, killedFigure, figure1Type, figure2Type;

            if(/\+1|-1/.test(parsedStep[1])){
                attr = figure.attr('data-p');
                figure.attr('data-p', attr.replace(/([a-zA-Z]{1,2})(\d$)*/, function(match,$1,$2) {//$1 - figure name; $2 - orientation index
                    var n = + $2 + action;
                    switch ($1.toLowerCase()) {
                        case 'ph':
                            return $1;
                        case 'c':
                            return $1 + (n > 2 ? 1 : n <= 0 ? 2 : n);
                        default :
                            return $1 + (n > 4 ? 1 : n <= 0 ? 4 : n);
                    }
                }));
            }else{
                figure1Type = figure.attr('data-p');
                figure2Type = _pieces[action].attr('data-p');
                _pieces[action].attr('data-p', figure1Type);
                figure.attr('data-p', /[c|C]/.test(figure1Type) && figure2Type ? figure2Type : '');
            }
            for(var i = 0; i < step.laser.length; i++) {
                laser += ((i === 0) ? 'M ' : 'L ') + LASER_COORDINATES[+step.laser[i]];
            }
            setTimeout(function() {
                _path.attr('d', laser);
                setTimeout(function() {
                    _path.attr('d', 'M 0 0');
                    if(step.killedFigure) {
                        killedFigure = +(step.killedFigure.y + '' +step.killedFigure.x);
                        _pieces[killedFigure].attr('data-p', '');
                    }
                    self.serializeBoard();
                }, _speed/4);
            }, _speed/2);
        };

        this.serializeBoard = function() {
            var el, res = '';

            for(var i = 0, l = _pieces.length; i < l; i++) {
                el = _pieces[i];
                if(el.attr('data-p')){
                    res += el.attr('data-p') + ' ' + i + (i < l - 1 ? ', ' : '');
                }
            }
            _cachedState.push(res);
        };

        this.autoPlay = function() {
            _action = !!this.nextStep();
            if(_action) {
                _timeout = setTimeout(this.autoPlay.bind(this), _speed);
            }else if(_gameOver) {
                setTimeout(function () {
                    events.emit('end', _gameOver);
                }, _speed);
            }
        };

        this.init = function() {

            events.on('initial_state', function(state) {
                _cachedState.push(state);
                _initialState = state.split(/\s*,\s*/);
                this.renderState(_initialState);
            }, this);

            events.on('step', function(step) {
                this.logger(step);
                this.renderStep(step);
            }, this);

            events.on('pause', function() {
                _action = false;
                clearTimeout(_timeout);
            });

            events.on('change_progress', function(stepNumber) {
                _pieces = [];
                _currentStep = Number(stepNumber);
                $('li.active').removeClass('active');
                _cachedLog[_currentStep] && _cachedLog[_currentStep].addClass('active');
                this.renderState(_cachedState[_currentStep].split(/\s*,\s*/));
            }, this);

            events.on('resume', function() {
                !_action && this.autoPlay();
            }, this);

            events.on('update_moves_log', function(log) {
                _gameProgress.attr('max', log.length);
                _end.text(log.length);
                _movesLog = log;
                if(!_action) {
                    _action = true;
                    setTimeout(this.autoPlay.bind(this), 2000);
                }
            }, this);

            events.on('game_over', function(end) {
                _gameOver = end;
            });

            events.on('change_speed', function(speed) {
                _speed = speed;
            });

            events.once('end', function() {
                _cachedLog.forEach(function(el) {
                    el.click(function() {
                        var stepNum = $(this).attr('data-stepNum');
                        app.events.emit('pause');
                        _gameProgress.val(stepNum);
                        events.emit('change_progress', stepNum);
                    });
                });
            });

            return this;
        };
    }

    function EventEmitter() {
        var _eventsContainer = {};

        this.on = function(event, cb, ctx) {
            if(arguments.length < 2) return;
            cb.ctx = ctx || null;
            if(event in _eventsContainer) {
                return _eventsContainer[event].push(cb);
            }else{
                return _eventsContainer[event] = [cb];
            }
        };

        this.once = function(event, cb, ctx) {
            if(arguments.length < 2) return;
            cb.once = true;
            this.on.apply(this, arguments);
        };

        this.emit = function(/*event, arg1, arg2, ...*/) {
            var event = arguments[0];
            var args = [].slice.call(arguments, 1);

            if(event in _eventsContainer) {
                _eventsContainer[event] = _eventsContainer[event].filter(function(cb) {
                    cb.apply(cb.ctx, args);
                    return !cb.once;
                });
            }else{
                console.error('Callback list for ' + event + ' is empty.');
            }
        };
    }

    return (new Visualizer()).init();
})();

socket.on('new_game', function(initState) {
    if(initState) {
        initState.bots.forEach(function (bot) {
            $('#legend a').eq(bot.color === 1 ? 0 : 1).text(bot.userId === 1 ? 'Your bot' : 'Default bot');
        });
        app.events.emit('initial_state', initState.boardNotation);
    }else{
        window.location = '/';
    }
});
socket.on('move', function(moves) {
    app.events.emit('update_moves_log', moves);
});
socket.on('game_over', function(gameOver) {
    app.events.emit('game_over', gameOver);
});

app.events.on('end', function(_gameOver) {
    var p1 = _gameOver.player1;
    var p2 = _gameOver.player2;
    var msg = $('#msg');
    var msgText;
    if(p1.score === 1) {
        msgText = (p1.color === 1 ? 'White' : 'Red') + ' WIN!';
    }else if(p2.score === 1) {
        msgText = (p2.color === 1 ? 'White' : 'Red') + ' WIN!';
    }else if(p1.score === 0.5) {
        msgText = 'dead heat';
    }else{
        msgText = 'All died :\'(';
    }
    msg.text(msgText);
    $(document).one('mousedown', function() {
        msg.text('');
    });
    $('#progress').attr('disabled', false);
    resumeBtn.attr('disabled', true);
    pauseBtn.attr('disabled', true);
});

app.events.on('pause', function() {
    resumeBtn.attr('disabled', false);
    pauseBtn.attr('disabled', true);
});

app.events.on('resume', function() {
    pauseBtn.attr('disabled', false);
});

resumeBtn.click(function() {
    app.events.emit('resume');
    $(this).attr('disabled', true);
});

pauseBtn.click(function() {
    app.events.emit('pause');
    $(this).attr('disabled', true);
});

$('#progress')
    .on('mousedown', function() {
        app.events.emit('pause');
    })
    .on('input', function() {
        app.events.emit('change_progress', $(this).val());
    });

$('[type="radio"]').click(function() {
    app.events.emit('change_speed', $(this).val());
});