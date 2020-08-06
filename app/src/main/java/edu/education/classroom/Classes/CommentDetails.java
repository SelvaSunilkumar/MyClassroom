package edu.education.classroom.Classes;

public class CommentDetails {
    private String announcementDate;
    private String userName;
    private String userProfile;
    private String commentMessage;

    public CommentDetails(String commentMessage,String userName,String userProfile,String announcementDate)
    {
        this.commentMessage = commentMessage;
        this.userName = userName;
        this.userProfile = userProfile;
        this.announcementDate = announcementDate;
    }

    public String getAnnouncementDate() {
        return announcementDate;
    }

    public void setAnnouncementDate(String announcementDate) {
        this.announcementDate = announcementDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getCommentMessage() {
        return commentMessage;
    }

    public void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage;
    }
}
