// import Vue from 'vue';
// import VueRouter from 'vue-router';
// import store from '@/store';
import router from '@/router';
import ElementUI from 'element-ui';

// const originalPush = VueRouter.prototype.push;
// VueRouter.prototype.push = function push(location) {
//   return originalPush.call(this, location).catch(err => err);
// };
// Vue.use(VueRouter);

// const router = new VueRouter({
//   routes
// });

// const whiteList = ['/Login', '/Home']; // no redirect whitelist

router.beforeEach((to, from, next) => {
  try {
    document.title = to.meta.title;
  } catch (e) {
    console.warn('load title warning!');
  }

  const role = localStorage.getItem('role');
  const token = localStorage.getItem('token');

  if (to.matched.some(r => r.meta.requireAuth)) {
    if (token) {
      let flag = filterPermission(to.matched, role);
      if (flag) {
        next();
      } else {
        //如果没有权限抛出自定义的错误
        ElementUI.Notification.error('You have no access to get in this page!');
        next({
          path: from.fullPath
        });
      }
    } else {
      //no token
      next({
        name: 'Login', // 跳转到home页
        query: { redirect: to.fullPath } //把要跳转的地址作为参数传到下一步,重定向
      });
    }
  } else {
    //如果to.path不需要权限
    next();
  }
});

function filterPermission(routes, role) {
  let flag = true;
  routes.forEach(route => {
    const tmp = { ...route };
    if (hasPermission(role, tmp)) {
      if (tmp.children) {
        tmp.children = filterPermission(tmp.children, role);
      }
    } else {
      flag = false;
    }
  });

  return flag;
}

function hasPermission(role, route) {
  //false=> no permission
  //true=>1no meta.roles 2.has role
  if (route.meta && route.meta.roles) {
    return route.meta.roles.includes(role);
  } else {
    return true;
  }
}

router.afterEach(() => {
  window.scrollTo(0, 0);
});

export default router;
