import Vue from 'vue'
import App from './App.vue'
import router from './router'
import element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import "@/assets/iconfont/iconfont.css";
import '@/icons' // icon
import VueParticles from 'vue-particles'
import axios from 'axios'
import store from './store/index'
import './permission'
Vue.prototype.$http = axios

Vue.config.productionTip = false;

Vue.use(VueParticles)
Vue.use(element)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
Vue.config.productionTip = false
