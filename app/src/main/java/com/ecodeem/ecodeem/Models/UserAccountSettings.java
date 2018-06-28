package com.ecodeem.ecodeem.Models;

public class UserAccountSettings {


    private String description;
    private String display_name;
    private long posts;
    private long eco_credit;
    private long connections;
    private String profile_photo;
    private String username;
    private String location;

    public UserAccountSettings(String description, String display_name, long posts, long eco_credit, long connections, String profile_photo, String username, String location) {
        this.description = description;
        this.display_name = display_name;
        this.posts = posts;
        this.eco_credit = eco_credit;
        this.connections = connections;
        this.profile_photo = profile_photo;
        this.username = username;
        this.location = location;
    }


    public UserAccountSettings() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public long getPosts() {
        return posts;
    }

    public void setPosts(long posts) {
        this.posts = posts;
    }

    public long getEco_credit() {
        return eco_credit;
    }

    public void setEco_credit(long eco_credit) {
        this.eco_credit = eco_credit;
    }

    public long getConnections() {
        return connections;
    }

    public void setConnections(long connections) {
        this.connections = connections;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "UserAccountSettings{" +
                "description='" + description + '\'' +
                ", display_name='" + display_name + '\'' +
                ", posts=" + posts +
                ", eco_credit=" + eco_credit +
                ", connections=" + connections +
                ", profile_photo='" + profile_photo + '\'' +
                ", username='" + username + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
