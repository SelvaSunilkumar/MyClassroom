package edu.education.classroom.Classes;

import java.util.Comparator;

public class AnnouncementDetails {

    public String announcementId;
    public String classId;
    public String username;
    public String message;
    public String date;
    public String profilePic;
    public String name;

    public AnnouncementDetails(String announcementId, String classId, String username, String message, String date, String profilePic,String name) {
        this.announcementId = announcementId;
        this.classId = classId;
        this.username = username;
        this.message = message;
        this.date = date;
        this.profilePic = profilePic;
        this.name = name;
    }

    public String getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(String announcementId) {
        this.announcementId = announcementId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
