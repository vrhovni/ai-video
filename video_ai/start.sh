#!/bin/bash

echo "========================================"
echo "视频推荐系统启动脚本"
echo "========================================"
echo

echo "正在检查Java环境..."
if ! command -v java &> /dev/null; then
    echo "错误：未找到Java环境，请先安装JDK 11+"
    exit 1
fi

java -version

echo
echo "正在检查Maven环境..."
if ! command -v mvn &> /dev/null; then
    echo "错误：未找到Maven环境，请先安装Maven 3.6+"
    exit 1
fi

mvn -version

echo
echo "正在启动视频推荐系统..."
echo "请确保MySQL数据库已启动，并创建了video_recommend数据库"
echo

mvn spring-boot:run

echo
echo "应用已停止" 