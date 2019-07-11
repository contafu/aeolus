const app = require('http');
const fs = require('fs');
const path = require('path');

const privateKey = fs.readFileSync(path.join(__dirname, './ca/private.pem'), 'utf8');
const certificate = fs.readFileSync(path.join(__dirname, './ca/ca.cer'), 'utf8');
const credentials = {key: privateKey, cert: certificate};

const server = app.createServer((req, res) => {
    switch (req.url) {
        case '/test':
            setTimeout(() => {
                res.statusCode = 200;
                res.end(req.url);
            }, 1000);
            break;
        case '/index':
        default:
            setTimeout(() => {
                res.statusCode = 200;
                res.end(req.url);
            }, 1000);
            break
    }
});

server.listen('3000', () => {
    console.log('running on 3000')
});
