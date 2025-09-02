/*
 * main js
 *
 * @Author: ShanZhu
 * @Date: 2023-11-17
 */
import Vue from "vue";
import App from "./App.vue";
import './assets/font/iconfont.css'
import router from "./router";
Vue.config.productionTip = false;
new Vue({
  router,
  render: h => h(App)
}).$mount("#app");
