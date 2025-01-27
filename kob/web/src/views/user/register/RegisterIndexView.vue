<template>
       <div class="container d-flex justify-content-center align-items-center min-vh-100 pt-5">
    <div class="card shadow-lg p-4" style="max-width: 400px; width: 100%; margin-top: -120px;">
      <div class="card-header text-center">
        <h4 class="mb-3">玩家注册</h4>
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

         <!-- 用户密码输入框 -->
         <div class="mb-3">
          <label for="password" class="form-label">确认密码</label>
          <input
            type="password"
            class="form-control"
            id="confirmedPassword"
            v-model="confirmedPassword"
            placeholder="请输入密码"
            required
          />
        </div>

        <div class="error-message">{{ error_message }}</div>
        <!-- 登录按钮 -->
        <button class="btn btn-primary w-100" @click="register">
           注册
        </button>
 
      </div>
    </form>
    </div>
  </div>
</template>

<script>
import { useStore } from 'vuex';
import {ref} from 'vue';
import router from '../../../router/index'

export default {
   setup(){
    const store = useStore();
    let username = ref('');
    let password = ref('');
    let confirmedPassword = ref('');
    let error_message = ref('');
    const register = () =>{
        error_message.value = "";
        if (password.value !== confirmedPassword.value)
        {
            alert("两次输入不一致, 请重新输入");
            router.push({name:'register_index'});
        }
        store.dispatch("register", {
           username:username.value,
           password:password.value,
           confirmedPassword:confirmedPassword.value,
           success(){
              alert("注册成功，请重新登录");
              router.push({name:'login_index'});
           },
           error(){
              error_message.value = "注册失败";
           }
        });
    }

     return {
        username,
        password,
        confirmedPassword,
        register,
     }
   }


}
</script>

<style>
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