package io.github.snz89.roshambo.features.game.controller;

import io.github.snz89.roshambo.features.game.dto.request.GameStrategySettings;
import io.github.snz89.roshambo.features.game.dto.response.GameCreatedResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
public class GamesController {
    @PostMapping
    public GameCreatedResponse createGame(@RequestBody GameStrategySettings gameSettings) {
        return null;
    }
}
