const getters = {
  token: state => state.user.token,
  userInfo: state => state.user.userInfo,
  userName: state => state.user.userInfo.userName,
  avatar: state => state.user.userInfo.avatar

  //   userName: state => state.user.userName
};
export default getters;
