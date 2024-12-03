package com.lostandfound.service;

import com.lostandfound.dao.MatchDAO;
import com.lostandfound.model.Match;
import java.sql.SQLException;
import java.util.List;

public class MatchService {
    private MatchDAO matchDAO;

    public MatchService(MatchDAO matchDAO) {
        this.matchDAO = matchDAO;
    }

    public void createMatch(Match match) {
        try {
            matchDAO.createMatch(match);  
        } 
        catch (SQLException ex) {
            System.out.println("Error creating match: " + ex.getMessage());
        }
    }

    public void updateMatch(Match match) {
        try {
            matchDAO.updateMatch(match); 
        } catch (SQLException ex) {
            System.out.println("Error updating match: " + ex.getMessage());
        }
    }

    public void deleteMatch(int matchId) {
        try {
            matchDAO.deleteMatch(matchId); 
        } catch (SQLException ex) {
            System.out.println("Error deleting match: " + ex.getMessage());
        }
    }

    public List<Match> getAllMatches() {
        try {
            return matchDAO.getAllMatches();  
        } catch (SQLException ex) {
            System.out.println("Error retrieving all matches: " + ex.getMessage());
            return null;
        }
    }
}
