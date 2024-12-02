package com.lostandfound.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.lostandfound.model.Item;

public class ItemDAO{
    private Connection connection;
    public ItemDAO(Connection connection){
        this.connection = connection;
    }
    public void createItem(Item item) throws SQLException{
        String sql="INSERT INTO item (description,date,location,status,type,image_path,owner_id) VALUES (?,?,?,?,?,?,?)";
        try(PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setString(1, item.getDescription());
            statement.setDate(2, java.sql.Date.valueOf(item.getDate()));
            statement.setString(3, item.getLocation());
            statement.setString(4, item.getStatus().toString());
            statement.setString(5, item.getType().toString());
            statement.setString(6, item.getImagePath());
            statement.setInt(7, item.getOwnerId());
            statement.executeUpdate();
        }
    }
    public Item getItemById(int id) throws SQLException{
        String sql = "SELECT * FROM item WHERE id = ?";
        try(Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/lost_and_found","root","rannou");
        PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setInt(1, id);
            try(ResultSet rs=statement.executeQuery()){
                if(rs.next()){
                    rs.getString("date");
                    LocalDate date = LocalDate.parse(rs.getString("date"));
                    return new Item(
                        rs.getInt("item_id"),
                        rs.getString("description"),
                        rs.getString("location"),
                        date,
                        rs.getString("image_path"),
                        rs.getInt("owner_id"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp( "updated_at")
                    );
                }
                return null;
            }
        }
    }

}