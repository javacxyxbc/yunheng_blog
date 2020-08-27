<template>
  <div>
    <svg-icon icon-class="question" style="height: 2.5em;font-size: 18px;cursor: pointer" @click.native="goto"/>
  </div>
</template>

<script>
export default {
  name: 'MoGuDoc',
  data() {
    return {
      url: 'https://github.com/hzh0425/yunheng-blog'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
