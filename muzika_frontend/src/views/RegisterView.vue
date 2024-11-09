<script setup>
import RegisterForm from '@/components/RegisterForm.vue';
import {useRouter} from 'vue-router'

const router = useRouter()

function redirectToBandsPage(){
    router.push("bands")
}

</script>

<template>
  <RegisterForm @registration-success="redirectToBandsPage"/>
  <router-link to="login" class="login-link">
    or login! </router-link>
</template>
