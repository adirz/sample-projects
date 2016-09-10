var hujiserver = require('./hujiwebserver');
hujiserver.start(9000, 'ex2/', function(e){
	e?(console.log(e)):(Console.log('server is up. port 9000'));
});

var fs = require('fs');
var paths = ["/index.html", "/../shit.tx", "/indix.html"];

var fileS = fs.readFileSync('ex2/index.html').toString();

var http = require('http');
function tests(bod, statusCose){
	console.log(bod.toString());
	var isDiff = ( bod.toString() != fileS.toString() );
	console.log('code: ' + String(statusCose));
	console.log('body diff: ' + String(isDiff));
	fileS = '';
}
callback = function (response) {
	response.on('data', function(chunk){
		tests(chunk, response.statusCode);
	});
}
for (var i = 0; i < 3; i++) {
	options = {
		host: "127.0.0.1",
		port: 9000,
		keepAlive: true,
		path: paths[i]
	};
	http.get(options, callback);
}

setTimeout(function(){hujiserver.stop(function(){});}, 30000);