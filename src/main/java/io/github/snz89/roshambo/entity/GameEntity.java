package io.github.snz89.roshambo.entity;

import io.github.snz89.roshambo.model.enums.GameResult;
import io.github.snz89.roshambo.model.enums.StrategyType;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "games")
    public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StrategyType botStrategy;

    @Column(nullable = false)
    private Integer playerScore = 0;

    @Column(nullable = false)
    private Integer botScore = 0;

    @Enumerated(EnumType.STRING)
    private GameResult result;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    private Instant finishedAt;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("roundNumber ASC")
    private List<RoundEntity> rounds = new ArrayList<>();

    public void addRound(RoundEntity round) {
        rounds.add(round);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public StrategyType getBotStrategy() {
        return botStrategy;
    }

    public void setBotStrategy(StrategyType botStrategy) {
        this.botStrategy = botStrategy;
    }

    public Integer getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(Integer playerScore) {
        this.playerScore = playerScore;
    }

    public Integer getBotScore() {
        return botScore;
    }

    public void setBotScore(Integer botScore) {
        this.botScore = botScore;
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

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public List<RoundEntity> getRounds() {
        return rounds;
    }

    public void setRounds(List<RoundEntity> rounds) {
        this.rounds = rounds;
    }
}
