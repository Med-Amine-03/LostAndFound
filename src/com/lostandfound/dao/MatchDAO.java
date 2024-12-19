package com.lostandfound.dao;

import com.lostandfound.model.Match;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchDAO {
    private Connection connection;

    public MatchDAO(Connection connection) {
        this.connection = connection;
    }

    public void createMatch(Match match) throws SQLException {
        String sql = "INSERT INTO matches (lost_item_id, found_item_id, levenshtein_score, status, reviewed_by_admin) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, match.getLostItemId());
            statement.setInt(2, match.getFoundItemId());
            statement.setDouble(3, match.getLevenshteinScore());
            statement.setString(4, match.getStatus().name());
            statement.setBoolean(5, match.getReviewedByAdmin());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        match.setMatchId(generatedKeys.getInt(1)); 
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in createMatch: " + ex.getMessage());
            throw ex;
        }
    }

    public void updateMatch(Match match) throws SQLException {
        String sql = "UPDATE matches SET lost_item_id=?, found_item_id=?, levenshtein_score=?, status=?, reviewed_by_admin=? WHERE match_id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, match.getLostItemId());
            statement.setInt(2, match.getFoundItemId());
            statement.setDouble(3, match.getLevenshteinScore());
            statement.setString(4, match.getStatus().name());
            statement.setBoolean(5, match.getReviewedByAdmin());
            statement.setInt(6, match.getMatchId());

            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error in updateMatch: " + ex.getMessage());
            throw ex;
        }
    }

    public void deleteMatch(int matchId) throws SQLException {
        String sql = "DELETE FROM matches WHERE match_id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, matchId);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error in deleteMatch: " + ex.getMessage());
            throw ex;
        }
    }

    public List<Match> getAllMatches() throws SQLException {
        String sql = "SELECT * FROM matches";
        List<Match> matches = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Match match = new Match(
                        resultSet.getInt("lost_item_id"),
                        resultSet.getInt("found_item_id"),
                        resultSet.getDouble("levenshtein_score")
                );
                match.setMatchId(resultSet.getInt("match_id"));
                match.setStatus(Match.Status.valueOf(resultSet.getString("status")));
                match.setReviewedByAdmin(resultSet.getBoolean("reviewed_by_admin"));
                match.setCreatedAt(resultSet.getTimestamp("created_at"));
                match.setUpdatedAt(resultSet.getTimestamp("updated_at"));

                matches.add(match);
            }
        }
        return matches;
    }

    public Match getMatchById(int matchId) throws SQLException {
        String sql = "SELECT * FROM matches WHERE match_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, matchId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Match match = new Match(
                            resultSet.getInt("lost_item_id"),
                            resultSet.getInt("found_item_id"),
                            resultSet.getDouble("levenshtein_score")
                    );
                    match.setMatchId(resultSet.getInt("match_id"));
                    match.setStatus(Match.Status.valueOf(resultSet.getString("status")));
                    match.setReviewedByAdmin(resultSet.getBoolean("reviewed_by_admin"));
                    match.setCreatedAt(resultSet.getTimestamp("created_at"));
                    match.setUpdatedAt(resultSet.getTimestamp("updated_at"));
                    return match;
                }
            }
        }
        return null; 
    }
    
}
