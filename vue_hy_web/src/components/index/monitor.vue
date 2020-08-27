<template>
  <div>
      <el-button type="success" @click="ReleaseArticle">发布文章</el-button>
      <el-button type="warning">发布动态</el-button>
  </div>
</template>

<script>
export default {
  methods:{
      ReleaseArticle(){
          this.$router.push("/ReleaseArticle")
      }
  }
}
</script>

<style>

</style>