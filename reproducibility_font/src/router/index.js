import Vue from "vue";
import VueRouter from "vue-router";
import routes from "./routes";

const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err);
};

Vue.use(VueRouter);

const router = new VueRouter({
    routes
});

router.beforeEach((to, from, next) => {
    try {
        document.title = to.meta.title;
    } catch (e) {
        console.warn("load title warning!");
    }

    if (to.matched.some(r => r.meta.requireAuth)) {
        //默认token存在
        const token = localStorage.getItem("token");
        if (!token && to.name !== "login") {
            // 未登录且要跳转的页面不是登录页
            next({
                name: "login", // 跳转到登录页
                query: {
                    redirect: to.fullPath
                }
            });
        } else if (!token && to.name === "login") {
            // 未登陆且要跳转的页面是登录页
            next(); // 跳转
        } else if (token && to.name === "login") {
            // 已登录且要跳转的页面是登录页
            next({
                name: "home" // 跳转到home页
            });
        } else {
            next();
        }
    } else {
        next();
    }
});

router.afterEach(() => {
    window.scrollTo(0, 0);
});

export default router;
