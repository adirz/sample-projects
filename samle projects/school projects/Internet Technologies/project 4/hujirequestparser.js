var msgs = new Object();
var types = ["application/javascript", "text/plain", "text/html", "text/css", "image/jpeg", "image/gif"];
var headers = new Object();

function resourceIsMatch(res, key) {
	if(key === "/"){
		return {};
	}
	var r = res.split('/');
	var k = key.split('/');
	var param = {};
	if (r.length < k.length) {
		return false;
	}
	for (i = 1; i < k.length ; i ++) {
		if (k[i][0] != ':' && k[i] != "*") {
			if (k[i] != r[i]) {
				return false;
			}
		}else if(k[i] != "*"){
			param[k[i].substr(1)] = r[i];
			for (j = i; j < k.length ; j ++) {
				if (k[j] === k[i]) {
					k[j] = r[i];
				}
			}
		}else{//ToDo is it legal?
			return param;
		}
	}
	return param;
}

function typeIsMatch(typ, key) {
	var k = key.split("/");
	return typ === key || typ === k[0] +"/*" || typ === k[1];
}

function dictRequestSplit(line, spliter){
	var dict = new Object();
	if(typeof line != "undefined"){
		var objs = line.split(spliter);
		for(var obj in objs){
			obj = objs[obj].split("=");
			dict[obj[0]] = obj[1];
		}
	}
	return dict;
}

function querySplit(resource){
	var que = '';
	if(resource .indexOf('?') > -1){
		que = resource.substring(resource.indexOf('?') + 1);
		que = que.replace("\+", " ");
	}
	return que;
}

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
	}
	
	var resource = firstLine.slice(firstLine.indexOf('\/') + 1, firstLine.indexOf('HTTP') - 1);
	if (resource == null) {
		resource = '';
	}
	msg["resource"] = "/" + resource;
	
	var httpType = firstLine.slice(firstLine.indexOf('HTTP/'), firstLine.indexOf('HTTP/') + 8);
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

	if(typeof msg["content-type"] != "undefined"){
		if(msg["content-type"].indexOf(';') > -1){
			msg["content-type"] = msg["content-type"].slice(0, msg["content-type"].indexOf(';'));
		}
	}

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
			toParse = toParse.slice(toParse.indexOf('\r\n\r\n') + 4);
			res.push(parseChunck(headers[socket], socket));
			if(res[res.length -1] != -1){
				headers[socket] = '';
				if(500 != res){
					//var lenLeft = toParse.length;
					var conLen = msgs[socket][msgs[socket].length - 1]["content-length"];
					if(typeof conLen === "string"){
						conLen = parseInt(conLen);
					}else{
						conLen = 0;
					}
					msgs[socket][msgs[socket].length - 1]["body"] = toParse;
					//if (lenLeft +1 >= conLen ){
					//	var bdy = toParse.slice(0, conLen + 1);
					//	msgs[socket][msgs[socket].length - 1]["body"] = bdy;
					//	toParse = toParse.slice(conLen, lenLeft);
					//}else{
					//	//TODO concat stuff
					//}
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

module.exports.querySplit = querySplit;
module.exports.dictRequestSplit = dictRequestSplit;
module.exports.resourceIsMatch = resourceIsMatch;
module.exports.typeIsMatch = typeIsMatch;
module.exports.msgs = msgs;
module.exports.init = init;
module.exports.parse = parse;
