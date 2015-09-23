/**
 * Server for running e2e tests
 */
var connect = require('connect'),
    serveStatic = require('serve-static'),
    fs = require('fs'),
    less = require('less'),
    PORT = 8080;

var src = "../../assets/less/main.less",
    dest = "../../assets/css/main.css";

var app = connect();

app.use('/', function barMiddleware(req, res, next) {
    if (req.url == '/') {
        compileLess(src, dest);
    }
    next();
});

app.use(serveStatic("../../")).listen(PORT, function() {
    console.log("Server listening on: http://localhost:%s", PORT);
});

function compileLess(source, dest) {
    less.render(fs.readFileSync(source).toString(),
        {
            paths: ['.', '../../assets/less'],  // Specify search paths for @import directives
            compress: false
        },
        function (e, output) {
            fs.writeFileSync(dest, output.css);
        });
}