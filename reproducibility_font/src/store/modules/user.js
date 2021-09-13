import { post, get } from '@/axios';
import md5 from 'js-md5';

const state = {
  // 每次页面刷新都会触发初始化
  name: localStorage.getItem('name'),
  token: localStorage.getItem('token'),
  avatar: localStorage.getItem('avatar'),
  userId: localStorage.getItem('userId'),
  email: localStorage.getItem('email'),
  unreadApplynum: localStorage.getItem('unreadApplynum'),
  unreadReplynum: localStorage.getItem('unreadReplynum')
};
//commit mutation ,同步
const mutations = {
  // 修改token，并将token存入localStorag
  //这里的state对应上面状态state
  // setUserInfo(state, userInfo) {
  //   state.userInfo = userInfo;
  //   localStorage.setItem('userInfo', userInfo);
  // },
  setName(state, name) {
    state.name = name;
    localStorage.setItem('name', name);
  },
  setUserId(state, userId) {
    state.userId = userId;
    localStorage.setItem('userId', userId);
  },
  setAvatar(state, avatar) {
    state.avatar = avatar;
    localStorage.setItem('avatar', avatar);
  },

  setToken(state, token) {
    state.token = token;
    localStorage.setItem('token', token);
  },

  setEmail(state, email) {
    state.email = email;
    localStorage.setItem('email', email);
  },

  setUnreadApplynum(state, unreadApplynum) {
    state.unreadApplynum = unreadApplynum
    localStorage.setItem('unreadApplynum', unreadApplynum)
  },

  setUnreadReplynum(state, unreadReplynum) {
    state.unreadReplynum = unreadReplynum
    localStorage.setItem('unreadReplynum', unreadReplynum)
  }

};
//dispatch action ，异步
const actions = {
  // 退出登录
  handleLogOut({ commit }) {
    commit('setToken', '');
    commit('setName', '');
    commit('setAvatar', '');
    commit('setUserId', '');
    commit('setEmail', '')
    commit('setUnreadApplynum', '')
    commit('setUnreadReplynum', '')
    localStorage.clear();
  },

  handleLogIn({ commit }, user) {
    const { email, password } = user; // 向后端发送请求，验证用户名密码是否正确，请求成功接收后端返回的token值，利用commit修改store的state属性，并将token存放在localStorage中

    let form = new FormData();
    form.append('email', email);
    form.append('password', md5(password)); //前端加密

    return new Promise((resolve, reject) => {
      post(`/users/login`, form)
        .then(response => {
          console.log(response);
          commit('setToken', response.token);
          commit('setName', response.name);
          commit('setAvatar', response.avatar);
          commit('setUserId', response.userId);
          commit('setEmail', response.email)
          commit('setUnreadApplynum', response.unreadApply)
          commit('setUnreadReplynum', response.unreadReply)
          resolve();
        })
        .catch(error => {
          reject(error);
        });
    });
  },

  // get user info
  getUserInfo({ commit }) {
    return new Promise((resolve, reject) => {
      get(`/users`)
        .then(data => {
          commit('setName', data.name);
          commit('setAvatar', data.avatar);
          commit('setUserId', data.userId);
          resolve(data);
        })
        .catch(error => {
          reject(error);
        });
    });
  }
};
export default {
  state,
  mutations,
  actions
};
