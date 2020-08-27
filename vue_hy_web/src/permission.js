import router from './router'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css'// Progress 进度条样式
import { Message } from 'element-ui'
import { getToken } from '@/utils/auth' // 验权
const BlackList = ['/personCenter'] // 重定向黑名单
router.beforeEach((to,from,next)=>{
    NProgress.start()
    if(getToken()){
       next()
    }else{
       if(BlackList.indexOf(to.path)!==-1){
           Message.success("请先登录")
           next("/login")
       }else{
           next()
           NProgress.done()
       }
    }

})

router.afterEach(() => {
    NProgress.done() // 结束Progress
  })
  