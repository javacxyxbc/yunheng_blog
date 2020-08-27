<template>
  <el-card class="recommendArticle">
  
    <div class="title">
       <svg t="1598103339830" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5240" width="20" height="20"><path d="M511.98893 472.763855a91.349971 91.349971 0 0 1-40.908523-9.607305L17.640424 236.386973 471.129993 9.604999a91.734263 91.734263 0 0 1 81.73027 0l453.551551 226.781974L799.675016 339.79876a30.491725 30.491725 0 0 1-27.272348-54.544697l97.796162-48.891883-344.623312-172.311656a30.644202 30.644202 0 0 0-27.272349 0l-344.623312 172.311656 344.623312 172.311656a30.54503 30.54503 0 0 0 27.272349 0l125.031321-62.528057a30.491725 30.491725 0 1 1 27.272348 54.544697l-125.031321 62.478471a91.362367 91.362367 0 0 1-40.858936 9.594908zM511.98893 1024a91.473936 91.473936 0 0 1-32.057406-5.801572l-4.6363-2.206581L70.350436 780.395946a30.445858 30.445858 0 1 1 30.619409-52.586047l402.341519 234.03394a30.892133 30.892133 0 0 0 17.355131 0L923.008014 727.809899a30.449577 30.449577 0 0 1 30.631806 52.635633L544.046336 1018.198428a91.42435 91.42435 0 0 1-32.057406 5.801572z" p-id="5241" fill="#44d7b6"></path><path d="M511.98893 766.239117a91.027661 91.027661 0 0 1-32.082199-5.826365l-4.611507-2.181788-404.944788-235.533918a30.489246 30.489246 0 0 1 30.619409-52.734805l402.353916 234.058732a30.111152 30.111152 0 0 0 17.355131 0L923.008014 469.962241a30.458255 30.458255 0 0 1 30.631806 52.648029L544.083525 760.363166a91.13923 91.13923 0 0 1-32.094595 5.875951z" p-id="5242" fill="#44d7b6"></path></svg>
       <div class="hotTitle">推荐文章</div>
    </div>
    <div class="articleList">
        <el-scrollbar style="height:100%">
        <div class="article" v-for="(item,index) in list" :key="index"
        @click="gotoArticle(item.uid)">
          <i :title="item.title" class="name">{{item.title}}</i>
          <div class="cnt">({{item.clickCount}})</div>
        </div>
        </el-scrollbar>
    </div>
  </el-card>
</template>

<script>
export default {
   props:{
     list:{
       type:Array
     }
   },
   methods:{
      gotoArticle(uid){
          var info={};
          info.uid=uid;
          sessionStorage.setItem("articleInfo",JSON.stringify(info))
          this.$router.go(0)
      },
   }
}
</script>

<style lang="less" scoped>
.recommendArticle{
  .title{
    color: #44d7b6;
    font-size: 14px;
    display: flex;
    .hotTitle{
      margin-left: 10px;
    }
  }
  .articleList{
    height:300px;
    display: flex;
    flex-direction: column;
     overflow-x: hidden;
    /deep/.el-scrollbar__wrap{
      overflow-x: hidden;
    }
    /deep/.el-scrollbar__bar{
      width: 3px;
    }
    .article{
      justify-content: space-around;
      margin: 10px 0;
      cursor: pointer;
      display: flex;
      color: #8b69a8;
      font-size: 13px;
      .name{
        overflow: hidden;
        text-overflow:ellipsis;
        white-space: nowrap;
        width: 55%;
      }
    }
  }
}

</style>