var msgs = new Object();
var types = ["application/javascript", "text/plain", "text/html", "text/css", "image/jpeg", "image/gif"];
var headers = new Object();

function parseChunck(toParse, socket){
	var msg = new Object();
	var res = 200;
	var firstLine = toParse.slice(0, toParse.indexOf('\r\n'));
	while('' != toParse && toParse.indexOf(/((GET |POST |SEND )\/(.*) HTTP\/(1.1|1.0))/) != -1){
		toParse = toParse.slice(toParse.indexOf('\r\n') + 2, toParse.length -1);
		firstLine = toParse.slice(0, toParse.indexOf('\r\n'));
	}
	if(firstLine == null){
		msgs[socket].push(msg);
		return 500;
	}
	
	var method = firstLine.slice(0, firstLine.indexOf(' '));
	msg["method"] = method;
	if(method != 'GET'){
		res = 500;
	}
	
	var resource = firstLine.slice(firstLine.indexOf('\/') + 1, firstLine.indexOf('HTTP') - 1);
	if (resource == null) {
		resource = '';
	}
	msg["resource"] = resource;
	
	var httpType = firstLine.slice(firstLine.indexOf('HTTP/') + 5, firstLine.indexOf('HTTP/') + 8);
	if (httpType != null) {
		msg["HTTP"] = httpType;
	}else {
		res = 500;
	}
	
	var lines = toParse.split("\r\n");
	for (var i = 0; i < lines.length; i++) {
		var line = lines[i];
		var field = (line.slice(0, line.indexOf(': '))).toLowerCase();
		msg[field] = line.slice(line.indexOf(': ') + 2, line.length);
	}
	
	var contType = msg["content-type"];
	if(contType == null || types.indexOf(contType) > -1){}
	
	var conLen = msg["content-length"];
	if (conLen == null) {
		msg["content-length"] = 0;
	}
	
	var con = msg["connection"];
	if (con != null) {
		if ((con != "keep-alive" && httpType == '1.0') || con == "close" ) {
			return -1;
		}
	}else {
		res =  500;
	}
	
	msgs[socket].push(msg);
	return res;
}

var parse = function (toParse, socket){
	try {
		var res = [];
		do{
			headers[socket] = headers[socket].concat(toParse.slice(0, toParse.indexOf('\r\n\r\n')));//we only receive headers
			toParse = toParse.slice(toParse.indexOf('\r\n\r\n') + 4, toParse.length -1);
			res.push(parseChunck(headers[socket], socket));
			if(res[res.length -1] != -1){
				headers[socket] = '';
				if(500 != res){
					var conLen = msgs[socket][msgs[socket].length - 1]["content-length"];
					var bdy = toParse.slice(0, conLen);
					toParse = toParse.slice(conLen, toParse.length -1);
				}
			}
		} while (toParse.indexOf('\r\n\r\n') > -1);
		headers[socket] = toParse;
		return res[0];
	}
	catch (e) {
		console.log(e);
		return 500;
	}
};

var init = function(socket){
	msgs[socket] = [];
	headers[socket] = '';
};

module.exports.msgs = msgs;
module.exports.init = init;
module.exports.parse = parse;
