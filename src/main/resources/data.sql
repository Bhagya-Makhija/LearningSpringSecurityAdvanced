INSERT INTO users ( username,email, password, provider_type, is_enabled, created_at)
VALUES
-- pass123
('userA', 'userA@gmail.com', '$2a$10$MBdS1i7SZcAIV3Xv4pAHc.fsbjl5x4iL5ptip1teBmPI/.BmlbOXq', 'LOCAL', true, NOW()),
('userB', 'userB@gmail.com', '$2a$10$MBdS1i7SZcAIV3Xv4pAHc.fsbjl5x4iL5ptip1teBmPI/.BmlbOXq', 'LOCAL', true, NOW());

INSERT INTO user_roles (user_id, role)
VALUES
(1, 'USER'),
(2, 'ADMIN'),
(2, 'USER');

INSERT INTO habits (id, name, description, user_id, is_active, created_at)
VALUES
(1, 'Gym', 'Workout daily', 1, true, NOW()),
(2, 'Study', 'Study 2 hours', 1, true, NOW()),
(3, 'Diet', 'Follow diet plan', 2, true, NOW());

INSERT INTO battles (id, name, description, start_date, end_date, is_active, created_at)
VALUES
(1, 'Fitness Battle', 'Gym consistency challenge', CURRENT_DATE, CURRENT_DATE + 7, true, NOW());

INSERT INTO battle_participants (id, battle_id, user_id, score)
VALUES
(1, 1, 1, 0),
(2, 1, 2, 0);

INSERT INTO daily_progress (id, user_id, habit_id, battle_id, date, completed, points_earned)
VALUES
(1, 1, 1, 1, CURRENT_DATE, true, 10),
(2, 2, 3, 1, CURRENT_DATE, false, 0);

INSERT INTO leaderboard (id, battle_id, user_id, total_score, rank)
VALUES
(1, 1, 1, 10, 1),
(2, 1, 2, 0, 2);