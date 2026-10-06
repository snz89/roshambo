package io.github.snz89.roshambo.features.game.repository;

import io.github.snz89.roshambo.features.game.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamesRepository extends JpaRepository<GameEntity, Long> {
}
