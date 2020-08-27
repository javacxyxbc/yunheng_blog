<template>
  <div >
      <el-card class="comment">
        <div class="commentTop">
          <div class="TopTitle">
            <svg t="1598364523636" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2242" width="25" height="25"><path d="M139.848 763.258c-11.561-19.62-19.128-32.781-22.89-39.806A446.103 446.103 0 0 1 64 511.997C64 264.581 264.581 64 511.998 64 759.419 64 960 264.581 960 511.997 960 759.42 759.419 960 511.998 960a446.024 446.024 0 0 1-230.077-63.528 3021.71 3021.71 0 0 0-21.057-12.356l-109.628 29.905c-25.086 6.835-48.092-16.18-41.247-41.262l29.86-109.501z m68.563 3.98l-18.118 66.47 66.47-18.124a33.596 33.596 0 0 1 25.747 3.373 4571.82 4571.82 0 0 1 33.972 19.893 378.83 378.83 0 0 0 195.516 53.946c210.317 0 380.803-170.482 380.803-380.799 0-210.312-170.486-380.798-380.803-380.798-210.312 0-380.799 170.486-380.799 380.798 0 63.616 15.591 124.903 44.969 179.694 3.897 7.26 13.576 23.969 28.74 49.593a33.595 33.595 0 0 1 3.503 25.953z m153.295-74.316a33.603 33.603 0 0 1-11.971-31.644 33.602 33.602 0 0 1 54.936-20.02 167.151 167.151 0 0 0 107.327 38.739c39.785 0 77.36-13.833 107.296-38.73 14.274-11.863 35.459-9.91 47.322 4.358 11.864 14.27 9.91 35.458-4.359 47.322a234.339 234.339 0 0 1-150.26 54.254 234.38 234.38 0 0 1-150.29-54.279z" p-id="2243"></path></svg>
            <span class="title">评论</span>
           </div>
          <div class="sum">
             <span class="sumNum">{{commentList.length}}</span>条评论
          </div>
        </div>

        <el-divider></el-divider>


        <!--评论列表-->
        <commentList :commentList="commentList"></commentList>

      </el-card>
  </div>
</template>

<script>
import {getCommentList} from '../../api/comment'
export default {
    data(){
      return{
         commentList:[],
         source:"BLOG_INFO",
         currentPage:0,
         pageSize:10
      }
    },
    components:{
      commentBox:resolve=>require(['./CommentBox'],resolve),
      commentList:resolve=>require(['../comment/CommentList'],resolve)
    },
    created(){
       var param={}
       param.source=this.source;
       param.currentPage=this.currentPage;
       param.pageSize=this.pageSize;
       param.blogUid="d45510bedba2b7e6c7dcf65950d65176"
       getCommentList(param).then(res=>{
         console.log(res.data)
         this.commentList=res.data.records
         this.currentPage=res.data.currentPage;
         this.pageSize=res.data.pageSize
       })
    }
}
</script>

<style lang="less" scoped>
.comment{
   .commentTop{
       .TopTitle{
         margin: 20px 0;
         display: flex;
         align-items: center;
       }
       .title{
         margin-left: 5px;
         font-weight: bold;
       }
       .sumNum{
         color:#44d7b6 ;
         margin-right: 5px;
       }
   }
}

</style>
