import Vue from 'vue'
import VueRouter from 'vue-router'


Vue.use(VueRouter)

  const routes = [
  {
    path:'/',
    name:'index',
    component: resolve => require(['../views/index.vue'], resolve),
    redirect:'/indexView',
    children:[
      {
        path:'/indexView',
        component:resolve=>require(['../components/index/indexView.vue'],resolve)
      },
      {
        path:'/messageBoard',
        component:resolve=>require(['../components/index/messageBoard.vue'],resolve)
      },
      {
        path:'/indexMonitor',
        component:resolve=>require(['../components/index/monitor.vue'],resolve)
      },
      {
        path:'/archived',
        component:resolve=>require(['../components/index/archived.vue'],resolve)
      },
      {
        path:'/indexLabel',
        component:resolve=>require(['../components/index/label.vue'],resolve)
      },
      {
        path:'/sort',
        component:resolve=>require(['../components/index/sort.vue'],resolve)
      },
      {
        path:'/aboutMe',
        component:resolve=>require(['../components/index/aboutMe.vue'],resolve)
      },
      {
        path:'/personCenter',
        component:resolve=>require(['../components/index/personCenter.vue'],resolve)
      },
      {
        path:'/comment',
        component:resolve=>require(['../components/comment'],resolve)
      }
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: resolve => require(['../views/login.vue'], resolve),
  },
  {
    path: '/article',
    name: 'article',
    component: resolve => require(['../views/article.vue'], resolve),
  },
  {
    path: '/500',
    name: '500',
    component: resolve => require(['../views/500.vue'], resolve),
  },
  {
    path: '/502',
    name: '502',
    component: resolve => require(['../views/502.vue'], resolve),
  },
  {
    path: '/404',
    name: '404',
    component: resolve => require(['../views/404.vue'], resolve),
  },
]

const router = new VueRouter({
  routes
})

export default router
