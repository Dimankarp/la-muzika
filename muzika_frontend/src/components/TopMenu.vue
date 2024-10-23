<template>
  <div class="top-menu">
    <button @click="navigateTo('table')">Table</button>
    <button @click="navigateTo('table')">Editing</button>
    <button @click="navigateTo('table')">Visualisation</button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const navigateTo = (route) => {
  router.push({ name: route });
};
</script>

<style scoped>
.top-menu {
  display: flex;
  justify-content: space-around;
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
</style>
