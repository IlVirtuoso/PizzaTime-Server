import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import { ApplicationServices } from './data/ApplicationServices';
import MockBridge from './data/mockBridge';
import Bridge from './data/bridge';

createApp(App).use(ElementPlus).mount('#app')

ApplicationServices.Instance()
.AddApplicationBridge(new MockBridge());
//.AddApplicationBridge(new Bridge());
