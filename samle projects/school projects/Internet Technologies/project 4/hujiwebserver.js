var hujinet = require('./hujinet');
var serverObj;

function start(port, callback){
	try {
		serverObj = hujinet.connection();
	}catch (err) {
		callback(err);
	}
	serverObj.listen(port);
	var p1 = port;
	var p2 = "www/";
	Object.defineProperty(serverObj, "port", {
		get: function(){return p1;},
		set: function(newP){p1 = newP;},
		enumerable: true,
		configurable: true
	});
	Object.defineProperty(serverObj, "rootFolder", {
		get: function(){return p2;},
		set: function(newP){p2 = newP;},
		enumerable: true,
		configurable: true
	});
	
	serverObj.myUse = function (rq, rs, nxt){
		rs.set({'Date': new Date(), 'Host': rq.host, 'Connection': 'Keep-Alive'});
		if (null != nxt) {
			nxt(rq, rs, null);
		}
	};
	serverObj.static = stat;
	serverObj.stop = stop;
	return serverObj;
};

function stat(rootFolder){
	return function (rq, rs, nxt) {
		var fs = require('fs');
		s.set({'Date': new Date(), 'Host': rq.host, 'Connection': 'Keep-Alive'});
		if(rq.path.indexOf('..') > -1){
			rs.status(403);
			rs.send();
		}else if (!fs.existsSync(fileLoc)) {
			rs.status(404);
			rs.send();
		}else{
			var fileAsAstream = fs.createReadStream(rq.path);
			var fileType = path.extname(fileLoc);
			if(!(fileType in types)){
				rs.status(500);
				rs.send();
			}else{
				fs.stat(fileLoc, function(error, stats){
					if(error){
						rs.status(500);
						rs.send();
					}else if(res == 200){
						rs.set('Content-Length', String(stats.size))
						rs.status(200);
						rs.send();
						fileAsAstream.pipe(socket, {end: false});
					}
				});
			}
		}
		if (null != nxt) {
			nxt(rq, rs, null);
		}
	};
};

function stop(callback){
	serverObj.close();
	if (callback != null) {
		callback();
	}
};

module.exports.stop = stop;
module.exports.static = stat;
module.exports.start = start;