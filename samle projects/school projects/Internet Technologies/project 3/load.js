var hujiserver = require('./hujiwebserver');
hujiserver.start(9000, 'ex2/', function(e){
	e?(console.log(e)):(Console.log('server is up. port 9000'));
});


var http = require('http');

var options = {
	host: "127.0.0.1",
	port: 9000,
	keepAlive: true,
	path: "/index.html"
};

callback = function (response) {
	console.log(response.statusCode);
}
for (var i = 0; i < 5; i ++) {
	http.get(options, callback);
}

setTimeout(function(){hujiserver.stop(function(){});}, 30000);