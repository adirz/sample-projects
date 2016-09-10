var hujinet = require('./hujinet');
var serverObj;

function start(port,rootFolder,callback){
	try {
		serverObj = hujinet.connection(rootFolder);
	}catch (err) {
		calback(err);
	}
	serverObj.listen(port);
	var p1 = port;
	var p2 = rootFolder;
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
	
	return serverObj;
};

function stop(callback){
	serverObj.close();
	callback();
};

module.exports.stop = stop;
module.exports.start = start;