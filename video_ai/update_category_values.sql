-- 更新视频分类值，使其与Java枚举匹配
USE video_recommend;

-- 更新category字段值，使其与Java枚举匹配
UPDATE videos SET category = 'TECHNOLOGY' WHERE category = 'TECHNOLOGY';
UPDATE videos SET category = 'MUSIC' WHERE category = 'MUSIC';
UPDATE videos SET category = 'ENTERTAINMENT' WHERE category = 'ENTERTAINMENT';
UPDATE videos SET category = 'FOOD' WHERE category = 'FOOD';
UPDATE videos SET category = 'TRAVEL' WHERE category = 'TRAVEL';
UPDATE videos SET category = 'SPORTS' WHERE category = 'SPORTS';
UPDATE videos SET category = 'NEWS' WHERE category = 'NEWS';
UPDATE videos SET category = 'FASHION' WHERE category = 'FASHION';
UPDATE videos SET category = 'LIFESTYLE' WHERE category = 'LIFESTYLE';

-- 显示更新后的结果
SELECT id, title, category FROM videos ORDER BY id; 