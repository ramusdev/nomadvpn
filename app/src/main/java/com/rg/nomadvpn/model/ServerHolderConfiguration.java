package com.rg.nomadvpn.model;

public class ServerHolderConfiguration {
    private String user;
    private String password;
    private String country;
    private String city;
    private String fileName;
    private String flagName;
    private int id;

    public ServerHolderConfiguration() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setFlagName(String flagName) {
        this.flagName = flagName;
    }

    public String getFlagName() {
        return flagName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
