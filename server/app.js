const app = require('http');
const fs = require('fs');
const path = require('path');

const privateKey = fs.readFileSync(path.join(__dirname, './ca/private.pem'), 'utf8');
const certificate = fs.readFileSync(path.join(__dirname, './ca/ca.cer'), 'utf8');
const credentials = {key: privateKey, cert: certificate};

const server = app.createServer((req, res) => {
    const data = {
        code: 200,
        msg: 'success'
    };
    res.end(JSON.stringify(data))
});

server.listen('3000', () => {
    console.log('running on 3000')
});
