/**
 * meta可配置的参数有：
 * meta:{
 * title:(string)
 * requireAuth:(true)
 * }
 */
export default [{
        path: "*",
        name: "error_404",
        meta: {
            title: "404-页面不存在"
        },
        component: () => import("@/views/error-page/404.vue")
    },

    {
        path: "/test/mavon",
        name: "mavon",
        meta: {
            title: "r2 | mavon"
        },
        component: () => import("@/views/test/mavon")
    },

    {
        path: "/login",
        name: "login",
        meta: {
            title: "Login"
        },
        component: () => import("@/views/login")
    },
    {
        path: "/register",
        name: "register",
        meta: {
            title: "Register"
        },
        component: () => import("@/views/register")
    },
    {
        path: "/",
        redirect: "/home",
        component: () => import("_com/layout/NormalLayout.vue"),
        children: [{
                path: "/home",
                name: "home",
                meta: {
                    title: "r2 | home"
                },
                components: {
                    header: () => import("_com/layout/MyHeader.vue"),
                    main: () => import("@/views/home"),
                    footer: () => import("_com/layout/MyFooter.vue")
                }
            },
            {
                path: "/g2s",
                name: "g2s_list",
                meta: {
                    title: "r2 | list"
                },
                components: {
                    header: () => import("_com/layout/MyHeader.vue"),
                    main: () => import("@/views/g2s/list"),
                    footer: () => import("_com/layout/MyFooter.vue")
                }
            },
            {
                path: "/g2s/:id",
                name: "g2s_operation",
                meta: {
                    title: "r2 | operation",
                    requireAuth: true
                },
                components: {
                    header: () => import("_com/layout/MyHeader.vue"),
                    main: () => import("@/views/g2s/operation"),
                    footer: () => import("_com/layout/MyFooter.vue")
                }
            },
            {
                path: "/g2s/:id/compute",
                name: "g2s_compute",
                meta: {
                    title: "r2 | compute",
                    requireAuth: true
                },
                components: {
                    header: () => import("_com/layout/MyHeader.vue"),
                    main: () => import("@/views/g2s/operation/compute"),
                    footer: () => import("_com/layout/MyFooter.vue")
                }
            },
            {
                path: "/g2s/:id/view",
                name: "g2s_view",
                meta: {
                    title: "r2 | view",
                    requireAuth: true
                },
                components: {
                    header: () => import("_com/layout/MyHeader.vue"),
                    main: () => import("@/views/g2s/view"),
                    footer: () => import("_com/layout/MyFooter.vue")
                }
            },

            {
                path: "/resource/model",
                name: "model",
                meta: {
                    title: "r2 | model"
                },
                components: {
                    header: () => import("_com/layout/MyHeader.vue"),
                    main: () => import("@/views/resource/model"),
                    footer: () => import("_com/layout/MyFooter.vue")
                }
            },

            {
                path: "/resource/process",
                name: "process",
                meta: {
                    title: "r2 | model"
                },
                components: {
                    header: () => import("_com/layout/MyHeader.vue"),
                    main: () => import("@/views/resource/process"),
                    footer: () => import("_com/layout/MyFooter.vue")
                }
            },

            {
                path: "/resource/:id/:type",
                name: "detail",
                meta: {
                    title: "r2 | detail"
                },
                components: {
                    header: () => import("_com/layout/MyHeader.vue"),
                    main: () => import("@/views/resource/detail"),
                    footer: () => import("_com/layout/MyFooter.vue")
                }
            },

            {
                path: "/resource/:id/:type/invoke",
                name: "invoke",
                meta: {
                    title: "r2 | invoke",
                    requireAuth: true
                },
                components: {
                    header: () => import("_com/layout/MyHeader.vue"),
                    main: () => import("@/views/resource/invoke"),
                    footer: () => import("_com/layout/MyFooter.vue")
                }
            }
        ]
    }
];