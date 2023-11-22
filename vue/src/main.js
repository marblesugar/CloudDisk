import Vue from 'vue'
import './plugins/axios'
import App from './App.vue'
import router from './router'
import store from './store'
import './plugins/element.js'
import axios from 'axios'
import 'font-awesome/css/font-awesome.min.css'//引入font-awesome

import preview from 'vue-photo-preview'
import 'vue-photo-preview/dist/skin.css'
Vue.use(preview)


axios.defaults.baseURL="http://localhost/disk" //发送请求时默认地址
Vue.prototype.$http = axios //发送请求时用this.$http置换axios

Vue.config.productionTip = false

Vue.prototype.$ELEMENT = { size: 'Medium' };

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')


