/**
 * Proxy dev server to http://localhost:8080/api
 * It also compiles *.less files
 */
var connect = require('connect'),
    serveStatic = require('serve-static'),
    proxy = require('proxy-middleware'),
    fs = require('fs'),
    less = require('less'),
    url = require('url'),
    PORT = 9000;

var src = "../../assets/less/main.less",
    dest = "../../assets/css/main.css";

var app = connect();

app.use('/', function barMiddleware(req, res, next) {
    if (req.url == '/') {
        compileLess(src, dest);
    }
    next();
});

app.use('/api', proxy(url.parse('http://localhost:8080/api')));

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