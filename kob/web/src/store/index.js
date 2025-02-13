import { createStore } from 'vuex'
import User from   './user'
import Pk from './pk'
import Record from './record'

export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
      user:User,
      pk:Pk,
      record:Record,
  }
})
