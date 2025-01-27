<template>
     <PlayGround v-if="$store.state.pk.status !== 'matching'"></PlayGround>
     <MatchGround v-if="$store.state.pk.status === 'matching'"></MatchGround>
     <GameEnd v-if ="$store.state.pk.status === 'game_over'"></GameEnd>
</template>
<script>
import PlayGround from '../../components/PlayGround.vue'
import { useStore } from 'vuex';
import { onMounted, ref, onUnmounted } from 'vue';
import MatchGround from '../../components/MatchGround.vue'
import GameEnd from '../../components/GameEnd.vue';

export default{
  components:{
    PlayGround,
    MatchGround,
    GameEnd,
  },
  setup()
  {
      let socket = null;
      let showinfo = ref("ismatching");
      const store = useStore();
      store.commit("clear_record_store");
      const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}`;
      onMounted(()=>{
        store.commit("updateSocket", null);
        store.commit("updateOpponent", {
                username: "我的对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            })

        socket = new WebSocket(socketUrl);
        socket.onopen = ()=>{
           store.commit("updateSocket", socket);
        };

        socket.onmessage = msg=>{
        const data = JSON.parse(msg.data);
        if (data.event === "start-matching")
        {
             store.commit("updateOpponent", {
               username:data.opponent_username,
               photo:data.opponent_photo,
               owner_snake_id: parseInt(data.owner_snake_id),
               opponent_snake_id: parseInt(data.opponent_snake_id)
             });
             setTimeout(() => {
                        store.commit("updateStatus", "playing");
             }, 2000);
             store.commit("updateGamemap", data.gamemap);
             store.commit("update_play_id", data.play_id);
        }else if (data.event === "move")
        {
            console.log(data.opponent_direction + " " + data.owner_direction);
            store.commit("update_direction", { opponent_direction:data.opponent_direction, owner_direction:data.owner_direction});
        }else if (data.event === "result")
        {      
              if (store.state.pk.snakes == null) return ;
              if (data.loser === "all")
              {
                    store.state.pk.snakes[0].status = "die";
                    store.state.pk.snakes[1].status = "die";
              }
              else if (data.loser === store.state.pk.play_id)
              {
                    if (store.state.pk.play_id === 'A')   store.state.pk.snakes[0].status = "die";
                    else   store.state.pk.snakes[1].status = "die";
              }
              else 
              {
                    if (store.state.pk.play_id === 'A')   store.state.pk.snakes[1].status = "die";
                    else   store.state.pk.snakes[0].status = "die";
              }
              store.commit("update_loser", data.loser);
              store.commit("updateStatus", "game_over");
              console.log(store.state.pk.play_id + " " + store.state.pk.loser);
        }
      }

           onUnmounted(()=>{
             store.state.pk.socket.send(JSON.stringify({
                     event:"stop-matching",
              }))
             socket.close();
             store.commit("updateStatus", "matching");
         })


      })

   


      return { showinfo};
  }

}

</script>


<style scoped>

</style>
