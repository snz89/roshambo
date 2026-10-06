package io.github.snz89.roshambo.domain.service;

import io.github.snz89.roshambo.domain.model.GameContext;
import io.github.snz89.roshambo.domain.model.GameContextQuery;
import io.github.snz89.roshambo.domain.model.enums.Move;

public interface GameStrategy {
    GameContextQuery getContextQuery();
    Move determineNextMove(GameContext gameContext);
}
