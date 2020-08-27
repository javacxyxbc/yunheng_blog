<template>
  <div class="share">
    <div class="shang_box" >
      <div class="shang_tit">
        <p>感谢您的支持，我会继续努力的!</p>
      </div>
      <div class="shang_payselect">
        <div
          class="pay_item"
          @click="choosePay('1')"
        >
          <span class="pay_logo">
            <img src="../../assets/pay/wechatPay.jpg" alt="微信">
          </span>
        </div>
        <div
          data-id="weipay"
          @click="choosePay('2')"
        >
          <span class="pay_logo">
            <!-- <img src="../../assets/pay/wechatPay.jpg" alt="微信"> -->
          </span>
        </div>
      </div>
      <div class="pay_explain">扫码打赏，你说多少就多少</div>      
    </div>
  </div>
</template>

<script>
export default {
  name: "PayCode",
  data() {
    return {
      showPay: true, //是否显示支付
      payMethod: 1, // 1: 支付宝  2：微信
      payCode: "", //支付码图片
    };
  },
  methods: {
    dashangToggle: function() {
      this.showPay = !this.showPay;
    },
    // 支付方式
    choosePay: function(type) {
      this.payMethod = type;
      if (type == 1) {
        this.payCode = this.webConfigData.aliPayPhoto;
      } else {
        this.payCode = this.webConfigData.weixinPayPhoto;
      }
    },
  }
};
</script>

<style lang="less" scoped>
  .diggit {
    cursor: pointer;
    
  }
  .shang_box{
    display: flex;
    flex-direction: column;
    align-items: center;
    .shang_tit{
      color: #c5c5c5;
    }
    .shang_payselect{
      img{
        width: 300px;
         height: 300px;
      }
    }
  }
</style>
