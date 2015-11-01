/**
 * Server for running e2e tests
 * All e2e tests should mock backend
 */
var connect = require('connect'),
    serveStatic = require('serve-static'),

    PORT = 8080;

var app = connect();

app.use(serveStatic("../")).listen(PORT, function () {
    console.log("Server listening on: http://localhost:%s", PORT);
});
