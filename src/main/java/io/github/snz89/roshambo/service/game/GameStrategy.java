package io.github.snz89.roshambo.service.game;

import io.github.snz89.roshambo.model.GameContext;
import io.github.snz89.roshambo.model.GameContextQuery;
import io.github.snz89.roshambo.model.Move;

public interface GameStrategy {
    GameContextQuery getContextQuery();
    Move determineNextMove(GameContext gameContext);
}
