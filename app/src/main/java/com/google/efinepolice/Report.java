package com.google.efinepolice;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Map;

public class Report extends Document {
    ObjectId _id;
    String type;
    String description;
    String licence;
    String police;
    double latitude;
    double longitude;
    Date date;

    public Report(ObjectId _id, String type, String description, String licence, String police, double latitude, double longitude, Date date) {
        this._id = _id;
        this.type = type;
        this.description = description;
        this.licence = licence;
        this.police = police;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }

    public ObjectId get_id() {
        return _id;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getLicence() {
        return licence;
    }

    public String getPolice() {
        return police;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Date getDate() {
        return date;
    }
}
