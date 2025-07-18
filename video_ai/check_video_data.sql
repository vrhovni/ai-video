-- 检查视频数据状态
USE video_recommend;

-- 检查视频总数
SELECT COUNT(*) as total_videos FROM videos;

-- 检查category字段的值
SELECT DISTINCT category FROM videos;

-- 检查是否有空值或异常数据
SELECT id, title, category, video_url, is_public 
FROM videos 
WHERE category IS NULL 
   OR title IS NULL 
   OR video_url IS NULL 
   OR is_public IS NULL;

-- 检查前10条视频数据
SELECT id, title, category, video_url, is_public, created_at 
FROM videos 
ORDER BY id 
LIMIT 10;

-- 检查是否有重复的category值
SELECT category, COUNT(*) as count 
FROM videos 
GROUP BY category 
ORDER BY count DESC; 