<template>
  <el-card class="detail">
    <div class="articleDetail" v-if="data">
      <!-- 头部基本数据 -->
       <div class="title" >{{data.title}}</div>
       <div class="sort">
         <div class="sortName">分类于  {{data.blogSort.sortName}}</div>
         <div class="time">发布于: {{data.createTime}}</div>
       </div>
       <div class="tagAndData">
          <div>标签:
              <el-tag style="margin:0 5px" type="success" v-for="(item,index) in data.tagList" :key="index">
              {{item.content}}
              </el-tag>
          </div>
          <div class="data">
          <div class="clickCount">浏览量: {{data.clickCount}}</div>
          <div>点赞量: {{data.priseCount}}</div>
        </div>
       </div>



       <!-- 图片区域 -->
       <el-image
            style="width: 100%; height: auto;margin:30px 0"
            :src="data.url"
            :preview-src-list="data.url.split(',')">
      </el-image>



      <!-- 文章内容区域   -->
      <mdView :content="data.content"></mdView>
    </div>
  </el-card>
</template>

<script>
export default {
   data(){
       return{
         commentCount:10
       }
     },
   components:{
     mdView:resolve=>require(['../editor/mdView'],resolve)
   },
   props:{
     data:{
       type:Object
     }
   }
}
</script>

<style lang="less" scoped>
.detail{
      margin-bottom: 20px;
  .articleDetail{
    display: flex;
    flex-direction: column;
    .title{
      font-size: 25px;
      color: #616161;
      font-weight: bold;
    }
    .sort{
      margin: 15px 0;
      color: #858585;
      font-size: 14px;
      display: flex;
      .sortName{
        margin-right: 20px;
      }
    }
    .tagAndData{
      color: #858585;
      font-size: 14px;
      display: flex;
      justify-content: space-between;
    }
    .data{
      display: flex;
      color: #858585;
      font-size: 14px;
      .clickCount{
        margin-right: 20px;
      }
    }
  }
}

</style>
