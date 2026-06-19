<template>
  <div class="home">
    <div class="hero">
      <h1>欢迎来到 Fishfrineds 钓鱼社区</h1>
      <p>分享钓点、鱼获、生活，结交志同道合的钓友</p>
    </div>
    <div class="features">
      <div class="feature-card" @click="navigateTo('/spots')" tabindex="0" @keydown.enter="navigateTo('/spots')" @keydown.space="navigateTo('/spots')">
        <h3>钓点信息</h3>
        <p>发现和分享优质钓鱼地点</p>
      </div>
      <div class="feature-card" @click="navigateTo('/catches')" tabindex="0" @keydown.enter="navigateTo('/catches')" @keydown.space="navigateTo('/catches')">
        <h3>鱼获分享</h3>
        <p>晒出你的钓鱼战绩</p>
      </div>
      <div class="feature-card" @click="navigateTo('/life')" tabindex="0" @keydown.enter="navigateTo('/life')" @keydown.space="navigateTo('/life')">
        <h3>生活分享</h3>
        <p>分享你的钓鱼生活</p>
      </div>
      <div class="feature-card" @click="navigateTo('/friends')" tabindex="0" @keydown.enter="navigateTo('/friends')" @keydown.space="navigateTo('/friends')">
        <h3>钓友圈</h3>
        <p>结识更多钓鱼爱好者</p>
      </div>
      <div class="feature-card" @click="navigateTo('/gear')" tabindex="0" @keydown.enter="navigateTo('/gear')" @keydown.space="navigateTo('/gear')">
        <h3>钓具交易</h3>
        <p>买卖二手钓具装备</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const navigateTo = (path) => {
  router.push(path)
}
</script>

<style scoped>
.hero {
  text-align: center;
  padding: 60px 20px;
  background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
  color: #fff;
  border-radius: 16px;
  margin-bottom: 32px;
}

.hero h1 {
  font-size: 32px;
  margin-bottom: 12px;
}

.hero p {
  font-size: 16px;
  opacity: 0.9;
}

.features {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 20px;
}

.feature-card {
  background: #fff;
  padding: 28px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: transform 0.2s, box-shadow 0.2s;
  cursor: pointer;
}

.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.feature-card:focus {
  outline: 2px solid #2563eb;
  outline-offset: 2px;
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.feature-card h3 {
  color: #2563eb;
  margin-bottom: 8px;
  font-size: 18px;
}

.feature-card p {
  color: #666;
  font-size: 14px;
}
</style>
