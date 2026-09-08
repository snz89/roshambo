package io.github.snz89.roshambo.model;

public enum Move {
    ROCK,
    PAPER,
    SCISSORS;

    public RoundResult compareWith(Move opponentMove) {
        if (this == opponentMove) {
            return RoundResult.DRAW;
        }

        return switch (this) {
            case ROCK -> (opponentMove == SCISSORS) ? RoundResult.WIN : RoundResult.LOSE;
            case PAPER -> (opponentMove == ROCK) ? RoundResult.WIN : RoundResult.LOSE;
            case SCISSORS -> (opponentMove == PAPER) ? RoundResult.WIN : RoundResult.LOSE;
        };
    }
}
