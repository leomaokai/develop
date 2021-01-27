import Vue from 'vue'
import App from './App.vue'
import router from './router'

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

// 关闭浏览器配置提示
Vue.config.productionTip = false

//引入ElementUI
Vue.use(ElementUI);

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
