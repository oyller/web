export default {
    state: {
       status:"matching",
       socket:null,
       opponent_username:"我的对手",
       opponent_photo:"",
       opponent_id:-1,
       gamemap:null,
       snakes:null,
       owner_snake_id:-1,
       opponent_snake_id:-1,
       game:null,
       play_id:"**",
       isVictory:null,
       loser:null,
    },
    getters: {
    },
    mutations: {
       clear_end(state){
         state.opponent_username = "我的对手";
         state.opponent_photo = "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png";
         state.opponent_id = -1;
         state.gamemap = null;
         state.snakes = null;
         state.owner_snake_id = -1;
         state.opponent_snake_id = -1;
         state.game = null;
         state.play_id = "**";
         state.isVictory = null;
         state.loser = null;
       },
       updateSocket(state, socket){
         state.socket = socket;
       },
       updateOpponent(state, opponent){
        state.opponent_username = opponent.username;
        state.opponent_photo = opponent.photo;
        state.opponent_id = opponent.id;
        state.owner_snake_id = opponent.owner_snake_id;
        state.opponent_snake_id = opponent.opponent_snake_id;
       },
       updateStatus(state, status){
         state.status = status;
       },
       updateGamemap(state, gamemap) {
        state.gamemap = gamemap;
       },
       update_direction(state, data){
           let t = state.opponent_snake_id;
           let t1 = state.owner_snake_id;
           console.log(state.game);
            state.game.snakes[t].set_direction(data.opponent_direction);
            state.game.snakes[t1].set_direction(data.owner_direction);
     //    state.snakes[state.opponent_snake_id].direction = direction;
       },
       update_play_id(state, data)
       {
           state.play_id = data;
       },
       update_loser(state, data)
       {
           state.loser = data;
       }
    },
    actions: {
    },
    modules: {
    }
}
