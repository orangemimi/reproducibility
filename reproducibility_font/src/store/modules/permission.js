// import { constantRoutes } from '@/router';

// export function filterAsyncRoutes(routes, role) {
//   const res = [];

//   routes.forEach(route => {
//     const tmp = { ...route };
//     if (hasPermission(role, tmp)) {
//       if (tmp.children) {
//         tmp.children = filterAsyncRoutes(tmp.children, role);
//       }
//       res.push(tmp);
//     }
//   });

//   return res;
// }

// function hasPermission(role, route) {
//   if (route.meta && route.meta.roles) {
//     return route.meta.roles.includes(role);
//   } else {
//     return true;
//   }
// }

const state = {
  // routes: [],
  // addRoutes: [],
  role: localStorage.getItem('role')
};

const mutations = {
  // SET_ROUTES: (state, routes) => {
  //   state.addRoutes = routes;
  //   state.routes = constantRoutes.concat(routes);
  // },
  SET_ROLE: (state, role) => {
    state.role = role;
    localStorage.setItem('role', role);
  }
};

const actions = {
  // generateRoutes({ commit }, role) {
  //   return new Promise(resolve => {
  //     let accessedRoutes;
  //     if (role == 'builder') {
  //       debugger;
  //       accessedRoutes = asyncRoutes || [];
  //     } else {
  //       accessedRoutes = filterAsyncRoutes(asyncRoutes, role);
  //     }
  //     console.log(accessedRoutes);
  //     commit('SET_ROUTES', accessedRoutes);
  //     resolve(accessedRoutes);
  //   });
  // },

  // getPermission({ commit }, role) {
  //   return new Promise(resolve => {
  //     let accessedRoutes;
  //     if (role == 'builder') {
  //       accessedRoutes = asyncRoutes || [];
  //     } else {
  //       accessedRoutes = filterAsyncRoutes(asyncRoutes, role);
  //     }
  //     console.log(accessedRoutes);
  //     commit('SET_ROUTES', accessedRoutes);
  //     resolve(accessedRoutes);
  //   });
  // },

  getRole({ commit }, { project, userId }) {
    console.log(project);
    return new Promise(resolve => {
      // debugger;
      if (project.creator == userId) {
        commit('SET_ROLE', 'builder');
      } else if (project.members != null && project.members.some(member => member.memberId == userId)) {
        let member = project.members.filter(item => item.memberId == userId); //member's userId
        console.log('member', member);
        commit('SET_ROLE', member[0].role);
      } else {
        commit('SET_ROLE', 'visitor');
      }
      resolve();
    });
  }
};

export default {
  namespaced: true,
  state,
  mutations,
  actions
};
