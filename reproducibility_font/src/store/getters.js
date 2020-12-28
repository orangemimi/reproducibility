const getters = {
  token: state => state.user.token,
  userId: state => state.user.userId,
  name: state => state.user.name,
  avatar: state => state.user.avatar,
  role: state => state.permission.role,
  permission_routes: state => state.permission.routes
  //   userName: state => state.user.userName
};
export default getters;
