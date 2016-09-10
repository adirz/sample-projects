var types = new Object();
types[".js"] = "application/javascript";
types[".tx"] = "text/plain";
types[".html"] =  "text/html";
types[".css"] =  "text/css";
types[".jpg"] =  "image/jpeg";
types[".gif"] =  "image/gif";

var statusCode = [];
statusCode[200] = ' OK';
statusCode[400] = ' Bad request';
statusCode[403] = ' Forbidden';
statusCode[404] = ' Not Found';
statusCode[500] = ' Internal Server Error';

function findMatch(path, frm, handlers){
	var parser = require('./hujirequestparser');
	var param = [];
	for(var key = frm; key < handlers.length; key ++){
		var handler = handlers[key];
		var parms = parser.resourceIsMatch(path, handler[0]);
		if (parms){
			return [[handler[1], parms], key];
		}
	}
	return null;
}

var connection = function(){
	var net = require('net');
	var path = require('path');
	var parser = require('./hujirequestparser');
	var handlers = [];//baaaaaaaaaa ToDo

	var serverObj = net.createServer(function(socket) {
		parser.init(socket);
		socket.on('data', function(data){
			socket.setTimeout(2000);
			var res = parser.parse(data.toString(), socket);
			while (parser.msgs[socket].length > 0) {
				var msgs = parser.msgs[socket][0];
				var sent = false;
				var resource = parser.msgs[socket][0]["resource"];
				var que = parser.querySplit(resource);
				if (resource.indexOf('?') >= 0) {
					resource = resource.slice(0, resource.indexOf('?'));
				}
				var fileLoc = path.normalize(resource);
				var hos = msgs["host"];
				var http = msgs["HTTP"];
				var con = msgs["connection"];
				var contType = msgs["content-type"];
				var meth = msgs["method"];

				parser.msgs[socket].splice(0, 1);
				var rq = {
					params: {},
					query: parser.dictRequestSplit(que, "&"),
					method: meth,
					cookies: parser.dictRequestSplit(msgs["cookie"], "; "),
					path: fileLoc,
					host: hos,
					protocol: "http",//ToDo ask if ok
					get: function(s){	return msgs[s.toLowerCase()];},
					param: function(s){	return this.params[s];},
					is: function(s){	return parser.typeIsMatch(s, msgs["content-type"]);},
					body: msgs["body"]
				};
				var params = que.split('&');
				for (var p in params) {
					var key = params[p].split('=');
					var val = key[1];
					key = key[0];
					rq.params[key] = val;
				}

				var rs = {
					parameters: [],
					cookies: [],
					sttsCode: null,
					set: function(arg1, arg2){
						if (arguments.length === 1) {
							if(typeof arg1 === "string"){
								for(var i = 0; i < arg1.length; i ++){
									var ob = arg1[i].split(': ');
									this.parameters[ob[0].toLowerCase()] = ob[1];
								}
							}else{
								for(var key in arg1){
									this.parameters[key.toLowerCase()] = arg1[key];
								}
							}
						}else if (arguments.length === 2){
							this.parameters[arg1.toLowerCase()] = arg2;
						}
					},
					status: function(st){
						//ToDo chainable alias of Node's response.statusCode
						this.sttsCode = st;
						return this;
					},
					get: function(param){
						return this.parameters[param.toLowerCase()];
					},
					cookie: function(name, value, options){//ToDo check if exists already
						var val = value;
						if ("string" === typeof value){
							val = JSON.stringify(value);
						}
						var cook = 'Set-Cookie: '+ name + '=' + val;
						for (var property in options) {
							cook += '; ' + property + '=' + options[property];
						}
						cookies.push(cook);
					},
					send: function(bodyData){
						if(this.sttsCode === 404 && typeof bodyData === "undefined"){
							bodyData = 'The requested resource not found';
						}
						var header = 'HTTP/1.1 ' + String(this.sttsCode) + statusCode[this.sttsCode] + '\r\n';
						sent = true;
						for (var param in this.parameters) {
							header += param + ': ' + this.parameters[param] + '\r\n';
						}
						if(typeof contType === "string"){
							header += 'Content-Type: ' + contType + '\r\n';
						}
						for (var coo in this.cookies) {
							header += coo + ': ' + this.cookies[coo] + '\r\n';
						}
						if("undefined" != typeof bodyData){
							if ("string" === typeof bodyData) {
								header += 'Content-Length: ' + bodyData.length + '\r\n\r\n';
								socket.write(header);
								socket.write(bodyData);
							}else if("object" === typeof bodyData) {
								this.json(bodyData);
							}
						}else{
							header += 'Content-Length: 0\r\n\r\n';
							socket.write(header);
						}
					},
					json: function(ob){
						if("string" === typeof ob) {
							this.send(ob);
						}else{
							this.send(JSON.stringify(ob));
						}
					}
				};
				try {
					var matchO = findMatch(fileLoc, 0, handlers);
					var placeO = 0;
					var funcO;
					if (null != matchO) {
						placeO = matchO[1];
						var funcAndParamsO = matchO[0];
						funcO = funcAndParamsO[0];
						var paramsO = funcAndParamsO[1];
						for (var key in paramsO) {
							rq.params[key] = paramsO[key];
						}
						var place = placeO;
						var	func = null;
						var	params= null;
						var	match =null;
						var next = function(){
							match = findMatch(fileLoc, place + 1, handlers);
							if(null != match){
								place = match[1];
								params = match[0][1];
								func = match[0][0];
								for (var key in params) {
									rq.params[key] = params[key];
								}
								func(rq, rs, next);
							}
						}
						funcO(rq, rs, next);
					}
					if(!sent){
						rs.status(404);
						rs.send();
					}
				}catch (e) {
					console.log(e);
					rs.status(500);
					rs.send();
				}
			}
		});
	});
	serverObj.use = function (resource, requestHandler){
		if(arguments.length === 1) {
	    	requestHandler = resource;
			resource = "/";
		}
		handlers.push([resource, requestHandler]);
	};

	return serverObj;
};

module.exports.connection = connection;
