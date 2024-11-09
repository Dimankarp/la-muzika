<script setup>
import { RouterView, useRoute, useRouter } from 'vue-router'
import { computed, onBeforeMount, onMounted, watch } from "vue";
import TopMenu from '@/components/TopMenu.vue';
import {userStore} from '@/js/store'
import { fetchUser, logout } from '@/js/auth';


const route = useRoute()
const isLoginOrRegisterOrUndefined = computed(() => route.name === "login" || route.name === "register"  || route.name === "not-found" || route.name === undefined)

console.log(isLoginOrRegisterOrUndefined.value)

const router = useRouter()

onBeforeMount(async () => {
  if (!await fetchUser()) {
    logout()
    router.replace('login')
  }
})

</script>

<template>
  <TopMenu v-if="!isLoginOrRegisterOrUndefined" />
  <router-view>
  </router-view>
</template>
