<template>
  <div class="commentList">
     <div class="comment" v-for="(item,index1) in commentList " :key="index1">
        <div class="left">
          <img v-if="item.user" :src="item.user.avatar">
          <img v-else src="../../../assets/3.jpg" >
        </div>
        <div class="right">
          <div class="rightTop">
              <div class="NickName" style="color:#e94894">{{item.user.nickName}}</div>
              <div class="topTime" >
                {{item.createTime}}
              </div>
          </div>
          <div class="rightContent">

            <!-- 子评论列表,递归 -->
             <el-card v-if="item.replyList.length>0">
               <childList :replyList="item.replyList"
               @initLst="initLst"
               :source="source"></childList>
             </el-card>

             <div class="content" v-html="item.content"></div>

          </div>
          <div class="rightBottom">
              <div class="replayButton" @click="turnShowBox(index1)"
              v-if="currentIndex!=index1"
              > 回复</div>
              <div class="replayButton" v-else @click="cancel">取消回复</div>
          </div>

          <commentBox v-show="currentIndex==index1"
           @submit="submit" ></commentBox>
        </div>
     </div>
  </div>
</template>

<script>
import "../../../assets/css/emoji.css"
import {addComment} from '../../../api/comment'
export default {
   data(){
     return{
      valueList:[],
      currentIndex:-1,
      currentUid:"",
     }
   },
   components:{
     childList:resolve=>require(['../replyList/index'],resolve),
     commentBox:resolve=>require(['../CommentBox/index'],resolve)
   },
   watch:{
   },
   props:{
     commentList:{
       type:Array
     },
     source:{
       type:String
     }
   },
   methods:{
     turnShowBox(e){
       this.currentIndex=e
       this.currentUid=this.commentList[e].uid
     },
     initLst(){
       this.$emit("initLst")
     },
     submit(e){
       let param={};
       param.source=this.source
       param.content=e;
       param.userUid=sessionStorage.getItem("userUid");
       param.toUid=this.currentUid;
       param.toUserUid=this.commentList[this.currentIndex].user.uid
       if(this.source=="BLOG_INFO"){
         param.blogUid=JSON.parse(sessionStorage.getItem("articleInfo")).uid
       }
       addComment(param).then(res=>{
         if(res.code=="success"){
           console.log(res.data)
           var comment=res.data
           this.$message.success("评论成功");
           this.initLst()
         }else{
           if(res.code==401){
             this.$message.warning("请先登录")
           }else{
             this.$message.error("系统故障,请稍后尝试")
           }
         }
       })
     },
     cancel(){
       this.currentIndex=-1
     }
   }
}
</script>

<style lang="less" scoped>
  @import "../../../assets/css/emoji.css";
   .commentList{
       .comment{
         margin: 20px 0;
         border-top: 1px solid #dddddd;
         padding: 10px 0;
         box-sizing: border-box;
         display: flex;
         .left{
            flex: 1;
            img{
              width: 45px;
              height: 45px;
              border-radius: 50%;
            }
         }
         .right{
           display: flex;
           flex-direction: column;
           flex: 9;
           .rightTop{
             display: flex;
             justify-content: space-between;
             margin-bottom: 10px;
             .topTime{
               color:#a6a5be ;
               font-size: 14px;
             }
           }
           .content{
             margin-top: 5px;
           }
           .rightBottom{
             display: flex;
             justify-content: flex-end;
             margin-top: 10px;
             .replayButton{
               font-size: 14px;
               color: #a6a5be;
               cursor: pointer;
             }
               .replayButton:hover{
               color: red;
             }
           }
         }
       }
   }

</style>
