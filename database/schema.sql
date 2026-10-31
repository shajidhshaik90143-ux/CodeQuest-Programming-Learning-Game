CREATE DATABASE IF NOT EXISTS codequest_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE codequest_db;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(40) NOT NULL UNIQUE,
  email VARCHAR(120) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  xp INT NOT NULL DEFAULT 0,
  level INT NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS admins (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(40) NOT NULL UNIQUE,
  email VARCHAR(120) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS missions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(150) NOT NULL,
  description TEXT NOT NULL,
  category VARCHAR(30) NOT NULL,
  difficulty ENUM('Easy','Medium','Hard') NOT NULL,
  option_a VARCHAR(255) NOT NULL,
  option_b VARCHAR(255) NOT NULL,
  option_c VARCHAR(255) NOT NULL,
  option_d VARCHAR(255) NOT NULL,
  correct_answer CHAR(1) NOT NULL,
  xp_reward INT NOT NULL DEFAULT 10,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT chk_correct_answer CHECK (correct_answer IN ('A','B','C','D')),
  CONSTRAINT chk_xp CHECK (xp_reward BETWEEN 1 AND 500)
);

CREATE TABLE IF NOT EXISTS mission_submissions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  mission_id BIGINT NOT NULL,
  selected_answer CHAR(1) NOT NULL,
  is_correct BOOLEAN NOT NULL,
  submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_submission_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_submission_mission FOREIGN KEY (mission_id) REFERENCES missions(id) ON DELETE CASCADE,
  CONSTRAINT uq_user_mission UNIQUE (user_id, mission_id)
);

INSERT INTO missions(title,description,category,difficulty,option_a,option_b,option_c,option_d,correct_answer,xp_reward)
SELECT 'Java: Entry point','Which method is the entry point of a standard Java application?','Java','Easy','start()','main()','run()','init()','B',10
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='Java: Entry point');
INSERT INTO missions(title,description,category,difficulty,option_a,option_b,option_c,option_d,correct_answer,xp_reward)
SELECT 'SQL: Read rows','Which SQL command is used to retrieve rows from a table?','SQL','Easy','GET','OPEN','SELECT','FETCH ALL','C',10
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='SQL: Read rows');
INSERT INTO missions(title,description,category,difficulty,option_a,option_b,option_c,option_d,correct_answer,xp_reward)
SELECT 'DSA: FIFO structure','Which data structure follows FIFO order?','DSA','Easy','Stack','Queue','Tree','Graph','B',10
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='DSA: FIFO structure');
