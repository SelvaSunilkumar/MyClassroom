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

    public ClassDetails(String classId,String className, String classRoom, String classSection, String classDesription) {
        this.classId = classId;
        this.className = className;
        this.classRoom = classRoom;
        this.classSection = classSection;
        this.classDesription = classDesription;
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

}
