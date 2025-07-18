@echo off
echo 启动AI视频推荐系统前端...
echo.

cd /d %~dp0"
echo 当前目录: %CD%

echo.
echo 安装依赖...
npm install

echo.
echo 启动开发服务器...
npm run serve

pause 