<template>
    <div class="game-container">
        <!-- 自然背景元素 -->
        <div class="leaf-background">
            <div v-for="(leaf, index) in leaves" :key="index" class="leaf" :style="leaf.style"></div>
        </div>

        <!-- 主内容区域 -->
        <div class="game-matching">
            <!-- 玩家信息 -->
            <div class="player-info">
                <div class="avatar-frame">
                    <img class="avatar" :src="playerAvatar" alt="Player Avatar" />
                    <div class="nature-decor"></div>
                </div>
                <div class="name-box">
                    <div class="name">🌿 玩家:{{ playerName }}</div>
                    <div class="status-dot" :class="{ matching: isMatching }"></div>
                </div>
            </div>

            <!-- VS区域 -->
            <div class="vs-container">
                <div class="vs-text">🍃 VS 🍃</div>
                <div class="nature-effect"></div>
                <div class="mode-select">
                    <label for="mode">选择模式：</label>
                    <select id="mode" v-model="selectedMode" @change="handleModeChange">
                        <option value="self">本人亲自出战</option>
                        <option v-for="(bot, index) in bots" :key="index" :value= bot.id>bot:{{ bot.title }} 出战 </option>
                    </select>
                </div>
            </div>

            <!-- 对手信息 -->
            <div class="opponent-info">
                <div class="avatar-frame">
                    <img class="avatar" :src="opponentAvatar" alt="Opponent Avatar" />
                    <div class="bubble">匹配中...</div>
                </div>
                <div class="name-box">
                    <div class="name">🌱 对手:{{ opponentName || '等待中...' }}</div>
                </div>
            </div>
        </div>

        <!-- 操作按钮 -->
        <div class="button-container">
            <button class="match-button" @click="toggleMatchStatus">
                {{ matchButtonText }}
                <div class="button-glow"></div>
            </button>
        </div>
    </div>
</template>
<script>
import { ref, computed, onMounted, reactive } from 'vue';
import { useStore } from 'vuex';
import $ from 'jquery';

export default {
    setup() {
        const store = useStore();

        // 绿叶飘落效果
        const leaves = ref([]);
        const createLeaves = () => {
            const newLeaves = [];
            for (let i = 0; i < 20; i++) {
                newLeaves.push({
                    style: {
                        left: Math.random() * 100 + '%',
                        animationDelay: Math.random() * 5 + 's',
                        rotate: Math.random() * 360 + 'deg'
                    }
                });
            }
            leaves.value = newLeaves;
        };

        onMounted(createLeaves);

        // 用户数据
        const playerName = ref(store.state.user.username);
        const playerAvatar = ref(store.state.user.photo);

        // 对手数据
        const opponentName = computed(() => store.state.pk.opponent_username);
        const opponentAvatar = computed(() => store.state.pk.opponent_photo);
        const isMatching = computed(() => matchStatus.value === 'cancel');

        // 匹配状态
        const matchStatus = ref("start");
        const matchButtonText = computed(() =>
            matchStatus.value === "start" ? "🌳 开始匹配 🌳" : "🍂 取消匹配 🍂"
        );

        // 选择模式
        const selectedMode = ref("self");
        const handleModeChange = () => {
            // 切换模式时自动取消匹配
            if (matchStatus.value === 'cancel') {
                toggleMatchStatus();
            }
        };

        let bots = reactive([]);

        // 刷新Bot列表
        const refresh_bots = () => {
            $.ajax({
                url: "http://127.0.0.1:3000/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    let tmp = [];
                    for (let k of resp) {
                        tmp.push(k);
                    }
                    Object.assign(bots, tmp);
                }
            });
        };

        refresh_bots();

        // 切换匹配状态
        const toggleMatchStatus = () => {
            const eventType = matchStatus.value === "start" ? "start-matching" : "stop-matching";
            store.state.pk.socket.send(JSON.stringify({
                event: eventType,
                bot_id: selectedMode.value === "self" ? -1: selectedMode.value,
            }));
            matchStatus.value = matchStatus.value === "start" ? "cancel" : "start";
        };

        return {
            playerName,
            playerAvatar,
            opponentName,
            opponentAvatar,
            matchButtonText,
            toggleMatchStatus,
            leaves,
            isMatching,
            selectedMode,
            handleModeChange,
            bots
        };
    }
};
</script>

<style scoped>
/* 新增模式选择器样式 */
.mode-select {
    margin-top: 20px;
    position: relative;
    z-index: 5;
    /* 确保在最上层 */
}

.mode-select label {
    font-size: 1.5rem;
    color: #0277BD;
    font-weight: 600;
    margin-right: 10px;
}

