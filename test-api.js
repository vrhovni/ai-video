// API连接测试脚本
const API_BASE_URL = 'http://localhost:8080';

// 测试函数
async function testApiConnection() {
  console.log('🧪 开始测试API连接...\n');

  try {
    // 测试健康检查
    console.log('1. 测试健康检查...');
    const healthResponse = await fetch(`${API_BASE_URL}/health`);
    const healthData = await healthResponse.json();
    console.log('✅ 健康检查成功:', healthData);

    // 测试API信息
    console.log('\n2. 测试API信息...');
    const apiInfoResponse = await fetch(`${API_BASE_URL}`);
    const apiInfoData = await apiInfoResponse.json();
    console.log('✅ API信息获取成功:', apiInfoData);

    // 测试获取公开视频
    console.log('\n3测试获取公开视频...');
    const videosResponse = await fetch(`${API_BASE_URL}/videos/public?page=0&size=5`);
    const videosData = await videosResponse.json();
    console.log('✅ 视频列表获取成功:', {
      totalElements: videosData.totalElements,
      totalPages: videosData.totalPages,
      content: videosData.content?.length || 0    });

    // 测试获取热门视频
    console.log('\n4测试获取热门视频...');
    const popularResponse = await fetch(`${API_BASE_URL}/videos/popular?page=0&size=5`);
    const popularData = await popularResponse.json();
    console.log('✅ 热门视频获取成功:', {
      totalElements: popularData.totalElements,
      totalPages: popularData.totalPages,
      content: popularData.content?.length || 0    });

    // 测试获取最新视频
    console.log('\n5测试获取最新视频...');
    const latestResponse = await fetch(`${API_BASE_URL}/videos/latest?page=0&size=5`);
    const latestData = await latestResponse.json();
    console.log('✅ 最新视频获取成功:', {
      totalElements: latestData.totalElements,
      totalPages: latestData.totalPages,
      content: latestData.content?.length || 0    });

    // 测试获取推荐视频
    console.log('\n6测试获取推荐视频...');
    const recommendationsResponse = await fetch(`${API_BASE_URL}/recommendations/popular?limit=5`);
    const recommendationsData = await recommendationsResponse.json();
    console.log('✅ 推荐视频获取成功:', recommendationsData.length || 0);

    console.log('\n🎉 所有API测试通过！前后端连接正常。');

  } catch (error) {
    console.error('\n❌ API测试失败:', error.message);
    console.log('\n请检查：');
    console.log('1后端服务器是否正在运行');
    console.log('2.后端服务器端口是否为880');
    console.log('3否正常');
    console.log('4CORS设置是否正确');
  }
}

// 运行测试
testApiConnection(); 