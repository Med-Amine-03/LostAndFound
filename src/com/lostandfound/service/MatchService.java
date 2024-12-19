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

    public List<Match> getAllMatches() {
        try {
            return matchDAO.getAllMatches();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean createMatch(Match match) {
        try {
            matchDAO.createMatch(match);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Match getMatchById(int matchId) {
        try {
            return matchDAO.getMatchById(matchId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteMatch(int matchId) {
        try {
            matchDAO.deleteMatch(matchId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMatch(Match match) {
        try {
            matchDAO.updateMatch(match);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
