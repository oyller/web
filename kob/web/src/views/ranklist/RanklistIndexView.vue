<template>
  <ContentField>
    <div class="rank-container">

      <div class="anime-bg">
        <div class="star-pattern"></div>
        <div class="sparkle-effect"></div>
        <img src="https://i.328888.xyz/2023/03/06/i5gxM.png" class="anime-character left" alt="角色立绘">
        <img src="https://i.328888.xyz/2023/03/06/i5bWR.png" class="anime-character right" alt="角色立绘">
      </div>

      <div class="rank-header">
        <h1>玩家排行榜</h1>
        <div class="stats">
          <span>当前页: {{ currentPage }} / {{ total_pages }}</span>
        </div>
      </div>

      <div class="player-list">
        <div class="list-header">
          <div class="col-avatar">头像</div>
          <div class="col-name">玩家名称</div>
          <div class="col-rating">战力评分</div>
        </div>

        <div 
          v-for="(player, index) in players" 
          :key="index"
          class="player-item"
        >
          <div class="player-content">
            <div class="col-avatar">
              <img 
                :src="player.photo || 'https://via.placeholder.com/50'" 
                class="avatar"
                @error="handleAvatarError"
              />
            </div>
            <div class="col-name">
              <div class="username">{{ player.username }}</div>
              <div class="rank-tag">#{{ index + 1 }}</div>
            </div>
            <div class="col-rating">
              <div class="rating-value">{{ player.rating }}</div>
              <div class="rating-progress">
                <div 
                  class="progress-bar" 
                  :style="{ width: getRatingProgress(player.rating) + '%' }"
                ></div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="pagination">
        <div class="pagination-controls">
          <button 
            class="page-btn prev"
            @click="changePage('prev')"
            :disabled="currentPage === 1"
          >
            &lt;
          </button>
          
          <div class="page-numbers">
            <button
              v-for="page in pageOptions"
              :key="page"
              class="page-btn"
              :class="{ active: page === currentPage }"
              @click="selectPage(page)"
            >
              {{ page }}
            </button>
          </div>
          
          <button
            class="page-btn next"
            @click="changePage('next')"
            :disabled="currentPage === total_pages"
          >
            &gt;
          </button>
        </div>

        <div class="page-jump">
          <input
            type="number"
            v-model="targetPage"
            :min="1"
            :max="total_pages"
            class="page-input"
            placeholder="页码"
          />
          <button
            class="jump-btn"
            @click="goToPage"
          >
            跳转
          </button>
        </div>
      </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import { ref, reactive, onMounted, computed } from 'vue'
import $ from 'jquery'
import store from '../../store/index'

export default {
  components: {
    ContentField
  },
  setup() {
    let total_pages = reactive(1);
    let players = reactive([]);
    let currentPage = ref(1);
    let targetPage = ref(1);

    const pageOptions = computed(() => {
        const options = [];
        for (let i = -2; i <= 2; i++) {
          const page = i + currentPage.value;
          if (page > 1 && page <= total_pages) {
            options.push(page);
          }
        }
        return options;
    });

    const fetchPlayers = (page = 1) => {
      $.ajax({
        url: "http://127.0.0.1:3000/rank/ranklist_page_get/",
        type: "GET",
        data: { page: page },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success: (resp) => {
          players.splice(0, players.length);
          for (let k of resp.players) players.push(k);
          total_pages = Math.floor(resp.players_count / 10);
          if (resp.players_count % 10 !== 0) total_pages++;
        },
        error: (resp) => {
          console.error(resp);
        },
      });
    }

    onMounted(() => {
      fetchPlayers(currentPage.value);
    });

    const changePage = (direction) => {
      if (direction === 'prev' && currentPage.value > 1) {
        currentPage.value--;
      } else if (direction === 'next' && currentPage.value < total_pages) {
        currentPage.value++;
      }
      fetchPlayers(currentPage.value);
    }

    const selectPage = (page) => {
      currentPage.value = page;
      fetchPlayers(page);
    }

    const goToPage = () => {
      if (targetPage.value >= 1 && targetPage.value <= total_pages) {
        currentPage.value = targetPage.value;
        fetchPlayers(targetPage.value);
      } else {
        currentPage.value = 1;
        fetchPlayers(1);
      }
      targetPage.value = 1;
    }

    const handleAvatarError = (event) => {
      event.target.src = 'https://via.placeholder.com/70'
    }

    const getRatingProgress = (rating) => Math.min((rating / 3000) * 100, 100)

    return {
      players,
      currentPage,
      total_pages,
      handleAvatarError,
      getRatingProgress,
      pageOptions,
      targetPage,
      changePage,
      selectPage,
      goToPage,
    }
  }
}
</script>

