const router = require('express').Router();
const fs = require('fs');
const multer = require('multer');
const upload = multer({dest: 'uploads/'}).any();

router.get('/', (req, res, next) => {
    res.json({
        province: '臺灣省',
        cities: [
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

router.post('/upload', (req, res, next) => {
    upload(req, res, (err) => {
        if (err) {
            next(err);
        }
        req.file = req.files[0];
        let tmp_path = req.file.path;
        let target_path = 'uploads/' + req.file.originalname;
        if (!fs.existsSync('uploads/')) {
            fs.mkdirSync('uploads/')
        }

        let src = fs.createReadStream(tmp_path);
        let dest = fs.createWriteStream(target_path);
        src.pipe(dest);
        src.on('end', () => {
            res.json({code: 0, msg: '上传成功'})
        });
        src.on('error', (err) => {
            next(err);
        });
    });
});

router.post('/test', (req, res, next) => {
    console.log(JSON.stringify(req.body));
    res.json({code: 0})
});

module.exports = router;