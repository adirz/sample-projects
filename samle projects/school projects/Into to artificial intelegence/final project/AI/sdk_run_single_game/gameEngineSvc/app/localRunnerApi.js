var express = require('express');
var game = require('./controllers/game');
var router = express.Router();
var bodyParser = require('body-parser');

module.exports = router;

router.post('/game/start', bodyParser.json(), game.init);
router.post('/bot/ready', bodyParser.json(), game.connectionBot);