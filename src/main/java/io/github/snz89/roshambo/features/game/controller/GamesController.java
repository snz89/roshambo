package io.github.snz89.roshambo.features.game.controller;

import io.github.snz89.roshambo.features.game.dto.request.CreateGameRequest;
import io.github.snz89.roshambo.features.game.dto.response.GameCreatedResponse;
import io.github.snz89.roshambo.features.game.service.GamesApplicationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
public class GamesController {
    private final GamesApplicationService gamesService;

    public GamesController(GamesApplicationService gamesService) {
        this.gamesService = gamesService;
    }

    @PostMapping
    public GameCreatedResponse createGame(@RequestBody CreateGameRequest request,
                                          Authentication authentication) {
        return gamesService.createGame(request, authentication);
    }
}
