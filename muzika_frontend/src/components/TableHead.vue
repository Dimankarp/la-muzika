<script setup>

const { id, sortInfo } = defineProps(['id', "sortInfo"])
</script>

<template>
    <th @click.prevent="()=>{
        sortInfo.sortChange(id);
     }">
        <slot />
        <span>
            {{sortInfo.id===id ? (sortInfo.dir !==null ? (sortInfo.dir === 'desc' ? '▼': '▲') : '') : ''}}
        </span>
    </th>
</template>