<template>
    <div class="menu">
        <button @click="navigateTo('Home')">Home</button>
        <button @click="navigateTo('Vanya')">Vanya</button>
        <button @click="navigateTo('VanyaHome')">Vanya Home</button>
    </div>
</template>

<script>
export default {
    name: 'Menu',
    methods: {
        navigateTo(route) {
            this.$router.push({ name: route });
        }
    }
}
</script>

<style scoped>
.menu {
    background-color: beige;
    padding: 10px;
    display: flex;
    gap: 10px;
}

button {
    padding: 10px 20px;
    border: none;
    background-color: #f5f5dc;
    cursor: pointer;
}

button:hover {
    background-color: #e0e0d1;
}
</style>