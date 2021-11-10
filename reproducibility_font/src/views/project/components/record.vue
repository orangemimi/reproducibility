<template>
  <div class="main">
    <el-row>
      <el-col :span="1">
        <div>
          <el-avatar :size="50">user</el-avatar>
        </div>
      </el-col>
      <el-col :span="23">
        <div class="card">
          <div class="card-top">
            <div class="text">123</div>
            <div class="text">123</div>
            <div class="text">123</div>
          </div>
          <div class="card-bottom"></div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {};
</script>

<style lang="scss" scoped>
.main {
  height: 100%;
  .card {
    //   background: black;
    height: 100%;
    width: 100%;

    .card-top {
      height: 50px;
      display: flex;
      .text {
        line-height: 50px;
        margin-right: 3px;
      }
    }
    .card-bottom {
      height: 200px;
      background-color: #f6f6f6;
      border-radius: 4px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
    }
  }
}
</style>