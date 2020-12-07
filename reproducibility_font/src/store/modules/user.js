export default {
    state: {
        // 每次页面刷新都会触发初始化
        name: localStorage.getItem("name"),
        token: localStorage.getItem("token")
    },
    getters: {},
    //commit mutation ,同步
    mutations: {
        setName(state, name) {
            state.name = name;
            localStorage.setItem("name", name);
        },
        setToken(state, token) {
            state.token = token;
            localStorage.setItem("token", token);
        }
    },
    //dispatch action ，异步
    actions: {
        // 退出登录
        handleLogOut({commit}) {
            commit("setToken", "");
            commit("setName", "");
            localStorage.clear();
        },
        handleLogIn({commit}, user) {
            commit("setToken", user.token);
            commit("setName", user.name);
        }
    }
};
