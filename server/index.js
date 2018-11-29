const express = require('express');
const app = new express();
const bodyParser = require('body-parser');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));

app.use((req, res, next) => {
    console.log(req.url);
    next();
});

app.use('/mobile', require('./router/index'));

app.use((err, req, res, next) => {
    res.send(err);
});

app.listen(3000, () => {
    console.log("starting... welcome to visit 'http://localhost:3000/mobile/'")
});