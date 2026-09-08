package io.github.snz89.roshambo.entity;

import io.github.snz89.roshambo.model.enums.GameResult;
import io.github.snz89.roshambo.model.enums.Move;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;

@Entity
@Table(
        name = "rounds",
        uniqueConstraints = @UniqueConstraint(columnNames = {"game_id", "round_number"})
)
public class RoundEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private GameEntity game;

    @Column(name = "round_number", nullable = false)
    private Integer roundNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Move playerMove;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Move botMove;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameResult result;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameEntity getGame() {
        return game;
    }

    public void setGame(GameEntity game) {
        this.game = game;
    }

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

    public Move getPlayerMove() {
        return playerMove;
    }

    public void setPlayerMove(Move playerMove) {
        this.playerMove = playerMove;
    }

    public Move getBotMove() {
        return botMove;
    }

    public void setBotMove(Move botMove) {
        this.botMove = botMove;
    }

    public GameResult getResult() {
        return result;
    }

    public void setResult(GameResult result) {
        this.result = result;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
