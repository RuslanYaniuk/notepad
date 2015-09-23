/* Create initial schema for MySql 5 */

CREATE TABLE `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(191) NOT NULL,
  `first_name` VARCHAR(255) NOT NULL,
  `last_name` VARCHAR(255) NOT NULL,
  `email` VARCHAR(191) NOT NULL,
  `password` VARCHAR(60) NOT NULL,
  `enabled` TINYINT(1) NOT NULL,
  `registration_date_utc` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT UNIQUE `login_uq` (`login`),
  CONSTRAINT UNIQUE `email_uq` (`email`)
);

CREATE TABLE `user_roles` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `role` VARCHAR(191) NOT NULL,
  CONSTRAINT UNIQUE `role_uq` (`role`),
  PRIMARY KEY (`id`)
);

CREATE TABLE `users_to_roles` (
  `user_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL
);

ALTER TABLE users_to_roles ADD CONSTRAINT FK__users_to_roles__user_roles FOREIGN KEY (`role_id`) REFERENCES user_roles (`id`);

ALTER TABLE users_to_roles ADD CONSTRAINT FK__users_to_roles__users FOREIGN KEY (`user_id`) REFERENCES users (`id`);
