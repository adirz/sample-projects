var express = require('express');
var bodyParser = require('body-parser');
var path = require('path');

module.exports = function(io) {
    var app = express();

    app.use(express.static(path.join(__dirname, 'visualizer')));
    app.use(bodyParser.text());
    app.get('/', function(req, res) {
        res.sendFile('index.html', {root:__dirname + '/visualizer/'})
    });
    require('./app/visualizerSocket')(io);
    return app;
};