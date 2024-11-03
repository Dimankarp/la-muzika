<script setup>
import { useRouter } from 'vue-router';
import { userStore } from '@/js/store';
import { onMounted, ref } from 'vue';

const router = useRouter();
const isPending = ref(false)
const navigateTo = (route) => {
  router.push({ name: route });
};

console.log(userStore.isAdmin)

const logout = () => {
  userStore.logout()
  router.replace('login')
};

const onRequestAdmin = async ()=>{
  const response = await fetch(`/api/requests`, {
    method: "POST"
  });
  if (response.ok) {
    isPending.value = true;
  } else {
    isPending.value = false;
    console.log("Failed to delete because of " + response.body);
  }
}

onMounted(async () => {
  if(!userStore.isAdmin){
  const response = await fetch(`/api/requests/pending`, {
    method: "GET"
  });
  if (response.ok) {
    isPending.value = await response.json()
  } else {
    isPending.value = false;
    console.log("Failed to delete because of " + response.body);
  }
}
})

</script>

<style scoped>
.top-menu {
  display: flex;
  justify-content: space-between;
  background-color: #f8f9fa;
  padding: 10px;
  border-bottom: 1px solid #dee2e6;
}

.top-menu button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  font-size: 16px;
  border-radius: 4px;
}

.top-menu button:hover {
  background-color: #0056b3;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-info span {
  margin-right: 10px;
}

.user-info button {
  background-color: #dc3545;
}

.user-info button:hover {
  background-color: #c82333;
}
</style>

<template>
  <div class="top-menu">
    <button @click="navigateTo('bands')">Bands</button>
    <button @click="navigateTo('albums')">Albums</button>
    <button @click="navigateTo('studios')">Studios</button>
    <button @click="navigateTo('actions')">Actions</button>
    <button v-if="userStore.isAdmin" @click="navigateTo('requests')">Admin Requests</button>
    <button @click="navigateTo('visual')">Visualization</button>

    <div class="user-info">
      <span>{{ userStore.username }} ({{ userStore.isAdmin ? 'Admin' : 'User' }})</span>
      <button @click="logout">Logout</button>
      <button v-if="!userStore.isAdmin" :disabled="isPending" @click="onRequestAdmin">RequestAdmin</button>
    </div>
  </div>
</template>
