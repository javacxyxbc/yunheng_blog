<!--文章预览页面-->

<template>
  <div class="article">

      <div class="left">
        <detail :data="detailData"></detail>

        <comment></comment>
      </div>

      <div class="right">
         <resume class="resume" @turnPay="turnState"></resume>
         <recommendArticle class="articles" :list="recommendArticles"></recommendArticle>
         <recommendSort class="sorts" :list="recommendSorts"></recommendSort>
      </div>





      <el-dialog
       :visible.sync="PayVisible"
        width="30%"
        :before-close="turnState"
      >
        <payCode ></payCode>


      </el-dialog>
  </div>
</template>

<script>

import {getArticleById,getRecommendBlogList,getRecommendSorts} from '../api/article'
export default {
  data(){
    return{
       content:"",
       detailData:null,
       recommendArticles:[],
       recommendSorts:[],
       PayVisible:false
    }
  },
  components:{
       detail:resolve=>require(['../components/article/articleDetails'],resolve),
       payCode:resolve=>require(['../components/PayCode/index'],resolve),
       resume:resolve=>require(['../components/article/myResume'],resolve),
       recommendSort:resolve=>require(['../components/article/recommendSort'],resolve),
       recommendArticle:resolve=>require(['../components/article/recommendArticle'],resolve),
       comment:resolve=>require(['../components/article/comment'],resolve)
  },
  created(){
    var info=JSON.parse(sessionStorage.getItem("articleInfo"));
    var param={uid:info.uid}
    getArticleById(param).then(res=>{
      this.detailData=res.data

    })
    getRecommendBlogList(param).then(res=>{
      this.recommendArticles=res.data.records
    }),
    getRecommendSorts().then(res=>{
      this.recommendSorts=res.data.records

    })
  },
  methods:{
    turnState(){
      this.PayVisible=!this.PayVisible
    }
  }
}
</script>

<style lang="less" scoped>
.article{
      width: 1100px;
      height: 100%;
      margin: 0 auto;
      display: flex;
      background-color: #f4f4f4;
      justify-content: space-between;
      .left{
        flex:12;
        min-height: 1000px;
        margin-right: 15px;
      }
      .right{
        min-height: 1000px;
        flex:4;
        display: flex;
        flex-direction: column;
        .resume{
          margin-bottom: 15px;
        }
        .articles{
          margin-bottom: 15px;
        }
        .sorts{
        }
      }
}

</style>
