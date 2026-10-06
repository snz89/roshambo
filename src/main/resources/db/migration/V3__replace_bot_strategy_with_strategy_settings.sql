ALTER TABLE games
    ADD COLUMN strategy_settings JSON NOT NULL;

ALTER TABLE games
DROP
COLUMN bot_strategy;