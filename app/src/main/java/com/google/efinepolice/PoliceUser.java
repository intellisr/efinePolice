package com.google.efinepolice;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class PoliceUser extends RealmObject implements Serializable {
    @PrimaryKey
    public String Email;
    @Required
    public String idNo;
    @Required
    public String Name;
    @Required
    public String rankNo;

    public PoliceUser(String email, String idNo, String name, String rankNo) {
        Email = email;
        this.idNo = idNo;
        Name = name;
        this.rankNo = rankNo;
    }

    public PoliceUser() {
    }

    public String getEmail() {
        return Email;
    }

    public String getIdNo() {
        return idNo;
    }

    public String getName() {
        return Name;
    }

    public String getRankNo() {
        return rankNo;
    }
}
