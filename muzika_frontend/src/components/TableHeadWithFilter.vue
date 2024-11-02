<script setup>
import { ref } from 'vue';

const { id, sortInfo } = defineProps(['id', 'sortInfo']);
const isInputVisible = ref(false);
const inputValue = ref('');

const openFilter = () => {
    isInputVisible.value = true;
};

const closeFilter = () => {
    isInputVisible.value = false;
    inputValue.value = ''
    sortInfo.updateFilter(id, inputValue.value);
};

const handleInputChange = (event) => {
    inputValue.value = event.target.value;
    sortInfo.updateFilter(id, inputValue.value);
};
</script>
<template>
    <th>
        <div @click.prevent="() => {
            sortInfo.sortChange(id);
        }">
            <slot />
            <span>
                {{ sortInfo.id === id ? (sortInfo.dir !== null ? (sortInfo.dir === 'desc' ? '▼' : '▲') : '') : '' }}
            </span>
        </div>
        <div class="filter-input">
            <button v-if="!isInputVisible" @click="openFilter">
                <i class="filter-icon">🔍</i>
            </button>
            <div v-else>
                <input type="text" v-model="inputValue" @input="handleInputChange" />
                <button @click="closeFilter">✖</button>
            </div>
        </div>
    </th>
</template>

<style scoped>
.filter-input {
    display: flex;
    align-items: center;
}

.filter-input button {
    background: none;
    border: none;
    cursor: pointer;
}

.filter-input input {
    margin-right: 5px;
}
</style>