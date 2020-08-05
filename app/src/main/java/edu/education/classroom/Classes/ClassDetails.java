package edu.education.classroom.Classes;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClassDetails {

    private String classId;
    private String className;
    private String classRoom;
    private String classSection;
    private String classDesription;
    private int backgroundNumber;

    public ClassDetails(String classId,String className, String classRoom, String classSection, String classDesription,int backgroundNumber) {
        this.classId = classId;
        this.className = className;
        this.classRoom = classRoom;
        this.classSection = classSection;
        this.classDesription = classDesription;
        this.backgroundNumber = backgroundNumber;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getClassSection() {
        return classSection;
    }

    public void setClassSection(String classSection) {
        this.classSection = classSection;
    }

    public String getClassDesription() {
        return classDesription;
    }

    public void setClassDesription(String classDesription) {
        this.classDesription = classDesription;
    }

    public int getBackgroundNumber() {
        return backgroundNumber;
    }

    public void setBackgroundNumber(int backgroundNumber) {
        this.backgroundNumber = backgroundNumber;
    }
}
