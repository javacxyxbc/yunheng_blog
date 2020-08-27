<template>
  <div class="reply">
      <div class="replayTemplate" v-for="(item2,index2) in replyList" :key="index2">
         <div class="top">
           <div class="replyTo" >{{item2.user.nickName}} 回复 {{item2.toUser.nickName}}</div>
           <div class="time">{{item2.createTime}}</div>
         </div>
         <div class="content">{{item2.content}}</div>
          <div class="rightBottom">
              <div class="replayButton" @click="turnShowBox(index2)"
              v-if="currentIndex!=index2"
              >回复</div>
              <div class="replayButton"
              v-else
              @click="cancelShow">取消回复</div>
          </div>
          <!-- 回复组件 -->
          <commentBox v-show="currentIndex==index2" @submit="submit"></commentBox>
          <!-- 递归 -->
         <childList :replyList="item2.replyList" :source="source"
         @initLst="initLst"
         ></childList>
      </div>
  </div>
</template>

<script>
import "../../../assets/css/emoji.css"
import {addComment} from '../../../api/comment'
export default {
  data(){
    return{
      showBox:[],
      currentIndex:-1,
      currentUid:""
    }
  },
  components:{
     childList:resolve=>require(['./index'],resolve),
     commentBox:resolve=>require(['../CommentBox/index'],resolve)
  },
  props:{
    replyList:{
      type:Array
    },
    source:{
      type:String
    }
  },
  mounted(){
      var len=this.replyList.length
       this.showBox=new Array(len)
       for(var i=0;i<len;i++){
         this.showBox[i]=false
       }
  },
   methods:{
     initLst(){
       this.$emit("initLst")
     },
     turnShowBox(e){
       this.currentIndex=e;
       this.currentUid=this.replyList[e].uid
     },
     cancelShow(){
       this.currentIndex=-1
       this.currentUid=""
     },
     submit(e){
       let param={};
       param.source=this.source
       param.content=e;
       param.userUid=sessionStorage.getItem("userUid");
       param.toUid=this.currentUid;
       param.toUserUid=this.replyList[this.currentIndex].user.uid
       if(this.source=="BLOG_INFO"){
         param.blogUid=JSON.parse(sessionStorage.getItem("articleInfo")).uid
       }
       addComment(param).then(res=>{
         if(res.code=="success"){
           console.log(res.data)
           var comment=res.data
           this.$message.success("评论成功");
           this.$emit("initLst")
         }else{
           if(res.code==401){
             this.$message.warning("请先登录")
           }else{
             this.$message.error("系统故障,请稍后尝试")
           }
         }
       })
     },
   }
}
</script>

<style lang="less" scoped>
   .reply{
     .replayTemplate{
       display: flex;
       flex-direction: column;
       margin: 10px 0;

     }
     .top{
       display: flex;
       justify-content: space-between;
       font-size: 14px;
       color: #a6a5be;
       margin-bottom: 5px;

     }
     .content{
       font-size: 15px;
       color: #333333;
     }
           .rightBottom{
             display: flex;
             justify-content: flex-end;
             margin-top: 10px;
             .replayButton{
               font-size: 14px;
               color: #a6a5be;
               cursor: pointer;
               margin-bottom: 10px;
             }
             .replayButton:hover{
               color: red;
             }
           }
}

  .emoji-panel-btn img{
    height: 20px;
    margin:0 5px;
    width: 20px;
  }
  .emoji-panel-btn:hover {
    cursor: pointer;
    opacity: 0.8;
  }
  .emoji-item-common {
    background: url("../../../assets/img/emoji_sprite.png");
    display: inline-block;
  }
  .emoji-item-common:hover {
     cursor: pointer;
   }
  .emoji-size-small {
    // 表情大小
    zoom: 0.3;
  }
  .emoji-size-large {
    zoom: 0.5; // emojipanel表情大小
    margin: 4px;
  }
</style>
