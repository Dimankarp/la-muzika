import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import BandView from '@/views/BandView.vue'
import { userStore } from '@/js/store'
import AlbumView from '@/views/AlbumView.vue'
import StudioView from '@/views/StudioView.vue'
import BandVisualiser from '@/components/BandVisualiser.vue'
import AdminRequestTable from '@/components/AdminRequestTable.vue'
import ActionsView from '@/views/ActionsView.vue'
import VisualiserView from '@/views/VisualiserView.vue'
import BatchRequestTable from '@/components/BatchRequestTable.vue'
const routes = [
  {
    path: '/login',
    name: 'login',
    component: LoginView

  },
  {
    path: '/bands',
    name: 'bands',
    component: BandView,
  },

  {
    path: '/albums',
    name: 'albums',
    component: AlbumView,
  },

  {
    path: '/studios',
    name: 'studios',
    component: StudioView,
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterView
  },
  {
    path: '/requests',
    name: 'requests',
    component: AdminRequestTable
  },
  {
    path: '/batches',
    name: 'batches',
    component: BatchRequestTable
  },
  {
    path: '/actions',
    name: 'actions',
    component: ActionsView
  },

  {
    path: '/visual',
    name: 'visual',
    component: VisualiserView
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
  console.log("To" + to.name)
  if (to.name === 'login' || to.name === 'register' || to.name === 'not-found') {
    console.log(userStore.username)
    console.log(userStore.isLoggedIn)
    if (userStore.isLoggedIn) {
      console.log("Redirect to bands")
      return { name: 'bands' }
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
