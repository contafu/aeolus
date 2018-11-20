let express = require('express');

let app = new express();

app.use('/mobile', require('./router/index'));

app.listen(3000, () => {
    console.log('starting... welcome to visit "http://localhost:3000/mobile/"')
})