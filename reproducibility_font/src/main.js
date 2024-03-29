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
import 'mavon-editor/dist/css/index.css'

import './assets/iconfont/iconfont.css';
import './assets/iconfont1/iconfont.css'
import './assets/iconfont1/iconfont.js'
import './assets/font/font.css'
import '@/router/permission'; // permission control
import vuescroll from 'vuescroll';
import vueJsonEditor from 'vue-json-editor';
// import Highlight from '@/utils/highlight'; // 这里是你项目highlight.js所在路径

import { codemirror } from 'vue-codemirror';
import 'codemirror/lib/codemirror.css';

import BaiduMap from 'vue-baidu-map'



// import Descriptions from '_com/Description/index.js';
Vue.config.devtools = true;
Vue.config.productionTip = false;
Vue.use(ElementUI, { locale: enLocale });
// Vue.use(ElementPlus, { enLocale });
Vue.use(mavonEditor);
Vue.use(vuescroll);
// Vue.use(Highlight);
Vue.use(codemirror);
Vue.use(BaiduMap, {
  ak: 'duBHPLUvDk5T3GgLF2o66hqSG3hnGTw5'
})


Vue.component('vue-json-editor', vueJsonEditor);
// Vue.use(vueJsonEditor);

Vue.config.errorHandler = errorHandler;
Vue.prototype.$throw = error => errorHandler(error, this);
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');

import x2js from 'x2js' // xml数据处理插件
Vue.prototype.$x2js = new x2js() // 创建x2js对象，挂到vue原型上

// const app = createApp(App)
// app.use(mavonEditor),
// app.use(ElementUI, { locale: enLocale });
// app.use(mavonEditor);
// app.use(vuescroll);
// app.use(ElDescriptions);
// app.use(router);
// app.use(store);
// app.mount('#app')
