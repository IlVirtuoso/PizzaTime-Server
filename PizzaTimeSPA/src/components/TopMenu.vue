<script setup lang="ts">
import { ref } from 'vue';


</script>


<template>
<el-menu :default-active="'1'" class="" mode="horizontal">
  <el-menu-item index="1">Menu1</el-menu-item>
  <el-submenu index="2">
    <template #title>Menu2</template>
    <el-menu-item index="2-1">SubItem</el-menu-item>
    <el-menu-item index="2-2">SubItem</el-menu-item>
    <el-menu-item index="2-3">SubItem</el-menu-item>
  </el-submenu>
  <el-menu-item index="3" disabled>Disabled</el-menu-item>
</el-menu>
</template>

<style scoped>
.read-the-docs {
    color: #888;
}
</style>
