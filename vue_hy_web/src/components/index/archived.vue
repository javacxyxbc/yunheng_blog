<!--归档子页面-->
<template>
  <div class="archived" >
      <!--头部总计-->
     <div class="sumaryze">
         <div  class="SumpointDeco"></div>
         非常好！ 目前共计 {{total}} 篇文章， 继续努力。 </div>
     <!--主题循环内容-->
     <div class="archivedContent" >
         <div class="archivedContentMask" v-for="(item,index) in articleList" :key="index">
             <h3 class="contentYear">
             <div  class="yearpointDeco"></div>
             {{item.year}}</h3>
            <div class="circurationContent" 
            @click="gotoArticle(item1.uid)"
            v-for="(item1,index1) in item.articles" :key="index1">
                <!--点缀,日期,内容-->
                <div  class="pointDeco"></div>
                <div class="releaseTime">{{item1.createTime}}</div>
                <div class="articleTitle">{{item1.title}}</div>
            </div>
         </div>
     </div>
  </div>
</template>

<script>
import {getSortList} from '../../api/sort'
export default {
   data(){
       return{
           articleList:[],
           total:0,
       }
   },
   created(){
       getSortList().then(res=>{
  
           this.articleList=res.data.records
           this.total=res.data.total;
       })
   },
   methods:{
     gotoArticle(uid){
        console.log(uid)
        var info={};
        info.uid=uid;
        sessionStorage.setItem("articleInfo",JSON.stringify(info))

        this.$router.push("/article");
    },      
   }
}
</script>

<style lang="less" scoped>
.archived{
    padding-top: 50px;
    padding-left: 25px;

    width: 90%;
    box-sizing: border-box;
        min-height: 900px;
        background-color: white;
   .archivedContent{
       margin-top: 50px;
       .circurationContent{
           cursor: pointer;
          border-bottom: 1px dashed #ccc;
          padding-bottom: 20px;
           display: flex;
           margin-bottom: 25px;
           align-items: center;
           transition: 1s;
           .pointDeco{
               margin-right: 20px;
               width: 8px;
               height: 8px;
               border-radius: 50%;
               background-color: #bbbbbb;
           }
           .releaseTime{
               color:#855555 ;
               font-size: 13px;
               margin-right: 10px;
           }
           .articleTitle{
               color: #666666;
           }
       }
       .contentYear{
           margin-bottom: 50px;
           display: flex;
           align-items: center;
            .yearpointDeco{
              margin-right: 15px;
               width: 10px;
               height: 10px;
               border-radius: 50%;
               background-color: #bbbbbb;
           
         } 
      }
    
   }
}
.sumaryze{
    
    display: flex;
    font-size: 15px;
}
.SumpointDeco{

    margin-right: 15px;
    width: 10px;
    height: 10px;
    border-radius: 50%;
    background-color: #bbbbbb;
}
.circurationContent:hover .pointDeco{
    background-color: black;
    height: 18px;
}

</style>