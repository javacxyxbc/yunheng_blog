import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)
const store = new Vuex.Store({
    state:{
        showSearchBar:false,
    },
    getters:{
        getSearchBar(state){
            return state.showSearchBar;
        }
    },
    mutations:{
         changeSearchBarState(state){
             state.showSearchBar=!state.showSearchBar;
         }
    },
    actions:{
        changeSearchBarState(context){
            context.commit('changeSearchBarState')
        }
    }
})

export default store
