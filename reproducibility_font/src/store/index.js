import Vue from 'vue';
import Vuex from 'vuex';
import getters from './getters';

Vue.use(Vuex);

// https://webpack.js.org/guides/dependency-management/#requirecontext
const files = require.context('./modules', true, /\.js$/),
  modules = {};

files.keys().forEach(key => {
  modules[key.replace(/\.\/|\.js/g, '')] = files(key).default;
});
// 导入所有 vuex 模块，自动加入namespaced:true，用于解决vuex命名冲突
Object.keys(modules).forEach(key => {
  modules[key]['namespaced'] = true;
});

export default new Vuex.Store({
  modules,
  getters
});
