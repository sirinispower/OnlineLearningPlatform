import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 屏蔽 ResizeObserver 无害报错（Element Plus 等组件常见）
const suppressResizeObserverError = (event) => {
  const message = event?.message || event?.reason?.message || ''
  if (message.includes('ResizeObserver loop')) {
    event.stopImmediatePropagation?.()
    event.preventDefault?.()
    return true
  }
  return false
}

window.addEventListener('error', (event) => {
  suppressResizeObserverError(event)
})

window.addEventListener('unhandledrejection', (event) => {
  suppressResizeObserverError(event)
})

app.use(router)
app.use(ElementPlus)
app.mount('#app')
