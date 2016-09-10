/*
 tester which tests our web-server's core functionality, including
 reviving and shutting it down
 */
var HOST = 'localhost';
var PORT = 8080;

var passed_counter = 0;
var failed_counter = 0;
var test_counter = 0;
var DEBUG = true;
var http = require('http');
http.globalAgent.maxSockets = 100;
var hujiwebserver = require('./hujiwebserver');
//work on server
var server = hujiwebserver.start(PORT, function(err) {

    // check if there were errors in revivng the server
    if (err) {
        console.log("test failed : could not revive server " + err);
        return;
    }
});
// register of all routes relevant for testing.
server.use(function(req, res, next) {
    res.status(200);
    res.send("bye");
});

setTimeout(function() {
    server.stop();
    console.log("server shutdown");
}, 5000);

function single_server_test(options, expected) {
    var req_options = {
        hostname: HOST,
        port: PORT,
        path: options.path,
        method: options.method
    };


    // check if http request test should be sent with some headers
    if (options.headers) {
        req_options.headers = options.headers;
    }

    // send the http request test to the server
    var req = http.request(req_options, function(res) {
        var buffer = '';
        res.setEncoding('utf8');
        // accumulating the http response body
        res.on('data', function(chunk) {
            buffer += chunk;
        });

        // upon receiving the whole http response
        res.on('end', function() {
            console.log("Ended");
            test_counter++;
            res.buffer = buffer;


            // check if we pass the relevant test - namely what expected is what we got
            if (res.statusCode != expected.status || (expected.data && (expected.data != buffer)) ||
                (expected.func && !expected.func(res))) {

                console.warn("test #" + test_counter + ":  " + options.test_name + " ... FAILED");
                failed_counter++;

                // in case, we're in DEBUG mode show more details why the test failed.
                if (DEBUG) {
                    console.warn("--------------------------------------------------");

                    // check if http response status is not what we expected.
                    if (res.statusCode != expected.status) {
                        console.warn("got ", res.statusCode, " but expected", expected.status);
                    }

                    // check if http response body is not what we expected.
                    if (buffer != expected.data) {
                        console.warn("got ", buffer, " but expected", expected.data);
                    }

                    if (expected.func && !expected.func(res)) {
                        console.warn("func failed");
                        console.warn(expected.func.toString());
                    }
                    console.warn("--------------------------------------------------");
                }

                // current test succeeded
            } else {
                console.log("test #" + test_counter + ":  " + options.test_name + " ... PASSED");
                passed_counter++;
            }

        });

    });

    req.on('error', function(e) {
        console.log('problem with request: ' + e.message);
    });

    if (options.data) {
        req.write(options.data);
    }
    req.end();
}

for(var i = 0; i < 1000; i++){
    single_server_test(
        {
            path:"/request/test/params/param123",
            method:"GET",
            test_name:"testing load"
        },
        {
            status:200,
            data:"bye"
        });
}

console.log("ended sending requests");