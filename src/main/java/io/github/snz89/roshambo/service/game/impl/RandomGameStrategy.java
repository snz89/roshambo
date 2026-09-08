package io.github.snz89.roshambo.service.game.impl;

import io.github.snz89.roshambo.model.GameContext;
import io.github.snz89.roshambo.model.GameContextQuery;
import io.github.snz89.roshambo.model.enums.Move;
import io.github.snz89.roshambo.service.game.GameStrategy;

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
