<script setup>
import LoginForm from '@/components/LoginForm.vue';
import { useRouter } from 'vue-router'
const router = useRouter()

function redirectToTablePage() {
  router.push({name: 'table'})
}

</script>

<template>
  <LoginForm @login-success="redirectToTablePage" />
  <router-link to="/register" class="register-link">
    or register new account! </router-link>
</template>
