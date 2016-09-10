var winston = require('winston');

var logger = new winston.Logger({
    transports: [
        new winston.transports.Console({
            level: 'debug',
            handleExceptions: true,
            json: false,
            colorize: true,
            timestamp: false
        })
    ],
    exitOnError: true
});

module.exports = logger;
