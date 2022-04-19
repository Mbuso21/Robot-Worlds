CREATE TABLE world (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  name TEXT UNIQUE NOT NULL,
  size INT NOT NULL,
  attributes TEXT NOT NULL
);

CREATE TABLE `obstacle` (
  `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
  `size` INT NOT NULL,
  `position` TEXT UNIQUE NOT NULL,
  `world_id` INT NOT NULL,
   FOREIGN KEY (`world_id`) REFERENCES `world` (`id`)
   ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `pit` (
  `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
  `size` INT NOT NULL,
  `position` TEXT UNIQUE NOT NULL,
  `world_id` INT NOT NULL,
   FOREIGN KEY (`world_id`) REFERENCES `world` (`id`)
   ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `mine` (
  `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
  `size` INT NOT NULL,
  `position` TEXT UNIQUE NOT NULL,
  `world_id` INT NOT NULL,
   FOREIGN KEY (`world_id`) REFERENCES `world` (`id`)
   ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `robot` (
  `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
  `name` TEXT UNIQUE NOT NULL,
  `position` TEXT UNIQUE NOT NULL,
  `direction` TEXT NOT NULL,
  `world_id` INT NOT NULL,
   FOREIGN KEY (`world_id`) REFERENCES `world` (`id`)
   ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `server` (
  `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
  `ip` TEXT UNIQUE NOT NULL,
  `name` TEXT UNIQUE NOT NULL,
  `port` INT NOT NULL
);

