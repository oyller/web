<template>
  <div class="playground">
    <!-- 游戏区域 -->
    <div class="game-container">
      <!-- 新增地图装饰 -->
      <div class="nature-decorations">
        <!-- 左上角云朵 -->
        <div class="floating-cloud cloud1">☁️</div>
        <!-- 右上角太阳 -->
        <div class="rotating-sun">☀️</div>
        <!-- 左下角石头 -->
        <div class="rock-group">
          <div class="rock">🪨</div>
          <div class="rock small">🪨</div>
        </div>
        <!-- 右下角花朵 -->
        <div class="flower-patch">🌸🌸<br>🌸</div>
        <!-- 中间边缘飞鸟 -->
        <div class="flying-bird">🕊️</div>
      </div>

      <div class="map-decoration left">
        <div class="decoration-item">🐇</div>
        <div class="decoration-item">🌼</div>
      </div>

      <!-- 新增环境装饰 -->
      <div class="grass-patch top-left"></div>
      <div class="grass-patch bottom-right"></div>
      <div class="ice-lake left"></div>
      <div class="ice-lake right"></div>
      <div class="floating-leaf"></div>

      <GameMap></GameMap>
      <div class="map-decoration right">
        <div class="decoration-item">🦔</div>
        <div class="decoration-item">🍄</div>
      </div>
    </div>

    <!-- 右侧柱子区域 -->
    <div class="columns">
      <div class="decorations">
        <div class="sparkle"></div>
        <div class="bubble"></div>
        <div class="light-beam"></div>
      </div>

      <!-- 玩家1区域 -->
      <div class="column-container">
        <div class="column-header">
          <span class="snake-title">玩家1 🐍</span>
          <span class="status-tag alive">Alive</span>
        </div>
        <div class="column player1">
          <div class="eye-container">
            <div class="eye left-eye"></div>
            <div class="eye right-eye"></div>
          </div>
          <div class="column-deco"></div>
        </div>
        <span v-if="$store.state.pk.owner_snake_id === 0 && $store.state.record.is_record === false" class="player-label player1-label">OWNER</span>
        <span v-if="$store.state.pk.owner_snake_id === 1 && $store.state.record.is_record === false">ENEMY</span>
      </div>

      <!-- 玩家2区域 -->
      <div class="column-container">
        <div class="column-header">
          <span class="snake-title">玩家2 🐉</span>
          <span class="status-tag warning">Warning</span>
        </div>
        <div class="column player2">
          <div class="eye-container">
            <div class="eye left-eye"></div>
            <div class="eye right-eye"></div>
          </div>
          <div class="column-deco"></div>
        </div>
        <span v-if="$store.state.pk.owner_snake_id === 1 && $store.state.record.is_record === false" class="player-label player2-label">OWNER</span>
        <span v-if="$store.state.pk.owner_snake_id === 0 && $store.state.record.is_record === false" class="player-label player2-label">ENEMY</span>

        <!-- 方向键容器 -->
        <div class="direction-container" v-if = "$store.state.record.is_record === false">
          <div class="control-tip">使用方向键控制移动</div>
          <div class="direction-button w" @click="click_w">
            W
            <span class="key-hint">↑</span>
          </div>
          <div class="direction-row">
            <div class="direction-button a" @click="click_a">
              A
              <span class="key-hint">←</span>
            </div>
            <div class="direction-button s" @click="click_s">
              S
              <span class="key-hint">↓</span>
            </div>
            <div class="direction-button d" @click="click_d">
              D
              <span class="key-hint">→</span>
            </div>
          </div>
          <div class="control-tip bottom">点击或键盘操作</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import GameMap from './GameMap.vue';
import store from '../store/index'

export default {
  components: {
    GameMap,
  },
  setup(){ 
      const click_w = () =>{
           store.state.pk.game.set_direction(0);
      }
      const click_a = ()=>{
           store.state.pk.game.set_direction(3);
      }
      const click_s = ()=>{
        store.state.pk.game.set_direction(2);
      }
      const click_d = ()=>{
        store.state.pk.game.set_direction(1);
     }

      return {
         click_w,
         click_a,
         click_s,
         click_d
      };
  }
}
</script>


