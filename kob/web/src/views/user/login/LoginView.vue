<template>
  <div v-if="!$store.state.user.pulling_info">
  <div class="container d-flex justify-content-center align-items-center min-vh-100 pt-5">
    <div class="card shadow-lg p-4" style="max-width: 400px; width: 100%; margin-top: -120px;">
      <div class="card-header text-center">
        <h4 class="mb-3">登录</h4>
      </div>
      <form @submit.prevent="login">
        <div class="card-body">
          <!-- 用户名输入框 -->
          <div class="mb-3">
            <label for="username" class="form-label">用户名</label>
            <input
              type="text"
              class="form-control"
              id="username"
              v-model="username"
              placeholder="请输入用户名"
              required
            />
          </div>

          <!-- 用户密码输入框 -->
          <div class="mb-3">
            <label for="password" class="form-label">密码</label>
            <input
              type="password"
              class="form-control"
              id="password"
              v-model="password"
              placeholder="请输入密码"
              required
            />
          </div>

          <div class="error-message">{{ error_message }}</div>
          <!-- 登录按钮 -->
          <button class="btn btn-primary w-100" type="submit">
            登录
          </button>

          <!-- 忘记密码链接 -->
          <div class="text-center mt-3">
            <a href="#" class="text-muted">忘记密码？</a>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>
</template>

<script>
import { useStore } from 'vuex';
import { ref } from 'vue';
import router from '../../../router/index';

export default {
  setup() {
    const store = useStore();
    const username = ref(''); // 确保这里是 const
    const password = ref(''); // 确保这里是 const
    const error_message = ref(''); // 初始化 error_message 为一个 ref
    const tmp = localStorage.getItem('jwt_token');
    if (tmp) {
      store.commit("updateToken", tmp);
      store.dispatch("getinfo", {
        success() {
          router.push({ name: 'home' });
          store.commit("updatePullingInfo", false);
        },
        error() {
          // 这里可以添加错误处理代码
          store.commit("updatePullingInfo", false);
        },
      });
    }
    else 
    {
         store.commit("updatePullingInfo", false);
    }

    const login = () => {
      error_message.value = "";
      store.dispatch("login", {
        username: username.value,
        password: password.value,
        success() {
          store.dispatch("getinfo", {
            success() {
              router.push({ name: 'home' });
              console.log(store.state.user);
            }
          });
        },
        error() {
          error_message.value = "用户名或密码错误";
        }
      });
    };

    return {
      username,
      password,
      error_message,
      login,
    };
  }
};
</script>

<style scoped>
.card {
  border-radius: 12px;
}

.card-header {
  background-color: #f8f9fa;
}

.card-body {
  padding: 2rem;
}

.btn {
  font-size: 1.1rem;
}

a {
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}
</style>