.mode-select select {
    font-size: 1.5rem;
    padding: 8px 20px;
    border-radius: 15px;
    border: 2px solid #81D4FA;
    background: #E1F5FE;
    color: #01579B;
    appearance: none;
    cursor: pointer;
    transition: all 0.3s ease;
    width: 280px;
    position: relative;
}

.mode-select select:hover {
    border-color: #4FC3F7;
    box-shadow: 0 0 15px rgba(79, 195, 247, 0.3);
}

.mode-select select:focus {
    outline: none;
    border-color: #029BE5;
    box-shadow: 0 0 20px rgba(2, 119, 189, 0.3);
}

/* 自定义下拉箭头 */
.mode-select::after {
    content: "▼";
    position: absolute;
    right: 15px;
    top: 50%;
    transform: translateY(-50%);
    color: #0277BD;
    pointer-events: none;
    font-size: 1.2rem;
}

/* 下拉选项样式 */
.mode-select option {
    padding: 10px;
    background: #E1F5FE;
    color: #01579B;
    font-size: 1.4rem;
}

.mode-select option:hover {
    background: #B3E5FC !important;
}


/* 新增对手头像位置调整 */
.opponent-info .avatar {
    transform: translateX(44px);
    /* 新增：向右平移12像素 */
}

/* 自然背景动画 */
@keyframes leafFall {
    0% {
        transform: translateY(-100vh) rotate(0deg);
    }

    100% {
        transform: translateY(100vh) rotate(360deg);
    }
}

.leaf-background {
    position: fixed;
    width: 100%;
    height: 100%;
    pointer-events: none;
    overflow: hidden;
    z-index: 1;
}

.leaf {
    position: absolute;
    width: 20px;
    height: 20px;
    background: #89C7C7;
    clip-path: polygon(50% 0%, 0% 100%, 100% 100%);
    animation: leafFall 10s linear infinite;
    opacity: 0.6;
    filter: drop-shadow(0 2px 5px rgba(66, 165, 245, 0.2));
}

/* 主容器 */
.game-container {
    position: relative;
    width: 100%;
    min-height: 100vh;
    background: linear-gradient(160deg, #E3F2FD 0%, #B3E5FC 50%, #81D4FA 100%);
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    overflow: hidden;
}

/* 匹配区域 */
.game-matching {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: rgba(255, 255, 255, 0.97);
    border-radius: 40px;
    padding: 40px 60px;
    width: 85%;
    max-width: 1200px;
    box-shadow:
        0 10px 30px rgba(66, 165, 245, 0.2),
        inset 0 0 20px rgba(255, 255, 255, 0.5);
    position: relative;
    z-index: 2;
    margin: 20px 0;
}

.game-matching::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background:
        linear-gradient(30deg, transparent 48%, rgba(255, 255, 255, 0.1) 50%, transparent 52%),
        linear-gradient(-30deg, transparent 48%, rgba(255, 255, 255, 0.1) 50%, transparent 52%);
    opacity: 0.3;
    pointer-events: none;
}

