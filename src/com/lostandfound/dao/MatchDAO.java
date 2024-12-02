package com.lostandfound.dao;
import com.lostandfound.model.Match;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class MatchDAO{
    Connection connection;
    public MatchDAO(Connection connection) {
        this.connection=connection;
    }
   public void createMatch(Match match) throws SQLException {
        String sql="INSERT INTO matches (lost_item_id,found_item_id, levenshtein_score, status, reviewed_by_admin)VALUES (?,?,?,?,?)";
        try(PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setInt(1,match.getLostItemId());
            statement.setInt(2, match.getFoundItemId());
            statement.setDouble(3, match.getLevenshteinScore());
            statement.setString(4, match.getStatus());
            statement.setBoolean(5, match.getReviewedByAdmin());
            statement.executeQuery();
        }
    }
    public void updateMatch(Match match) throws SQLException {
        String sql="UPDATE matches SET lost_item_id=? , found_item_id=?, levenshtein_score=?, status=?, reviewed_by_admin=? WHERE id=?";
        try(Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/lost_and_found","root","rannou");
        PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setInt(1, match.getLostItemId());
            statement.setInt(2, match.getFoundItemId());
            statement.setDouble(3, match.getLevenshteinScore());
            statement.setString(4, match.getStatus());
            statement.setBoolean(5, match.getReviewedByAdmin());
            statement.executeQuery();
        }
    }   
    public void deleteMatch(int id) throws SQLException {
        String sql="DELETE FROM matches WHERE id=?";
        try(Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/lost_and_found","root","rannou");
        PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setInt(1,id);
            statement.executeQuery();
        }
    }
    public List<Match> getAllMatches() throws SQLException {
        String sql="SELECT * FROM matches";
        List<Match> matches=new ArrayList<>();
        try(Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/lost_and_found", "root", "rannou");
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();) {
            while(resultSet.next()){
                Match match = new Match(
                    resultSet.getInt("match_id"),
                    resultSet.getInt("lost_item_id"),
                    resultSet.getInt("found_item_id"),
                    resultSet.getDouble("levenshtein_score"),
                    resultSet.getBoolean("reviewed_by_admin"),
                    resultSet.getTimestamp("created_at"),
                    resultSet.getTimestamp("updated_at")
                );
                matches.add(match);
            }
        }
        return matches;
    }
}