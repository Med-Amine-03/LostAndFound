package com.lostandfound.model;

import java.sql.Timestamp;

public class Match {
    private int matchId;
    private int lostItemId;
    private int foundItemId;
    private double levenshteinScore;
    private boolean reviewedByAdmin;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public enum Status {
        Pending, Confirmed, Rejected
    }
    private Status status;

    public Match(int lostItemId, int foundItemId, double levenshteinScore) {
        this.lostItemId = lostItemId;
        this.foundItemId = foundItemId;
        this.levenshteinScore = levenshteinScore;
        this.status = Status.Pending; 
        this.reviewedByAdmin = false; 
        this.createdAt = new Timestamp(System.currentTimeMillis()); 
        this.updatedAt = this.createdAt; 
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getLostItemId() {
        return lostItemId;
    }

    public void setLostItemId(int lostItemId) {
        this.lostItemId = lostItemId;
    }

    public int getFoundItemId() {
        return foundItemId;
    }

    public void setFoundItemId(int foundItemId) {
        this.foundItemId = foundItemId;
    }

    public double getLevenshteinScore() {
        return levenshteinScore;
    }

    public void setLevenshteinScore(double levenshteinScore) {
        this.levenshteinScore = levenshteinScore;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    public boolean getReviewedByAdmin() {
        return reviewedByAdmin;
    }

    public void setReviewedByAdmin(boolean reviewedByAdmin) {
        this.reviewedByAdmin = reviewedByAdmin;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Match{" +
                "matchId=" + matchId +
                ", lostItemId=" + lostItemId +
                ", foundItemId=" + foundItemId +
                ", levenshteinScore=" + levenshteinScore +
                ", status=" + status +
                ", reviewedByAdmin=" + reviewedByAdmin +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
