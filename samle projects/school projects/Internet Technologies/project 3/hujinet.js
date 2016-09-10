var types = new Object();
types[".js"] = "application/javascript";
types[".tx"] = "text/plain";
types[".html"] =  "text/html";
types[".css"] =  "text/css";
types[".jpg"] =  "image/jpeg";
types[".gif"] =  "image/gif";

function headerBuild(http, res, ans){
	return 'HTTP/' + http + ' ' + String(res) + ans +'\r\n';
};

var connection = function(rootFolder){
	var net = require('net');
	var fs = require('fs');
	var path = require('path');
	var parser = require('./hujirequestparser');
	
	var serverObj = net.createServer(function(socket) {
		parser.init(socket);
		socket.on('data', function(data){
			console.log('request:\n' + data);
			socket.setTimeout(2000);
			var res = parser.parse(data.toString(), socket);
			if (res == -1) {
				socket.end();
			}else {
				while (parser.msgs[socket].length > 0) {
					var fileLoc = rootFolder + parser.msgs[socket][0]["resource"];
					var http = parser.msgs[socket][0]["HTTP"];
					var con = parser.msgs[socket][0]["connection"];
					var hos = parser.msgs[socket][0]["host"];
					parser.msgs[socket].splice(0, 1);
					var mEnd = 'Date: '+ new Date() + '\r\n' +'Host: ' + hos + '\r\n' + 'Connection: '+ con +'\r\n\r\n';			
					
					var header;
					var ans = ' OK';
					if(fileLoc.indexOf('..') > -1){
						res = 403;
						ans = ' unauthorized location';
						header = headerBuild(http, res, ans);
						header += 'Content-Length: 0\r\n';
						header += mEnd;
						socket.write(header);
						return;
					}
					if (!fs.existsSync(fileLoc)) {
						res = 404;
						ans = ' page not found';
						header = headerBuild(http, res, ans);
						header += 'Content-Length: 0\r\n';
						header += mEnd;
						socket.write(header);
						return;
					}
					var fileAsAstream = fs.createReadStream(fileLoc);
					var fileType = path.extname(fileLoc);
					if(!(fileType in types)){
						res = 500;
						ans = ' file type requested not supported';
						header = headerBuild(http, res, ans);
						header += 'Content-Length: 0\r\n';
						header += mEnd;
						socket.write(header);
						return;
					}
					if (res == 500) {
						ans = ' internal error something something';
						header = headerBuild(http, res, ans);
						header += 'Content-Length: 0\r\n';
						header += mEnd;
						socket.write(header);
						return;
					}
					fs.stat(fileLoc, function(error, stats){
						if(error){
							res = 500;
							ans = ' internal error something something';
							header = headerBuild(http, res, ans);
							header += 'Content-Length: 0\r\n';
							header += mEnd;
							socket.write(header);
							return;
						}
						header = headerBuild(http, res, ans);
						if(res == 200){
							header += 'Content-Length: '+ String(stats.size) +'\r\n';
							header += mEnd;
							socket.write(header);
							fileAsAstream.pipe(socket, {end: false});
						}
					});
				}
			}
		});	
	});
	return serverObj;
};

module.exports.connection = connection;
