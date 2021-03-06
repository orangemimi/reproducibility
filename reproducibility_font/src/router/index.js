import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

/**
 * meta可配置的参数有：
 * meta:{
 * title:(string)
 * requireAuth:(true)
 * roles: ['builder', 'rebuilder_explorer','rebuilder_operator', 'visitor']
 * }
 */
export const constantRoutes = [
  {
    path: '*',
    name: 'error_404',
    meta: {
      title: '404-页面不存在'
    },
    component: () => import('@/views/error-page/404.vue')
  },
  {
    path: '/login',
    name: 'Login',
    meta: {
      title: 'Login'
    },
    component: () => import('@/views/login')
  },
  {
    path: '/register',
    name: 'Register',
    meta: {
      title: 'Register'
    },
    component: () => import('@/views/register')
  },
  {
    path: '/tree',
    name: 'Tree',
    meta: {
      title: 'Tree'
    },
    component: () => import('_com/TreeDescription/TreeComp')
  },
  {
    path: '/',
    redirect: '/home',
    component: () => import('_com/layout/NormalLayout.vue'),
    children: [
      {
        path: '/home',
        name: 'Home',
        meta: {
          title: 'r2 | home'
        },
        components: {
          header: () => import('_com/layout/MyHeader.vue'),
          main: () => import('@/views/home'),
          footer: () => import('_com/layout/MyFooter.vue')
        }
      },

      {
        path: '/user',
        name: 'UserInfo',
        meta: {
          title: 'User Info',
          requireAuth: true
        },
        components: {
          header: () => import('_com/layout/MyHeader.vue'),
          main: () => import('@/views/user'),
          footer: () => import('_com/layout/MyFooter.vue')
        },
        children: [
          {
            path: 'home',
            name: 'UserHome',
            component: () => import('@/views/user/components/Home')
          },
          {
            path: 'resource',
            name: 'UserResource',
            component: () => import('@/views/user/components/Resource')
          },
          {
            path: 'model',
            name: 'UserModel',
            component: () => import('@/views/user/components/Model')
          },
          {
            path: 'account',
            name: 'UserAccount',
            component: () => import('@/views/user/components/Account')
          }
        ]
      },
      {
        path: '/projects',
        name: 'Projects',
        meta: {
          title: 'r2 | projects'
        },
        components: {
          header: () => import('_com/layout/MyHeader.vue'),
          main: () => import('@/views/projects'),
          footer: () => import('_com/layout/MyFooter.vue')
        }
      },
      {
        path: '/newproject',
        name: 'NewProject',
        meta: {
          title: 'newproject',
          requireAuth: true
        },
        components: {
          header: () => import('_com/layout/MyHeader.vue'),
          main: () => import('@/views/projects/create'),
          footer: () => import('_com/layout/MyFooter.vue')
        }
      },
      {
        path: '/project/:id',
        name: 'Project',
        meta: {
          title: 'r2 | project',
          requireAuth: true
          // roles: ['builder'] // or you can only set roles in sub nav
        },
        components: {
          header: () => import('_com/layout/MyHeader.vue'),
          main: () => import('@/views/project'),
          footer: () => import('_com/layout/MyFooter.vue')
        },
        children: [
          {
            path: 'info',
            name: 'Information',
            component: () => import('@/views/project/info'),
            meta: {
              requireAuth: true,
              roles: ['builder', 'rebuilder_explorer', 'rebuilder_operator', 'visitor'] // or you can only set roles in sub nav
            }
          },
          {
            path: 'construction',
            name: 'Construction',
            component: () => import('@/views/project/construction'),
            meta: {
              requireAuth: true,
              roles: ['builder', 'rebuilder_explorer', 'rebuilder_operator'] // or you can only set roles in sub nav
            }
          },
          {
            path: 'contributor',
            name: 'Contributor',
            component: () => import('@/views/project/contributor'),
            meta: {
              requireAuth: true,
              roles: ['builder'] // or you can only set roles in sub nav
            }
          },
          {
            path: 'reproduction',
            name: 'Reproduction',
            component: () => import('@/views/project/reproduction'),
            meta: {
              requireAuth: true,
              roles: ['builder', 'rebuilder_explorer', 'rebuilder_operator'] // or you can only set roles in sub nav
            }
          }
        ]
      },
      {
        path: '/newmodel',
        name: 'NewModel',
        meta: {
          title: 'newmodel',
          requireAuth: true
        },
        components: {
          header: () => import('_com/layout/MyHeader.vue'),
          main: () => import('@/views/models/create.vue'),
          footer: () => import('_com/layout/MyFooter.vue')
        }
      },
      {
        path: '/models',
        name: 'Models',
        meta: {
          title: 'models',
          requireAuth: true
        },
        components: {
          header: () => import('_com/layout/MyHeader.vue'),
          main: () => import('@/views/models/index.vue'),
          footer: () => import('_com/layout/MyFooter.vue')
        }
      }
    ]
  }
];

const createRouter = () =>
  new Router({
    // mode: 'history', // require service support
    // scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes
  });

const router = createRouter();

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher; // reset router
}

export default router;
