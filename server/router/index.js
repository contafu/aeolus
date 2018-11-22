const router = require('express').Router();

router.get('/', (req, res) => {
    res.json({
        province: '臺灣省',
        citys: [
            {
                city: '台北市'
            }, {
                city: '新北市'
            }, {
                city: '桃園市'
            }, {
                city: '臺中市'
            }, {
                city: '臺南市'
            }, {
                city: '高雄市'
            }, {
                city: '基隆市'
            }, {
                city: '新竹市'
            }, {
                city: '嘉義市'
            }
        ]
    });
});