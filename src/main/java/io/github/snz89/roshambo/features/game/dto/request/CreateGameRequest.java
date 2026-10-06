package io.github.snz89.roshambo.features.game.dto.request;

import io.github.snz89.roshambo.domain.model.GameStrategySettings;

public record CreateGameRequest(
        GameStrategySettings strategySettings
) {
}
