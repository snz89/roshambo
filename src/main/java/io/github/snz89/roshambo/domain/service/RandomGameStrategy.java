package io.github.snz89.roshambo.domain.service;

import io.github.snz89.roshambo.domain.model.GameContext;
import io.github.snz89.roshambo.domain.model.GameContextQuery;
import io.github.snz89.roshambo.domain.model.enums.Move;

import java.util.concurrent.ThreadLocalRandom;

public class RandomGameStrategy implements GameStrategy {
    private static final Move[] MOVES = Move.values();

    @Override
    public GameContextQuery getContextQuery() {
        return GameContextQuery.EMPTY;
    }

    @Override
    public Move determineNextMove(GameContext gameContext) {
        int randomIndex = ThreadLocalRandom.current().nextInt(MOVES.length);
        return MOVES[randomIndex];
    }
}
