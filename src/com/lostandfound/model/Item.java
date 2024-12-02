package com.lostandfound.model;
import java.sql.Timestamp;
import java.time.LocalDate;
public class Item{
    private int itemId;
    private String description;
    private String location;
    private LocalDate date;
    private String imagePath;
    private int ownerId;
    private Timestamp created_at;
    private Timestamp updated_at;

    public enum Type{
        Lost,Found
    }
    public enum Status{
        Pending,Confirmed,Rejected
    }
    private Type type;
    private Status status;

    public Item(int itemId, String description, String location, LocalDate date, String imagePath, int ownerId, Timestamp created_at,Timestamp updated_at){
        this.itemId=itemId;
        this.description=description;
        this.location=location;
        this.date=date;
        this.imagePath=imagePath;
        this.ownerId=ownerId;
        this.created_at=created_at;
        this.updated_at=updated_at;
        this.status=Status.Pending;
        this.type=Type.Lost;
    }
    public LocalDate getDate() {
        return date;
    }
    public String getDescription() {
        return description;
    }
    public String getImagePath() {
        return imagePath;
    }
    public int getItemId() {
        return itemId;
    }
    public String getLocation() {
        return location;
    }
    public int getOwnerId() {
        return ownerId;
    }
    public Status getStatus() {
        return status;
    }
    public Type getType() {
        return type;
    }
    public Timestamp getCreated_at() {
        return (created_at == null) ? null : new Timestamp(created_at.getTime());
    }
    public Timestamp getUpdated_at() {
        return (updated_at == null) ? null : new Timestamp(updated_at.getTime());
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public void setCreated_at(Timestamp created_at) {
        this.created_at = (created_at == null) ? null : new Timestamp(created_at.getTime());
    }
    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = (updated_at == null) ? null : new Timestamp(updated_at.getTime());
    }
}