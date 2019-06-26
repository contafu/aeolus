package com.olympians.aeolus.sample;

import com.olympians.aeolus.AeolusRequest;
import com.olympians.aeolus.annotations.Post;
import com.olympians.aeolus.utils.ContentTypeKt;

import java.io.File;

@Post(host = "http://192.168.1.15:3700/", api = "common/upload", contentType = ContentTypeKt.ContentType_Multipart)
public class UploadRequest implements AeolusRequest {

    private int uid;

    private int pigeonId;

    private int type;

    private File imageFile;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getPigeonId() {
        return pigeonId;
    }

    public void setPigeonId(int pigeonId) {
        this.pigeonId = pigeonId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }
}
