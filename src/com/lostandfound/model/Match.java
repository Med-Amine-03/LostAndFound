package com.lostandfound.model;

import java.sql.Timestamp;

public class Match{
    private int matchId;
    private int lostItemId;
    private int foundItemId;
    private double levenshteinScore;
    private boolean reviewedByAdmin;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public enum Status{Pending,Confirmed,Rejected}
    private Status status;

    public Match(int matchId,int lostItemId,int foundItemId,double levenshteinScore,boolean reviewedByAdmin,Timestamp createdAt, Timestamp updatedAt){
        this.matchId = matchId;
        this.lostItemId=lostItemId;
        this.foundItemId=foundItemId;
        this.levenshteinScore=levenshteinScore;
        this.status=Status.Pending;
        this.reviewedByAdmin=reviewedByAdmin;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
    }
    public int getFoundItemId() {
        return foundItemId;
    }
    public double getLevenshteinScore() {
        return levenshteinScore;
    }
    public int getLostItemId() {
        return lostItemId;
    }
    public int getMatchId() {
        return matchId;
    }
    public boolean getReviewedByAdmin(){
        return reviewedByAdmin;
    }
    public String getStatus() {
        return status.name();
    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    public void setFoundItemId(int foundItemId) {
        this.foundItemId = foundItemId;
    }
    public void setLevenshteinScore(double levenshteinScore) {
        this.levenshteinScore = levenshteinScore;
    }
    public void setLostItemId(int lostItemId) {
        this.lostItemId = lostItemId;
    }
    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }
    public void setReviewedByAdmin(boolean reviewedByAdmin) {
        this.reviewedByAdmin = reviewedByAdmin;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public void setCreatedAt(Timestamp createdAT) {
        this.createdAt = createdAT;
    }
    public void setUpdatedAt(Timestamp updatedAT) {
        this.updatedAt = updatedAT;
    }
}