/* 头像装饰 */
.avatar-frame {
    position: relative;
    padding: 15px;
    background: linear-gradient(45deg, #81D4FA, #B3E5FC);
    border-radius: 50%;
    box-shadow: 0 0 30px rgba(129, 212, 250, 0.3);
    transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.avatar-frame:hover {
    transform: scale(1.05) rotate(2deg);
}

.avatar {
    width: 160px;
    height: 160px;
    border-radius: 50%;
    border: 5px solid #E1F5FE;
    object-fit: cover;
    transition: transform 0.3s ease;
}

/* 对手头像位置调整 */
.opponent-info .avatar {
    transform: translateX(44px);
}

/* 自然装饰 */
.nature-decor {
    position: absolute;
    top: -15px;
    left: -15px;
    width: 190px;
    height: 190px;
}

.nature-decor::before,
.nature-decor::after {
    content: '';
    position: absolute;
    background: #B3E5FC;
    border-radius: 50%;
    filter: drop-shadow(0 2px 4px rgba(3, 155, 229, 0.2));
}

.nature-decor::before {
    width: 25px;
    height: 25px;
    top: 10px;
    left: 50%;
    transform: translateX(-50%);
    box-shadow:
        35px 25px 0 #4FC3F7,
        -35px 25px 0 #29B6F6,
        0 50px 0 #039BE5;
}

.nature-decor::after {
    width: 20px;
    height: 20px;
    bottom: 15px;
    left: 50%;
    transform: translateX(-50%);
    box-shadow:
        25px -10px 0 #4FC3F7,
        -25px -10px 0 #29B6F6;
}

/* 名字框 */
.name-box {
    margin-top: 25px;
    padding: 12px 30px;
    background: #E1F5FE;
    border-radius: 30px;
    box-shadow: 0 5px 15px rgba(129, 212, 250, 0.2);
    text-align: center;
    position: relative;
    z-index: 1;
}

.name {
    font-size: 1.8rem;
    font-weight: 600;
    color: #0277BD;
    text-shadow: 1px 1px 3px rgba(255, 255, 255, 0.9);
    font-family: 'Microsoft YaHei', sans-serif;
    letter-spacing: 1px;
}

/* VS区域 */
.vs-container {
    position: relative;
    margin: 0 60px;
    perspective: 1000px;
}

.vs-text {
    font-size: 4.5rem;
    font-weight: bold;
    color: #01579B;
    text-shadow:
        2px 2px 4px rgba(255, 255, 255, 0.6),
        -2px -2px 4px rgba(3, 155, 229, 0.1);
    animation: pulse 1.8s ease-in-out infinite;
    transform-style: preserve-3d;

}

.nature-effect {
    position: absolute;
    width: 200%;
    height: 200%;
    top: -50%;
    left: -50%;
    background: radial-gradient(circle, rgba(179, 229, 252, 0.2) 10%, transparent 60%);
    animation: natureFlow 4s linear infinite;
}

@keyframes pulse {

    0%,
    100% {
        transform: scale(1) rotateX(0deg);
        opacity: 0.9;
    }

    50% {
        transform: scale(1.15) rotateX(10deg);
        opacity: 1;
    }
}

@keyframes natureFlow {
    0% {
        transform: rotate(0deg);
    }

    100% {
        transform: rotate(360deg);
    }
}

/* 按钮样式 */
.button-container {
    width: 100%;
    display: flex;
    justify-content: center;
    margin-top: 30px;
    position: relative;
    z-index: 3;
}

.match-button {
    position: relative;
    padding: 20px 70px;
    font-size: 2rem;
    color: #FFF;
    background: linear-gradient(135deg,
            #4FC3F7 0%,
            #29B6F6 30%,
            #039BE5 70%,
            #0277BD 100%);
    border: none;
    border-radius: 40px;
    cursor: pointer;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    overflow: hidden;
    font-family: 'Microsoft YaHei', sans-serif;
    letter-spacing: 2px;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.button-glow {
    position: absolute;
    width: 100%;
    height: 100%;
    background: radial-gradient(circle at 50% -30%, rgba(255, 255, 255, 0.5), transparent 70%);
    animation: glow 2.5s ease-in-out infinite;
}

@keyframes glow {

    0%,
    100% {
        opacity: 0.6;
    }

    50% {
        opacity: 0.2;
    }
}

@keyframes wave {
    0% {
        transform: translateX(-100%);
    }

    100% {
        transform: translateX(100%);
    }
}

.button-glow::after {
    content: '';
    position: absolute;
    top: -50%;
    left: 0;
    width: 200%;
    height: 200%;
    background: linear-gradient(90deg,
            transparent 25%,
            rgba(255, 255, 255, 0.3) 50%,
            transparent 75%);
    animation: wave 2s infinite linear;
}

.match-button:hover {
    transform: scale(1.05) translateY(-2px);
    box-shadow:
        0 0 50px rgba(3, 155, 229, 0.5),
        0 5px 20px rgba(3, 155, 229, 0.3);
}

/* 状态指示 */
.status-dot {
    width: 16px;
    height: 16px;
    background: #BDBDBD;
    border-radius: 50%;
    margin-left: 15px;
    transition: all 0.3s ease;
    position: relative;
}

.status-dot.matching {
    background: #00E5FF;
    box-shadow:
        0 0 20px #00E5FF,
        inset 0 0 5px rgba(255, 255, 255, 0.5);
    animation: pulseDot 1.2s ease-in-out infinite;
}

@keyframes pulseDot {

    0%,
    100% {
        transform: scale(1);
    }

    50% {
        transform: scale(1.3);
        box-shadow: 0 0 30px #00E5FF;
    }
}

/* 气泡提示 */
.bubble {
    position: absolute;
    top: -40px;
    left: 50%;
    transform: translateX(-50%);
    background: #E1F5FE;
    padding: 10px 25px;
    border-radius: 25px;
    box-shadow: 0 5px 20px rgba(3, 155, 229, 0.2);
    font-size: 1.3rem;
    color: #0277BD;
    font-weight: 500;
    animation: bubbleFloat 3s ease-in-out infinite;
    z-index: 2;
}

@keyframes bubbleFloat {

    0%,
    100% {
        transform: translate(-50%, 0);
    }

    50% {
        transform: translate(-50%, -15px);
    }
}

/* 响应式调整 */
@media (max-width: 768px) {
    .game-matching {
        flex-direction: column;
        padding: 30px;
        width: 90%;
    }

    .vs-container {
        margin: 30px 0;
    }

    .opponent-info .avatar {
        transform: translateX(20px);
    }

    .match-button {
        padding: 15px 40px;
        font-size: 1.6rem;
    }
}
</style>