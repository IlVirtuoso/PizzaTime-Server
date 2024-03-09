<script setup lang="ts">
import { ref } from 'vue';

const index = defineModel('index')

const menu = ref();


function change(key: string, keypath : string[]){
  index.value = key;
}

</script>


<template>
<el-menu :default-active="'1'" class="navbar el-menu" mode="horizontal" text-color="#2f3640" active-text-color="#409eff"  :model="menu" @select="change">
    <el-menu-item index="0">
      <p>logo here</p>
    </el-menu-item>
  <el-menu-item index="1">Menu1</el-menu-item>
  <el-sub-menu index="2">
    <template #title>Menu2</template>
    <el-menu-item index="2-1">Item 1</el-menu-item>
    <el-menu-item index="2-2">Item 2</el-menu-item>
  </el-sub-menu>
</el-menu>
</template>

<style scoped>
.read-the-docs {
    color: #888;
}
.navbar {
  background: transparent;
  border-bottom: solid 1px #2f3640;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 999;

}
</style>
