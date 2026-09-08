CREATE TABLE games
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT                   NOT NULL,
    bot_strategy VARCHAR(50)              NOT NULL,
    status       VARCHAR(30)              NOT NULL,
    max_rounds   INT                      NOT NULL,
    player_score INT                      NOT NULL DEFAULT 0,
    bot_score    INT                      NOT NULL DEFAULT 0,
    winner       VARCHAR(30),
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    finished_at  TIMESTAMP WITH TIME ZONE,

    CONSTRAINT fk_games_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_games_user_id ON games (user_id);


CREATE TABLE rounds
(
    id           BIGSERIAL PRIMARY KEY,
    game_id      BIGINT                   NOT NULL,
    round_number INT                      NOT NULL,
    player_move  VARCHAR(30)              NOT NULL,
    bot_move     VARCHAR(30)              NOT NULL,
    result       VARCHAR(30)              NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_rounds_game FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE,
    CONSTRAINT uq_rounds_game_id_round_number UNIQUE (game_id, round_number)
);

CREATE INDEX idx_rounds_game_id ON rounds (game_id);