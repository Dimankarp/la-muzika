<script setup>
import { useRouter } from 'vue-router';
import { userStore } from '@/js/store';

const router = useRouter();

const navigateTo = (route) => {
  router.push({ name: route });
};

console.log(userStore.isAdmin)

const logout = () => {
  userStore.logout()
  router.replace('login')
};
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
    <div class="user-info">
      <span>{{ userStore.username }} ({{ userStore.isAdmin ? 'Admin' : 'User' }})</span>
      <button @click="logout">Logout</button>
    </div>
  </div>
</template>
