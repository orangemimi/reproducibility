import Vue from 'vue';
// import { createApp } from 'vue'
import App from './App.vue';
import router from '@/router';

import ElementUI from 'element-ui';
import store from '@/store';
import 'element-ui/lib/theme-chalk/index.css';
import enLocale from '../node_modules/element-ui/lib/locale/lang/en'; //英文

import { errorHandler } from '@/lib/error';
import mavonEditor from 'mavon-editor';
import 'mavon-editor/dist/css/index.css';

import './assets/iconfont/iconfont.css';
import '@/router/permission'; // permission control
import vuescroll from 'vuescroll';
import vueJsonEditor from 'vue-json-editor';

// import Descriptions from '_com/Description/index.js';

Vue.config.productionTip = false;
Vue.use(ElementUI, { locale: enLocale });
Vue.use(mavonEditor);
Vue.use(vuescroll);
// Vue.use();

Vue.component('vue-json-editor', vueJsonEditor);
// Vue.use(vueJsonEditor);

Vue.config.errorHandler = errorHandler;
Vue.prototype.$throw = error => errorHandler(error, this);
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');

// const app = createApp(App)
// app.use(mavonEditor),
// app.use(ElementUI, { locale: enLocale });
// app.use(mavonEditor);
// app.use(vuescroll);
// app.use(ElDescriptions);
// app.use(router);
// app.use(store);
// app.mount('#app')