<style scoped>
/* 原有样式全部保留 */
div.playground {
  width: 80vw;
  height: 80vh;
  margin: 40px auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  background: linear-gradient(to right, #9AE7A3, #7BCC78);
  position: relative;
  overflow: hidden;
}

div.game-container {
  width: 85%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 15px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
}

.map-decoration {
  position: absolute;
  top: 0;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 20px;
  z-index: 2;
  pointer-events: none;
}

.map-decoration.left {
  left: 10px;
  align-items: flex-start;
}

.map-decoration.right {
  right: 10px;
  align-items: flex-end;
}

.decoration-item {
  font-size: 2.5em;
  opacity: 0.7;
  animation: float 3s ease-in-out infinite;
}

.snake-title {
  font-size: 1.2em;
  font-weight: bold;
  color: white;
  text-shadow: 1px 1px 3px rgba(0,0,0,0.3);
}
.status-tag {
  padding: 4px 10px;
  border-radius: 15px;
  font-size: 0.9em;
  color: white;
}
.status-tag.alive {
  background: rgba(46, 204, 113, 0.8);
}
.status-tag.warning {
  background: rgba(231, 76, 60, 0.8);
}


.columns {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  margin-left: 30px;
  padding: 30px 15px;
}

.column-container {
  display: flex;
  align-items: center;
  margin-bottom: 40px;
  position: relative;
}

div.column {
  width: 120px;
  height: 35px;
  border-radius: 20px;
  margin-right: 15px;
  position: relative;
}

.column-header {
  position: absolute;
  top: -40px;
  left: 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

div.player1 {
  background: linear-gradient(to right, #6A9FE8, #4876EC);
}

div.player2 {
  background: linear-gradient(to right, #FF6E6E, #F94848);
}

.eye-container {
  position: absolute;
  top: 5px;
  left: 8px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 100%;
  padding: 0 10px;
}

.eye {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background-color: white;
  border: 2px solid #333;
  margin-bottom: 4px;
  animation: blink 2s infinite;
}

.player-label {
  font-size: 1.3em;
  font-weight: bold;
  color: white;
  text-align: left;
  background-color: rgba(0, 0, 0, 0.5);
  padding: 7px 12px;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}

.player1-label {
  background-color: rgba(72, 118, 236, 0.7);
}

.player2-label {
  background-color: rgba(249, 72, 72, 0.7);
}

.direction-container {
  width: 160px;
  height: 120px;
  background-color: #4876EC;
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: absolute;
  top: 120px;
  left: 50%;
  transform: translateX(-50%);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.direction-row {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 5px;
}

.direction-button {
  background-color: #FF9E9E;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 1.5em;
  font-weight: bold;
  margin: 5px;
  color: white;
  cursor: pointer;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  transition: all 0.2s;
}

.direction-button:hover {
  background-color: #FF8C8C;
  transform: scale(1.1);
}

.direction-button.w {
  background-color: #78C5F6;
}

.direction-button.a {
  background-color: #99F5A6;
}

.direction-button.s {
  background-color: #FFB6B6;
}

.direction-button.d {
  background-color: #FFCC7F;
}

/* 新增装饰样式 */
.decorations {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.control-tip {
  position: absolute;
  width: 100%;
  text-align: center;
  color: rgba(255,255,255,0.9);
  font-size: 0.9em;
}
.control-tip.bottom {
  bottom: -25px;
}
.control-tip:not(.bottom) {
  top: -25px;
}


.sparkle {
  position: absolute;
  width: 8px;
  height: 8px;
  background: rgba(255,255,255,0.8);
  border-radius: 50%;
  animation: sparkle 2s infinite;
  top: 20%;
  left: 15%;
}

.bubble {
  position: absolute;
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255,255,255,0.3);
  border-radius: 50%;
  animation: bubble 3s infinite;
  bottom: 30%;
  right: 10%;
}

.light-beam {
  position: absolute;
  width: 120%;
  height: 60px;
  background: linear-gradient(
    90deg,
    rgba(255,255,255,0) 0%,
    rgba(255,255,255,0.1) 50%,
    rgba(255,255,255,0) 100%
  );
  transform: rotate(-45deg);
  top: -20%;
  left: -20%;
  animation: beam 8s infinite;
}

.column-deco {
  position: absolute;
  width: 100%;
  height: 100%;
  background: repeating-linear-gradient(
    45deg,
    rgba(255,255,255,0.1) 0px,
    rgba(255,255,255,0.1) 3px,
    transparent 3px,
    transparent 6px
  );
  border-radius: 20px;
}

.key-hint {
  position: absolute;
  bottom: -18px;
  font-size: 0.8em;
  opacity: 0.8;
}

/* 草地斑块 */
.grass-patch {
  position: absolute;
  z-index: 1;
  background: repeating-linear-gradient(
    45deg,
    rgba(113, 188, 120, 0.3) 0px,
    rgba(113, 188, 120, 0.3) 10px,
    rgba(97, 163, 103, 0.3) 10px,
    rgba(97, 163, 103, 0.3) 20px
  );
  border-radius: 15px;
  animation: sway 6s ease-in-out infinite;
}
.grass-patch.top-left {
  top: 10%;
  left: 1%;
  width: 120px;
  height: 80px;
  transform: rotate(-10deg);
}
.grass-patch.bottom-right {
  bottom: 15%;
  right: 25%;
  width: 150px;
  height: 100px;
  transform: rotate(5deg);
}

/* 冰湖效果 */
.ice-lake {
  position: absolute;
  z-index: 1;
  background: rgba(173, 216, 230, 0.2);
  border-radius: 50%;
  box-shadow: 0 0 20px rgba(173, 216, 230, 0.3);
  animation: ripple 4s infinite;
}
.ice-lake::after {
  content: "";
  position: absolute;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    45deg,
    transparent 50%,
    rgba(255,255,255,0.1) 50%
  );
  border-radius: 50%;
}
.ice-lake.left {
  top: 30%;
  left: 2%;
  width: 80px;
  height: 80px;
}
.ice-lake.right {
  bottom: 20%;
  right: 30%;
  top:40%;
  width: 60px;
  height: 60px;
}

/* 调整原有元素位置确保不重叠 */
.map-decoration.left {
  left: 2%;
}
.map-decoration.right {
  right: 2%;
}
.grass-patch.top-left {
  top: 18%;
  left: 8%;
}
.ice-lake.left {
  top: 35%;
  left: 5%;
}

/* 漂浮的落叶 */
.floating-leaf {
  position: absolute;
  width: 25px;
  height: 25px;
  background: rgba(144, 238, 144, 0.6);
  clip-path: polygon(50% 0%, 0% 100%, 100% 100%);
  animation: float 8s ease-in-out infinite;
  z-index: 2;
  right:30%;
}

.nature-decorations {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 2;
}

/* 飘动云朵 */
.floating-cloud {
  position: absolute;
  font-size: 3em;
  opacity: 0.8;
  animation: cloudFloat 12s linear infinite;
}
.cloud1 {
  top: 8%;
  left: 15%;
  animation-delay: 0s;
}

/* 旋转太阳 */
.rotating-sun {
  position: absolute;
  font-size: 2.5em;
  top: 5%;
  right: 8%;
  animation: sunRotate 8s linear infinite;
}

/* 石头组 */
.rock-group {
  position: absolute;
  bottom: 10%;
  left: 3%;
  transform: rotate(-15deg);
}
.rock {
  font-size: 2em;
  filter: grayscale(0.3);
}
.rock.small {
  font-size: 1.2em;
  margin-left: 1.5em;
  margin-top: -0.8em;
}

/* 花丛 */
.flower-patch {
  position: absolute;
  bottom: 12%;
  right: 4%;
  font-size: 1.2em;
  line-height: 0.8;
  text-align: center;
  animation: flowerBlink 3s ease-in-out infinite;
}

/* 飞鸟 */
.flying-bird {
  position: absolute;
  font-size: 1.5em;
  top: 30%;
  right: 15%;
  animation: birdFly 5s cubic-bezier(0.4,0,0.6,1) infinite;
}

/* 新增动画 */
@keyframes cloudFloat {
  0% { transform: translateX(0) scale(1); opacity: 0.8; }
  50% { transform: translateX(50px) scale(1.1); opacity: 1; }
  100% { transform: translateX(0) scale(1); opacity: 0.8; }
}

@keyframes sunRotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

@keyframes flowerBlink {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.05); }
}

@keyframes birdFly {
  0% { transform: translate(0,0) rotate(0deg); }
  25% { transform: translate(100px,20px) rotate(15deg); }
  50% { transform: translate(200px,-10px) rotate(-10deg); }
  75% { transform: translate(300px,20px) rotate(5deg); }
  100% { transform: translate(400px,0) rotate(0deg); }
}

/* 新增动画 */
@keyframes sway {
  0%, 100% { transform: rotate(-10deg) scale(0.95); }
  50% { transform: rotate(5deg) scale(1.05); }
}

@keyframes ripple {
  0% { box-shadow: 0 0 0 0 rgba(173, 216, 230, 0.3); }
  100% { box-shadow: 0 0 0 15px rgba(173, 216, 230, 0); }
}

@keyframes float {
  0% {
    transform: translate(0, 0) rotate(0deg);
    opacity: 0.8;
  }
  25% {
    transform: translate(20px, 30px) rotate(90deg);
  }
  50% {
    transform: translate(40px, 10px) rotate(180deg);
    opacity: 1;
  }
  75% {
    transform: translate(10px, 40px) rotate(270deg);
  }
  100% {
    transform: translate(0, 0) rotate(360deg);
    opacity: 0.8;
  }
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}



/* 新增动画 */
@keyframes sparkle {
  0% { opacity: 0; transform: scale(0); }
  50% { opacity: 1; transform: scale(1); }
  100% { opacity: 0; transform: scale(1.5); }
}

@keyframes bubble {
  0% { transform: translateY(0) scale(0.8); }
  50% { transform: translateY(-15px) scale(1); }
  100% { transform: translateY(0) scale(0.8); }
}

@keyframes beam {
  0% { left: -20%; }
  100% { left: 120%; }
}

@keyframes blink {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(0.8); }
}



</style>