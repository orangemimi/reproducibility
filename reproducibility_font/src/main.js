import Vue from 'vue';
import App from './App.vue';
import router from '@/router';

import ElementUI from 'element-ui';
import store from '@/store';
import 'element-ui/lib/theme-chalk/index.css';
import { errorHandler } from '@/lib/error';
import mavonEditor from 'mavon-editor';
import 'mavon-editor/dist/css/index.css';

import './assets/iconfont/iconfont.css';
import '@/router/permission'; // permission control
// import "./assets/variable.scss";

Vue.config.productionTip = false;
Vue.use(ElementUI);
Vue.use(mavonEditor);

Vue.config.errorHandler = errorHandler;
Vue.prototype.$throw = error => errorHandler(error, this);
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');
