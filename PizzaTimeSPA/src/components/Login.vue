<script setup lang="ts">
import { ref } from 'vue';

const loginForm = ref();

const username = ref('');
const password = ref('');

function login() {
    console.log("Username is ", username , " and password is ", password);
}

</script>


<template>
 <el-form  ref="loginForm" :model="loginForm">
      <el-form-item prop="username">
        <el-input v-model="username" placeholder="Username" />
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="password" placeholder="Password" type="password" />
      </el-form-item>
      <el-button class="login-btn" type="primary" @click="login">Login</el-button>
    </el-form>
</template>

<style scoped>
.read-the-docs {
    color: #888;
}
</style>
