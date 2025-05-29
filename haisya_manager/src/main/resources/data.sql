/* adminsテーブル */
INSERT IGNORE INTO admins (id, username, password, role, enabled) VALUES
(1, 'haisya', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 'ROLE_ADMIN', true),
(2, 'manager', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 'ROLE_ADMIN', true);

/* teams テーブル */
INSERT IGNORE INTO teams (id, name, admin_id) VALUES
(1, 'ドルフィンズ', 1),
(2, 'コンドルズ', 2);

/* members テーブル */
INSERT IGNORE INTO members (id, name, admin_id, team_id) VALUES
(1, '鈴木', 1, 1),
(2, '佐藤', 1, 1),
(3, '田中', 1, 1),
(4, '寺田', 1, 1),
(5, '栗原', 1, 1),
(6, '中山', 1, 1),
(7, '今田', 1, 1),
(8, '牧原', 1, 1),
(9, '菅原', 1, 1),
(10, '石野', 1, 1),
(11, '石田', 1, 1),
(12, '今宮', 2, 2),
(13, '戸崎', 2, 2),
(14, '川田', 2, 2),
(15, '北村', 2, 2),
(16, '浜中', 2, 2),
(17, '小久保', 2, 2),
(18, '松井', 2, 2);

/* children テーブル */
INSERT IGNORE INTO children (id, name, member_id, admin_id) VALUES
(1, '鈴木 ふみやす', 1, 1),
(2, '鈴木 ひさみつ', 1, 1),
(3, '佐藤 しんじ', 2, 1),
(4, '田中 むさし', 3, 1),
(5, '寺田 いちろう', 4, 1),
(6, '寺田 さぶろう', 4, 1),
(7, '寺田 たろう', 4, 1),
(8, '栗原 まさや', 5, 1),
(9, '中山 けん', 6, 1),
(10, '中山 みのる', 6, 1),
(11, '今田 さとし', 7, 1),
(12, '牧原 ゆうじ', 8, 1),
(13, '菅原 あきら', 9, 1),
(14, '石野 たつや', 10, 1),
(15, '石野 じゅん', 10, 1),
(16, '石田 しんご', 11, 1),
(17, '今宮 こう', 12, 2),
(18, '今宮 だいき', 12, 2),
(19, '戸崎 けいた', 13, 2),
(20, '川田 ゆうが', 14, 2),
(21, '北村 こうじ', 15, 2),
(22, '北村 ゆういち', 15, 2),
(23, '浜中 すぐる', 16, 2),
(24, '小久保 しん', 17, 2),
(25, '松井 ひでき', 18, 2),
(26, '松井 だいき', 18, 2);

/* rides テーブル */
INSERT IGNORE INTO rides (id, date, destination, memo, admin_id) VALUES
(1, '2025-05-17', '日大グラウンド', '公式戦', 1),
(2, '2025-05-18', '高瀬グラウンド', '練習試合', 1),
(3, '2025-05-25', '高瀬グラウンド', '練習試合', 2);

/* ride_entries （行けるか行けないかを保存する）テーブル　*/
INSERT IGNORE INTO ride_entries (id, ride_id, member_id, can_drive) VALUES
(1, 1, 1, true),
(2, 1, 2, false),
(3, 1, 3, true),
(4, 1, 4, true),
(5, 1, 5, true),
(6, 1, 6, false),
(7, 1, 7, true),
(8, 1, 8, true),
(9, 1, 9, true),
(10, 1, 10, false),
(11, 1, 11, true),
(12, 2, 1, true),
(13, 2, 2, true),
(14, 2, 3, false),
(15, 2, 4, true),
(16, 2, 5, true),
(17, 2, 6, true),
(18, 2, 7, true),
(19, 2, 8, false),
(20, 2, 9, true),
(21, 2, 10, true),
(22, 2, 11, true),
(23, 3, 12, true),
(24, 3, 13, true),
(25, 3, 14, true),
(26, 3, 15, true),
(27, 3, 16, false),
(28, 3, 17, true),
(29, 3, 18, true);

/* drivers　（配車に指定された運転手の保護者を保存する） テーブル */
INSERT IGNORE INTO drivers (id, ride_id, member_id) VALUES
(1, 1, 1),
(2, 1, 3),
(3, 1, 8),
(4, 1, 9),
(5, 3, 13),
(6, 3, 12),
(7, 3, 14),
(8, 3, 15),
(9, 3, 18);

/* ride_member_entries　（同乗する保護者を保存する） テーブル */
INSERT IGNORE INTO ride_member_entries (id, ride_id, member_id) VALUES
(1, 1, 5),
(2, 1, 7),
(3, 3, 17);

/* ride_child_entries　（配車に指定された人が乗せる子供を保存する） テーブル */
INSERT IGNORE INTO ride_child_entries (id, ride_id, member_id, child_id) VALUES
(1, 1, 1, 1),
(2, 1, 1, 2),
(3, 1, 1, 3),
(4, 1, 1, 4),
(5, 1, 1, 5),
(6, 1, 3, 6),
(7, 1, 3, 7),
(8, 1, 3, 8),
(9, 1, 8, 9),
(10, 1, 8, 10),
(11, 1, 8, 11),
(12, 1, 9, 12),
(13, 1, 9, 13),
(14, 1, 9, 14),
(15, 1, 9, 15),
(16, 1, 9, 16),
(17, 3, 13, 17),
(18, 3, 13, 18),
(19, 3, 12, 19),
(20, 3, 12, 20),
(21, 3, 15, 21),
(22, 3, 15, 22),
(23, 3, 14, 23),
(24, 3, 14, 24),
(25, 3, 18, 25),
(26, 3, 18, 26);