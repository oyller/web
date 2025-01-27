<template>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container">
   <router-link class="navbar-brand" :to="{name:'home'}">King Of Bots</router-link>
    <div class="collapse navbar-collapse" id="navbarText">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <router-link :class="route_name == 'pk_index' ? 'nav-link active': 'nav-link'" :to="{name:'pk_index'}">对战</router-link>
        </li>
        <li class="nav-item">
          <router-link  :class="route_name == 'record_index' ? 'nav-link active': 'nav-link'" :to="{name:'record_index'}">对局列表</router-link>
        </li>
        <li class="nav-item">
          <router-link  :class="route_name == 'ranklist_index' ? 'nav-link active': 'nav-link'" :to="{name:'ranklist_index'}">排行榜</router-link>
        </li>
      </ul>
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
  <li class="nav-item dropdown">
    <a v-if="username" class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
      {{ username }}
    </a>
    <div v-else class="nav-link" href="#">
      <div class="login-container" @click.self="closeLoginOptions">
    <button v-if = "!$store.state.user.pulling_info" @click="toggleLoginOptions" class="login-btn">登录/注册</button>

     <!-- 登录选项卡片 -->
     <div v-if="showLoginOptions" class="login-options" @click.stop>
      <div class="options-card">
        <!-- 关闭按钮 -->
        <button @click="closeLoginOptions" class="close-btn">×</button>
        <p class="welcome-message">欢迎回来！</p>
        <div class="action-buttons">
          <router-link :to="{ name: 'login_index' }" class="link-btn">立即登录</router-link>
          <router-link :to="{ name: 'register_index' }" class="link-btn">首次使用？点击注册</router-link>
        </div>
      </div>
    </div>
  </div>
    </div>
    <ul v-if="username" class="dropdown-menu">
      <li><router-link :class="route_name == 'user_bot' ? 'dropdown-item active' : 'dropdown-item'" :to="{name:'userbot_index'}">我的Bot</router-link></li>
      <li><hr class="dropdown-divider"></li>
      <li><a class="dropdown-item" href="#" @click="logout">退出</a></li>
    </ul>
  </li>
</ul>

    </div>
  </div>
</nav>
</template>


<script>
import {useRoute} from 'vue-router'
import {computed} from 'vue'
import {useStore} from 'vuex';
import router from '../router/index'

export default{
   setup(){
       const store = useStore();
       const route = useRoute();
       let route_name = computed(() => route.name);
       let username = computed(() => store.state.user.username);
       const logout =()=>{
           store.dispatch("logout");
           router.push({name:'home'});
       };
       return {
           route_name,
           username,
           logout,
       }
   },
   data() {
    return {
      showLoginOptions: false,  // 控制卡片显示与隐藏
    };
  },
  methods: {
    // 切换显示登录选项卡片
    toggleLoginOptions() {
      this.showLoginOptions = !this.showLoginOptions;
    },
    // 点击外部区域时关闭卡片
    closeLoginOptions() {
      this.showLoginOptions = false;
    }
  }
};


</script>


<style scoped>
.navbar {
    background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.navbar-brand {
    font-weight: 600;
    letter-spacing: 1px;
    color: #e94560 !important;
    transition: all 0.3s ease;
}

.navbar-brand:hover {
    transform: scale(1.05);
}

.nav-link {
    color: rgba(255, 255, 255, 0.85) !important;
    margin: 0 12px;
    padding: 8px 15px !important;
    border-radius: 5px;
    transition: all 0.3s ease;
    position: relative;
}

.nav-link.active {
    color: #fff !important;
    font-weight: 500;
}

.nav-link.active::after {
    content: '';
    position: absolute;
    bottom: -5px;
    left: 50%;
    transform: translateX(-50%);
    width: 60%;
    height: 2px;
    background: #e94560;
    border-radius: 2px;
}

.nav-link:hover:not(.active) {
    background: rgba(255, 255, 255, 0.1);
    transform: translateY(-2px);
}

.login-btn {
    background: linear-gradient(135deg, #e94560 0%, #d2334a 100%);
    border: none;
    padding: 8px 25px;
    border-radius: 25px;
    font-weight: 500;
    letter-spacing: 0.5px;
    transition: all 0.3s ease;
}

.login-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 15px rgba(233, 69, 96, 0.4);
}

.login-options {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(26, 26, 46, 0.95);
    backdrop-filter: blur(5px);
    border-radius: 12px;
    overflow: hidden;
}

.options-card {
    padding: 25px;
}

.welcome-message {
    color: #fff;
    font-size: 1.1rem;
    margin-bottom: 20px;
}

.link-btn {
    background: linear-gradient(135deg, #2d4263 0%, #1a1a2e 100%);
    border: 1px solid rgba(255, 255, 255, 0.1);
    margin: 10px 0;
    padding: 10px;
    border-radius: 8px;
    transition: all 0.3s ease;
}

.link-btn:hover {
    background: linear-gradient(135deg, #3d5a80 0%, #2d4263 100%);
    transform: translateX(5px);
}

.close-btn {
    color: rgba(255, 255, 255, 0.6);
    font-size: 1.5rem;
    padding: 0 8px;
}

.close-btn:hover {
    color: #e94560;
}

.dropdown-menu {
    background: #1a1a2e;
    border: 1px solid rgba(255, 255, 255, 0.1);
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.dropdown-item {
    color: rgba(255, 255, 255, 0.85);
    transition: all 0.3s ease;
}

.dropdown-item:hover {
    background: #e94560;
    color: #fff !important;
}

.dropdown-item.active {
    background: #e94560 !important;
}
</style>