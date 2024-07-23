CREATE DATABASE IF NOT EXISTS `user_database`;

USE `user_database`;

-- Drop the role_permissions table first to avoid foreign key constraint error
DROP TABLE IF EXISTS `roles_permissions`;

-- Drop the user_roles table next to avoid foreign key constraint error
DROP TABLE IF EXISTS `user_roles`;

-- Now drop the roles table
DROP TABLE IF EXISTS `roles`;

-- Drop the permission table
DROP TABLE IF EXISTS `permission`;

-- Drop the user table
DROP TABLE IF EXISTS `user`;

-- Drop the invalidated_token table
DROP TABLE IF EXISTS `invalidated_token`;

-- Create the user table
CREATE TABLE `user` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(255) NULL unique collate utf8mb4_unicode_ci,
    `password` VARCHAR(255) NULL,
    `firstname` VARCHAR(255) NULL,
    `lastname` VARCHAR(255) NULL,
    `dob` DATE  NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Create the roles table
CREATE TABLE roles (
    name VARCHAR(255) PRIMARY KEY,
    description TEXT
);

-- Create the permission table
CREATE TABLE permission (
    name VARCHAR(255) PRIMARY KEY,
    description TEXT
);

-- Create the user_roles table to establish the many-to-many relationship between user and roles
CREATE TABLE user_roles (
    user_id INT NOT NULL,
    roles_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, roles_name)
);

-- Create the role_permissions table to establish the many-to-many relationship between roles and permission
CREATE TABLE roles_permissions (
    role_name VARCHAR(255) NOT NULL,
    permission_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (role_name, permission_name)
);

-- Create the invalidated_token table
CREATE TABLE invalidated_token (
    id VARCHAR(255) PRIMARY KEY,
    expiry_time DATETIME
);

-- Add foreign key constraints to user_roles table
ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE;
ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_roles FOREIGN KEY (roles_name) REFERENCES roles(name) ON DELETE CASCADE;

-- -- Add foreign key constraints to role_permissions table
ALTER TABLE roles_permissions ADD CONSTRAINT fk_roles_permissions_roles FOREIGN KEY (role_name) REFERENCES roles(name) ON DELETE CASCADE;
ALTER TABLE roles_permissions ADD CONSTRAINT fk_roles_permissions_permission FOREIGN KEY (permission_name) REFERENCES permission(name) ON DELETE CASCADE;
