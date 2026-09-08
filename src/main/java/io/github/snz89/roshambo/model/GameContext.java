package io.github.snz89.roshambo.model;

import io.github.snz89.roshambo.exception.MissingContextDataException;
import io.github.snz89.roshambo.model.enums.Move;
import io.github.snz89.roshambo.model.enums.RoundResult;

import java.util.List;
import java.util.Map;

public class GameContext {
    private final Long playerId;
    private final List<Move> currentGameMoves;
    private final List<RoundResult> lastGamesResults;
    private final Map<Integer, Double> winratesByPeriods;

    public GameContext(Long playerId,
                       List<Move> currentGameMoves,
                       List<RoundResult> lastGamesResults,
                       Map<Integer, Double> winratesByPeriods) {
        this.playerId = playerId;
        this.currentGameMoves = currentGameMoves;
        this.lastGamesResults = lastGamesResults;
        this.winratesByPeriods = winratesByPeriods;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public List<Move> getCurrentGameMoves() {
        return currentGameMoves;
    }

    public List<RoundResult> getLastRoundsResults() {
        return lastGamesResults;
    }

    private double getWinrateByPeriod(int days) {
        if (!winratesByPeriods.containsKey(days)) {
            throw new MissingContextDataException("Missing winrate for period of " + days + " days");
        }
        return winratesByPeriods.get(days);
    }
}