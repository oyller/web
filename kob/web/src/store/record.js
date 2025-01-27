export default {
    state: {
       is_record:false,
       a_steps:"",
       b_steps:"",
       record_loser:"",
       a_id:-1,
       a_sx:-1,
       a_sy:-1,
       b_id:-1,
       b_sx:-1,
       b_sy:-1,
       game:null,
    },
    getters: {
    },
    mutations: {
        clear_record_store(state)
        {
            state.is_record = false;
            state.a_steps = "";
            state.a_id = -1;
            state.a_sx = -1;
            state.a_sy = -1;
            state.game = null;
            state.b_steps = "";
            state.b_id = -1;
            state.b_sx = -1;
            state.b_sy = -1;
            state.record_loser = "";
        },
        updateIsRecord(state, is_record) {
            state.is_record = is_record;
        },
        updateSteps(state, data) {
            state.a_steps = data.a_steps;
            state.b_steps = data.b_steps;
        },
        updateRecordLoser(state, loser) {
            state.record_loser = loser;
        },
        update_Record_Game(state, data){
            state.game = data.map;
            state.a_id = data.a_id;
            state.a_sx = data.a_sx;
            state.a_sy = data.a_sy;
            state.b_id = data.b_id;
            state.b_sx = data.b_sx;
            state.b_sy = data.b_sy;
        }
    },
    actions: {
    },
    modules: {
    }
}
