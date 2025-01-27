import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView.vue'
import RanklistIndexView from '../views/ranklist/RanklistIndexView.vue'
import RecordIndexView from '../views/record/RecordIndexView.vue'
import UserBotIndexView from '../views/user/bot/UserBotIndexView.vue'
import NotFound from '../views/error/NotFound.vue'
import LoginView  from '../views/user/login/LoginView.vue'
import RegisterIndexView from '@/views/user/register/RegisterIndexView.vue'
import store from '../store/index'
import RecordViedoView from '../views/record/RecordViedoView.vue'

const routes = [
   {
    path:"/",
    name:"home",
    component:PkIndexView,
    meta:{
      authorize:true,
    }
   },
   {
       path:"/pk/",
       name:"pk_index",
       component:PkIndexView,
       meta:{
       authorize:true,
       }
   },
   {
    path:"/ranklist/",
    name:"ranklist_index",
    component:RanklistIndexView,
    meta:{
    authorize:true,
    }
   },
   {
    path:"/record/",
    name:"record_index",
    component:RecordIndexView,
    meta:{
    authorize:true,
    }
   },
   {
    path:"/record/:recordId/",
    name:"record_viedo_index",
    component:RecordViedoView,
    meta:{
    authorize:true,
    }
   },
   {
    path:"/user/bot/",
    name:"userbot_index",
    component:UserBotIndexView,
    meta:{
    authorize:true,
    }
   },

   {
    path:"/user/login/",
    name:"login_index",
    component:LoginView,
    meta:{
    authorize:false,
    }
   },
   {
    path:"/user/register/",
    name:"register_index",
    component:RegisterIndexView,
    meta:{
    authorize:false,
    }
   },
   {
    path:"/404/",
    name:"404",
    component:NotFound,
   },
   {
    path:"/:catchAll(.*)",
    redirect:"/404/"
   },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) =>{
    if (to.meta.authorize == true && !store.state.user.is_login)
    {
        next({name:"login_index"});
    }
    else 
    {
        next();
    }
})

export default router
