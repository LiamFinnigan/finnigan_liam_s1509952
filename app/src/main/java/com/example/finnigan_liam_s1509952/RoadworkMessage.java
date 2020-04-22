package com.example.finnigan_liam_s1509952;

// Liam Finnigan - S1509952 - MPD CW 2020

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class RoadworkMessage implements Serializable {

    private IncidentType incidentType;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private String link;
    private String geoPoint;
    private String author;
    private String comments;
    private Date publishedDate;



    public enum IncidentType {
        ROADWORKS("Traffic Scotland - Roadworks"),
        CURRENT_INCIDENTS("Traffic Scotland - Current Incidents"),
        PLANNED_ROADWORKS("Traffic Scotland - Planned Roadworks");

        private String incidentType;

        IncidentType(String incidentType){
            this.incidentType  = incidentType;
        }

        public String getIncidentType() {
            return this.incidentType;
        }
    }

    public IncidentType getIncidentType() { return incidentType; }

    public void setIncidentType(String incidentType) {
       for(IncidentType incType: IncidentType.values()) {
           if (incType.getIncidentType().equals(incidentType)){
               this.incidentType = incType;
           }
       }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setDescription(String description) {
        // remove any HTML tags
        this.description = description.replaceAll("\\<.*?\\>", " ");
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(String geoPoint) {
        this.geoPoint = geoPoint;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    @NonNull
    @Override
    public String toString() {
        String output = getIncidentType().toString() + "\n" + getTitle();
        output = (getStartDate() != null) ? output + "\n" + "\n" + "Start Date:" + "\n" + getStartDate() : output;
        output = (getEndDate() != null) ? output + "\n" + "\n" + "End Date:" + "\n" + getEndDate() : output;
        return output;
    }
}
