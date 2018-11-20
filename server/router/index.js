let router = require('express').Router();

router.get('/', (req, res) => {
    res.json({
        province: '台湾省',
        citys: [
            {
                city: '台北'
            }, {
                city: '新北'
            }, {
                city: '桃园'
            }, {
                city: '台中'
            }, {
                city: '台南'
            }, {
                city: '高雄'
            }, {
                city: '基隆'
            }, {
                city: '新竹'
            }, {
                city: '嘉义'
            }
        ]
    });
});

module.exports = router;