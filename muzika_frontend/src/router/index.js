import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import TableView from '@/views/TableView.vue'
import { userStore } from '@/js/store'
const routes = [
  {
    path: '/login',
    name: 'login',
    component: LoginView

  },
  {
    path: '/table',
    name: 'table',
    component: TableView,
    props: { entryType: 'bands' }
  },

  {
    path: '/register',
    name: 'register',
    component: RegisterView
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'not-found',
    component: LoginView
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

router.beforeEach((to) => {
  if (to.name === 'login' || to.name === 'register' || to.name === 'not-found') {
    if (userStore.isLoggedIn) {
      console.log("REdirect to table")
      return { name: 'table' }
    }
    else return true;
  } else {
    if (userStore.isLoggedIn) {
      return true;
    } else {
      return { name: 'login' }
    }
  }
})

export default router
