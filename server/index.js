const express = require('express');
const app = new express();

app.use('/mobile', require('./router/index'));

app.use((err, req, res, next) => {
    res.send(err);
});

app.listen(3000, () => {
    console.log("starting... welcome to visit 'http://localhost:3000/mobile/'")
});