<template>
  <div class="sort">
      <div class="summary">共计 {{total}} 个分类</div>
      <div class="sortLabel">
           <div class="label" v-for="(item,index) in sortList" :key="index" @click="gotoSort(item.uid)">
             <span class="sortName">{{item.sortName}}</span>
             <span class="nums">({{item.cnt?item.cnt:0}})</span>
           </div>
      </div>   
  </div> 
</template>

<script>
import { getBlogSortList } from '../../api/classify'
export default {
    data(){
      return{
         total:10,
         sortList:[]

      }
    },
    created(){
       getBlogSortList().then(res=>{
        this.total=res.data.total;
        this.sortList=res.data.records
      })
    },
    methods:{
      gotoSort(uid){
         var type={};
         type.uid=uid;
         type.type="sort";
         sessionStorage.setItem("listType",JSON.stringify(type));
         this.$router.push("/indexView")
      }
    }
}
</script>

<style lang="less" scoped>
.sort{
   background-color: white;
   width: 90%;
   display: flex;
   padding: 20px 40px;
   box-sizing: border-box;
   flex-direction: column;
   .summary{
     align-self: center;
   }
   .sortLabel{
     display: flex;
     flex-wrap: wrap;
      .label{
            cursor: pointer;
        margin: 10px;
        .sortName{
          font-size: 15px;
          color:#555555 ;
          text-decoration: none;
          word-wrap: break-word;
          border-bottom: 1px solid #999;
        }
        .nums{
          font-size: 14px;
          margin-left: 5px;
          color:#bdbdbd ;
        }
        } 
   }
}

</style>