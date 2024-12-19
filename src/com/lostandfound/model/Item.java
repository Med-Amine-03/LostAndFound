package com.lostandfound.model;

import java.sql.Timestamp;
import java.time.LocalDate;

public class Item {
    private int itemId;
    private String description;
    private String location;
    private LocalDate date;
    private String imagePath;
    private int ownerId;
    private Timestamp created_at;
    private Timestamp updated_at;

    public enum Type {
        Lost, Found
    }
    public enum Status {
        Pending, Matched, Restored, Rejected
    }

    private Type type;
    private Status status;

    public Item(int itemId, String description, String location, LocalDate date, String imagePath, int ownerId, Timestamp created_at, Timestamp updated_at) {
        this.itemId = itemId;
        this.description = description;
        this.location = location;
        this.date = date;
        this.imagePath = imagePath;
        this.ownerId = ownerId;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.status = Status.Pending;  
        this.type = Type.Lost;         
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        this.updated_at = new Timestamp(System.currentTimeMillis()); 
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Timestamp getCreated_at() {
        return (created_at == null) ? null : new Timestamp(created_at.getTime());
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = (created_at == null) ? null : new Timestamp(created_at.getTime());
    }

    public Timestamp getUpdated_at() {
        return (updated_at == null) ? null : new Timestamp(updated_at.getTime());
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = (updated_at == null) ? null : new Timestamp(updated_at.getTime());
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", date=" + date +
                ", imagePath='" + imagePath + '\'' +
                ", ownerId=" + ownerId +
                ", status=" + status +
                ", type=" + type +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
