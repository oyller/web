<template>
    <div class="game-result">
      <!-- 装饰元素 -->
      <div class="decorations">
        <div class="star star1">⭐</div>
        <div class="star star2">🌟</div>
        <div class="confetti"></div>
        <div class="confetti"></div>
        <div class="confetti"></div>
      </div>
      
      <!-- 主要内容 -->
      <div class="result-content">
        <div class="result-message">
          {{ ($store.state.pk.loser != $store.state.pk.play_id && $store.state.pk.loser !== "all") ? '🎉 大胜利！' : '😢 再接再厉～' }}
          <div class="bounce-emoji">{{ isVictory ? '🏆' : '🌸' }}</div>
        </div>
        <div class="button-container">
          <button @click="nextRound" class="next-button">
            🎮 再来一局
            <div class="button-shadow"></div>
          </button>
          <button @click="goToHomePage" class="home-button">
            🏠 回到主页
            <div class="button-shadow"></div>
          </button>
        </div>
      </div>
  
      <!-- 小动物装饰 -->
      <div class="animal-deco">
        <div class="cat">🐱</div>
        <div class="rabbit">🐰</div>
      </div>
    </div>
  </template>
  
  <script>

import router from '../router/index'
import store from '../store/index'

export default {

     setup() {
    //  const route = useRoute();
      const nextRound = () =>{ 
        // 下一局逻辑
           store.commit("clear_end");
           store.commit("updateStatus", "matching");
           router.push({name:'pk_index'});
      }
      const goToHomePage = () =>{ 
          router.push({name:"userbot_index"});
      }
      return {
        nextRound,
        goToHomePage
      }
    }
  };
  </script>
  
  <style scoped>
  .game-result {
    background: linear-gradient(135deg, #ff9a9e 0%, #fad0c4 100%);
    width: 350px;
    height: 250px;
    border-radius: 20px;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    box-shadow: 0 10px 20px rgba(0,0,0,0.1);
    overflow: hidden;
    font-family: 'Comic Sans MS', cursive;
  }
  
  .result-content {
    position: relative;
    z-index: 2;
  }
  
  .result-message {
    font-size: 28px;
    color: #ffffff;
    text-align: center;
    text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
    padding: 20px 0;
    animation: bounce 1s ease infinite;
  }
  
  .bounce-emoji {
    font-size: 50px;
    margin-top: 10px;
    animation: float 2s ease-in-out infinite;
  }
  
  .button-container {
    display: flex;
    justify-content: center;
    gap: 15px;
    margin-top: 20px;
  }
  
  .next-button, .home-button {
    padding: 12px 25px;
    border: none;
    border-radius: 25px;
    cursor: pointer;
    font-size: 16px;
    position: relative;
    transition: all 0.3s ease;
    transform-style: preserve-3d;
  }
  
  .next-button {
    background: #ff6b6b;
    color: white;
  }
  
  .home-button {
    background: #4ecdc4;
    color: white;
  }
  
  .button-shadow {
    position: absolute;
    width: 90%;
    height: 10px;
    background: rgba(0,0,0,0.1);
    bottom: -8px;
    left: 5%;
    border-radius: 50%;
    filter: blur(3px);
  }
  
  button:hover {
    transform: translateY(-3px);
  }
  
  button:active {
    transform: translateY(1px);
  }
  
  /* 装饰元素样式 */
  .decorations {
    position: absolute;
    width: 100%;
    height: 100%;
    pointer-events: none;
  }
  
  .star {
    position: absolute;
    font-size: 24px;
    animation: spin 2s linear infinite;
  }
  
  .star1 { top: 10px; left: 20px; }
  .star2 { bottom: 10px; right: 20px; }
  
  .confetti {
    position: absolute;
    width: 8px;
    height: 8px;
    background: #ffd700;
    animation: fall 3s linear infinite;
  }
  
  .confetti:nth-child(3) { left: 10%; animation-delay: 0.5s; background: #ff6b6b; }
  .confetti:nth-child(4) { left: 30%; animation-delay: 1.2s; background: #4ecdc4; }
  .confetti:nth-child(5) { left: 70%; animation-delay: 0.8s; background: #ffd700; }
  
  .animal-deco {
    position: absolute;
    font-size: 40px;
  }
  
  .cat {
    position: absolute;
    bottom: -10px;
    left: -20px;
    animation: sway 3s ease-in-out infinite;
  }
  
  .rabbit {
    position: absolute;
    top: -10px;
    right: -20px;
    animation: sway 2.5s ease-in-out infinite;
  }
  
  /* 动画 */
  @keyframes bounce {
    0%, 100% { transform: translateY(0); }
    50% { transform: translateY(-5px); }
  }
  
  @keyframes float {
    0%, 100% { transform: translateY(0); }
    50% { transform: translateY(-10px); }
  }
  
  @keyframes spin {
    from { transform: rotate(0deg); }
    to { transform: rotate(360deg); }
  }
  
  @keyframes fall {
    0% { transform: translateY(-100vh) rotate(0deg); }
    100% { transform: translateY(100vh) rotate(360deg); }
  }
  
  @keyframes sway {
    0%, 100% { transform: translateX(0) rotate(-10deg); }
    50% { transform: translateX(10px) rotate(10deg); }
  }
  
  @media (max-width: 768px) {
    .game-result {
      width: 90%;
      height: auto;
      padding: 20px 0;
    }
    
    .button-container {
      flex-direction: column;
      align-items: center;
    }
  }
  </style>