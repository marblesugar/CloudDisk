import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Test from '../views/Test.vue'
import Share from '../views/Share.vue'
import Index from "../views/Index.vue";
import Register from "@/views/Register";
import Login from "@/views/Login";
import { Message } from 'element-ui';
Vue.prototype.$message = Message;

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/index',
    name: 'Index',
    component: Index
  },
  {
        path: '/share',
        name: 'Share',
        component: Share
  },
  {
    path: '/test',
    name: 'Test',
    component: Test
  },
  {
    path: '/',
    name: 'login',
    component: Login
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
    if (!(to.name === 'Login' || to.name === 'Register') && localStorage.getItem("token") === null) {
    console.log("请登录！")
    Message({
      message: "请登录",
      type: "warning"
    })
    next('/login')
  }else {
    next()
  }
})

export default router
