<script setup>
import { RouterView, useRoute, useRouter } from 'vue-router'
import { computed, onBeforeMount, watch } from "vue";
import TopMenu from '@/components/TopMenu.vue';
import { userStore } from '@/js/store';
import { tryLogin } from '@/js/auth';

const route = useRoute()
const isLoginOrRegister = computed(() => route.name === "login" || route.name === "register")

const router = useRouter()


watch(userStore, (newLoggedIn) => {
  console.log(newLoggedIn)
  if (!newLoggedIn) {
    router.replace({name:"login"});
  }
})



onBeforeMount(() => {
  if(!userStore.isLoggedIn){
    if (!tryLogin()){
      logout()
      router.replace({name:"login"});
    }
  }
})

</script>

<template>
  <TopMenu v-if="!isLoginOrRegister" />
  <router-view>
  </router-view>
</template>
