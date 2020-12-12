import Vue from 'vue';
import VueRouter from 'vue-router';
import routes from './routes';

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
    console.warn('load title warning!');
  }

  if (to.matched.some(r => r.meta.requireAuth)) {
    // this route requires auth, check if logged in
    // if not, redirect to login page.
    //默认token存在
    const token = localStorage.getItem('token');
    if (token) {
      // if (to.name === 'Login') {
      //   // 已登录且要跳转的页面不是登录页，需要进行权限验证
      //   next({
      //     name: 'Home' // 跳转到home页
      //   });
      // } else {
      //   // store
      //   //   .dispatch('GetUserInfo')
      //   //   .then(res => {
      //   //     // 拉取user_info
      //   //     const roles = res.data.data.roles; // note: roles must be a array! such as: ['editor','develop']
      //   //     store.dispatch('GenerateRoutes', { roles }).then(() => {
      //   //       // 根据roles权限生成可访问的路由表
      //   //       router.addRoutes(store.getters.addRouters); // 动态添加可访问路由表
      //   //       next({ ...to, replace: true }); // hack方法 确保addRoutes已完成 ,set the replace:
      //   //     });
      //   //   })
      //   //   .catch(err => {
      //   //     store.dispatch('FedLogOut').then(() => {
      //   //       Message.error(err || 'Verification failed, please login again');
      //   //       next({ path: '/' });
      //   //     });
      //   //   });
      // }
    } else {
      // no token
      //   next(); // 跳转
      next({
        path: '/login',
        query: { redirect: to.fullPath } //把要跳转的地址作为参数传到下一步,重定向
      });
    }
  } else {
    next();
  }
});

router.afterEach(() => {
  window.scrollTo(0, 0);
});

export default router;
