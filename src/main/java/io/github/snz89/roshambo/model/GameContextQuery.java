package io.github.snz89.roshambo.model;

import java.util.HashSet;
import java.util.Set;

public class GameContextQuery {
    public static GameContextQuery EMPTY = GameContextQuery.builder().build();

    private final boolean includeCurrentGameMoves;
    private final int lastGamesResultCount;
    private final Set<Integer> winrateDaysPeriods;

    private GameContextQuery(Builder builder) {
        this.includeCurrentGameMoves = builder.includeCurrentGameMoves;
        this.lastGamesResultCount = builder.lastGamesResultCount;
        this.winrateDaysPeriods = Set.copyOf(builder.winrateDaysPeriods);
    }

    public boolean isIncludeCurrentGameMoves() {
        return includeCurrentGameMoves;
    }

    public int getLastGamesResultCount() {
        return lastGamesResultCount;
    }

    public Set<Integer> getWinrateDaysPeriods() {
        return winrateDaysPeriods;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean includeCurrentGameMoves = false;
        private Integer lastGamesResultCount = null;
        private final Set<Integer> winrateDaysPeriods = new HashSet<>();

        public Builder includeCurrentGameMoves() {
            this.includeCurrentGameMoves = true;
            return this;
        }

        public Builder includeLastGamesResults(int count) {
            this.lastGamesResultCount = count;
            return this;
        }

        public Builder winrateLastDays(int days) {
            if (days > 0) {
                this.winrateDaysPeriods.add(days);
            }
            return this;
        }

        public GameContextQuery build() {
            return new GameContextQuery(this);
        }
    }
}
