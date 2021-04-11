package com.google.efinepolice;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class Report extends Document {

    private ObjectId _id;
    private Date date;
    private String description;
    private Double latitude;
    private String licence;
    private Double longitude;
    private String police;
    private String type;

    public Report(ObjectId _id, Date date, String description, Double latitude, String licence, Double longitude, String police, String type) {
        this._id = _id;
        this.date = date;
        this.description = description;
        this.latitude = latitude;
        this.licence = licence;
        this.longitude = longitude;
        this.police = police;
        this.type = type;
    }

    // Standard getters & setters
    public ObjectId get_id() { return _id; }
    public void set_id(ObjectId _id) { this._id = _id; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public String getLicence() { return licence; }
    public void setLicence(String licence) { this.licence = licence; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getPolice() { return police; }
    public void setPolice(String police) { this.police = police; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
