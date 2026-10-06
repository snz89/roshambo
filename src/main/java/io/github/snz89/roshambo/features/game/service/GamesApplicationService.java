package io.github.snz89.roshambo.features.game.service;

import io.github.snz89.roshambo.features.game.dto.request.CreateGameRequest;
import io.github.snz89.roshambo.features.game.dto.response.GameCreatedResponse;
import io.github.snz89.roshambo.features.game.entity.GameEntity;
import io.github.snz89.roshambo.features.game.entity.UserEntity;
import io.github.snz89.roshambo.features.game.repository.GamesRepository;
import io.github.snz89.roshambo.features.game.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GamesApplicationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GamesApplicationService.class);

    private final GamesRepository gamesRepository;
    private final UsersRepository usersRepository;

    public GamesApplicationService(GamesRepository gamesRepository, UsersRepository usersRepository) {
        this.gamesRepository = gamesRepository;
        this.usersRepository = usersRepository;
    }

    @Transactional
    public GameCreatedResponse createGame(CreateGameRequest createGameRequest,
                                          Authentication authentication) {
        GameEntity gameEntity = new GameEntity();
        gameEntity.setStrategySettings(createGameRequest.strategySettings());

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();

            UserEntity user = usersRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "User not found" + username
                    ));

            gameEntity.setUser(user);
        }

        gameEntity = gamesRepository.save(gameEntity);

        LOGGER.atInfo()
                .addKeyValue("game_id", gameEntity.getId())
                .log("game created");

        return new GameCreatedResponse(gameEntity.getId());
    }
}
