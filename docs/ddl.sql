CREATE TABLE IF NOT EXISTS `Image`
(
    `image_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `path`     TEXT COLLATE NOCASE,
    `title`    TEXT COLLATE NOCASE
);
CREATE UNIQUE INDEX IF NOT EXISTS `index_Image_path` ON `Image` (`path`);
CREATE TABLE IF NOT EXISTS `Puzzle`
(
    `puzzle_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `image_id`  INTEGER                           NOT NULL,
    `start`     INTEGER,
    `end`       INTEGER,
    `success`   INTEGER                           NOT NULL,
    FOREIGN KEY (`image_id`) REFERENCES `Image` (`image_id`) ON UPDATE NO ACTION ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS `index_Puzzle_image_id` ON `Puzzle` (`image_id`);
CREATE INDEX IF NOT EXISTS `index_Puzzle_start` ON `Puzzle` (`start`);
CREATE INDEX IF NOT EXISTS `index_Puzzle_end` ON `Puzzle` (`end`);
CREATE INDEX IF NOT EXISTS `index_Puzzle_success` ON `Puzzle` (`success`);
CREATE TABLE IF NOT EXISTS `Task`
(
    `task_id`   INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `puzzle_id` INTEGER                           NOT NULL,
    `start`     INTEGER,
    `end`       INTEGER,
    `success`   INTEGER                           NOT NULL,
    `block`     INTEGER                           NOT NULL,
    FOREIGN KEY (`puzzle_id`) REFERENCES `Puzzle` (`puzzle_id`) ON UPDATE NO ACTION ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS `index_Task_puzzle_id` ON `Task` (`puzzle_id`);
CREATE INDEX IF NOT EXISTS `index_Task_start` ON `Task` (`start`);
CREATE INDEX IF NOT EXISTS `index_Task_end` ON `Task` (`end`);
CREATE INDEX IF NOT EXISTS `index_Task_success` ON `Task` (`success`);
CREATE INDEX IF NOT EXISTS `index_Task_block` ON `Task` (`block`);