<style scoped>
.rank-container {
  max-width: 1200px;
  margin: 2rem auto;
  padding: 2rem;
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.08);
}

.rank-header {
  text-align: center;
  margin-bottom: 2rem;
}

.rank-header h1 {
  font-size: 2.2rem;
  color: #2d3436;
  margin-bottom: 0.5rem;
  letter-spacing: 1px;
}

.stats span {
  color: #636e72;
  font-size: 0.95rem;
}

.player-list {
  margin: 2rem 0;
  border-radius: 12px;
  overflow: hidden;
}

.list-header {
  display: grid;
  grid-template-columns: 80px 1fr 200px;
  padding: 1rem 2rem;
  background: #f8f9fa;
  font-weight: 600;
  color: #6c5ce7;
  border-bottom: 2px solid #f1f3f5;
}

.player-item {
  background: white;
  border-bottom: 1px solid #f1f3f5;
  transition: all 0.2s ease;
}

.player-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(106, 72, 255, 0.1);
}

.player-content {
  display: grid;
  grid-template-columns: 80px 1fr 200px;
  align-items: center;
  padding: 1.5rem 2rem;
  gap: 1.5rem; /* 增加间距 */
}

.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-right: 1rem; /* 头像和名字之间的间距 */
}

.col-name {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.username {
  font-size: 1.2rem; /* 增加字号 */
  font-weight: 600;
  color: #2d3436;
  letter-spacing: 0.5px;
}

.rank-tag {
  background: #f1f3f5;
  color: #6c5ce7;
  padding: 0.3rem 0.8rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 500;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1); /* 添加阴影 */
}

.rating-value {
  font-size: 1.3rem;
  font-weight: 700;
  color: #6c5ce7;
  margin-bottom: 0.5rem;
}

