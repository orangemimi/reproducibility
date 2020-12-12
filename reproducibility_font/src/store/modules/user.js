export default {
  state: {
    // 每次页面刷新都会触发初始化
    userInfo: localStorage.getItem('userInfo'),
    token: localStorage.getItem('token')
  },
  getters: {},
  //commit mutation ,同步
  mutations: {
    // 修改token，并将token存入localStorag
    //这里的state对应上面状态state
    setUserInfo(state, userInfo) {
      state.userInfo = userInfo;
      // localStorage.setItem('userName', userInfo.userName);
      localStorage.setItem('userInfo', userInfo);
    },
    setToken(state, token) {
      state.token = token;
      localStorage.setItem('token', token);
    }
  },
  //dispatch action ，异步
  actions: {
    // 退出登录
    handleLogOut({ commit }) {
      commit('setToken', '');
      commit('setUserInfo', '');
      localStorage.clear();
    },
    handleLogIn({ commit }, user) {
      commit('setToken', user.token);
      commit('setUserInfo', user.userInfo);
    }
    // generateRoutes({ commit }, roles) {
    //   return new Promise(resolve => {
    //     let accessedRoutes;
    //     if (roles.includes('admin')) {
    //       accessedRoutes = asyncRoutes || [];
    //     } else {
    //       accessedRoutes = filterAsyncRoutes(asyncRoutes, roles);
    //     }
    //     commit('SET_ROUTES', accessedRoutes);
    //     resolve(accessedRoutes);
    //   });
    // }
  }
};
