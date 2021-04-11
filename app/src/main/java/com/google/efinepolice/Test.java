package com.google.efinepolice;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Map;

public class Test extends Document {

    private String licence;
    private String police;

    public Test(String licence, String police) {
        this.licence = licence;
        this.police = police;
    }

    public Test(String key, Object value, String licence, String police) {
        super(key, value);
        this.licence = licence;
        this.police = police;
    }

    public Test(Map<String, Object> map, String licence, String police) {
        super(map);
        this.licence = licence;
        this.police = police;
    }
}