.rating-progress {
  height: 6px;
  background: #e9ecef;
  border-radius: 3px;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  background: linear-gradient(90deg, #6c5ce7, #a363d9);
  transition: width 0.5s ease;
}

.pagination {
  margin-top: 3rem;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  align-items: center;
}

.pagination-controls {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

.page-btn {
  padding: 0.6rem 1.2rem;
  border: none;
  border-radius: 8px;
  background: #f8f9fa;
  color: #6c5ce7;
  cursor: pointer;
  transition: all 0.2s ease;
  font-weight: 500;
}

.page-btn:hover:not(:disabled) {
  background: #6c5ce7;
  color: white;
  transform: translateY(-1px);
}

.page-btn.active {
  background: #6c5ce7;
  color: white;
}

.page-jump {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

.page-input {
  width: 80px;
  padding: 0.6rem;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  text-align: center;
  font-size: 0.9rem;
}

.jump-btn {
  padding: 0.6rem 1.2rem;
  background: #6c5ce7;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.jump-btn:hover {
  background: #5b4bc4;
  transform: translateY(-1px);
}

.rank-container {
  max-width: 1200px;
  margin: 2rem auto;
  padding: 3rem;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 20px;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.15);
  position: relative;
  overflow: hidden;
}

/* 新增二次元背景样式 */
.anime-bg {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  pointer-events: none;
  z-index: 0;
}

.star-pattern {
  position: absolute;
  width: 100%;
  height: 100%;
  background: 
    radial-gradient(circle at 10% 20%, rgba(255,255,255,0.2) 1px, transparent 1px),
    radial-gradient(circle at 90% 80%, rgba(255,255,255,0.2) 1px, transparent 1px);
  background-size: 30px 30px;
  animation: starGlow 2s ease-in-out infinite alternate;
}

.sparkle-effect {
  position: absolute;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, 
    rgba(255,255,255,0) 45%,
    rgba(255,255,255,0.3) 50%,
    rgba(255,255,255,0) 55%
  );
  animation: sparkleRotate 8s linear infinite;
}

.anime-character {
  position: absolute;
  bottom: -50px;
  height: 300px;
  opacity: 0.15;
  filter: drop-shadow(0 0 10px rgba(106, 72, 255, 0.3));
  &.left { left: -80px; }
  &.right { right: -80px; transform: scaleX(-1); }
}

.rank-header h1 {
  font-size: 2.5rem;
  background: linear-gradient(45deg, #6c5ce7, #a4508b);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  text-shadow: 2px 2px 4px rgba(0,0,0,0.1);
  margin-bottom: 1rem;
}

.stats span {
  color: #666;
  font-size: 1rem;
  background: rgba(255,255,255,0.8);
  padding: 0.3rem 1rem;
  border-radius: 20px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.05);
}

.player-list {
  margin: 2rem 0;
  background: rgba(255,255,255,0.9);
  border-radius: 15px;
  backdrop-filter: blur(5px);
  position: relative;
  z-index: 1;
}

.list-header {
  background: linear-gradient(90deg, #6c5ce7 0%, #a363d9 100%);
  color: white;
  padding: 1.2rem 2rem;
  border-radius: 15px 15px 0 0;
  font-size: 1.1rem;
}

.player-item {
  background: rgba(255,255,255,0.95);
  margin: 0 10px;
  border-radius: 12px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.player-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(108, 92, 231, 0.2);
  background: rgba(255,255,255,1);
}

.avatar {
  width: 60px;
  height: 60px;
  border: 3px solid #fff;
  box-shadow: 0 4px 15px rgba(0,0,0,0.15);
}

.username {
  color: #4a5568;
  font-size: 1.3rem;
  font-weight: 700;
}

.rank-tag {
  background: linear-gradient(45deg, #ff6b6b, #ff9f43);
  color: white;
  padding: 0.4rem 1.2rem;
  font-size: 1rem;
  box-shadow: 0 3px 8px rgba(255,107,107,0.3);
}

.rating-value {
  color: #6c5ce7;
  font-size: 1.4rem;
  text-shadow: 1px 1px 2px rgba(0,0,0,0.1);
}

.progress-bar {
  background: linear-gradient(90deg, 
    #6c5ce7 0%,
    #a363d9 50%,
    #ff6b6b 100%
  );
  box-shadow: inset 0 2px 4px rgba(0,0,0,0.1);
}

/* 分页按钮新样式 */
.page-btn {
  background: linear-gradient(145deg, #ffffff, #f0f0f0);
  border: 1px solid #e2e8f0;
  color: #6c5ce7;
  box-shadow: 3px 3px 6px rgba(0,0,0,0.05),
             -2px -2px 4px rgba(255,255,255,0.8);
}

.page-btn:hover:not(:disabled) {
  background: linear-gradient(145deg, #6c5ce7, #5b4bc4);
  color: white;
  box-shadow: 2px 2px 5px rgba(0,0,0,0.1);
}

.jump-btn {
  background: linear-gradient(45deg, #6c5ce7, #a363d9);
  box-shadow: 3px 3px 8px rgba(108,92,231,0.2);
}

/* 新增动画 */
@keyframes sparkleRotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

@keyframes starGlow {
  from { opacity: 0.3; }
  to { opacity: 0.8; }
}


@media (max-width: 768px) {
  .player-content {
    grid-template-columns: 1fr;
    gap: 1rem;
    text-align: center;
  }

  .col-name {
    flex-direction: column;
  }

  .list-header {
    display: none;
  }

  .pagination-controls {
    flex-wrap: wrap;
    justify-content: center;
  }
}
</style>
