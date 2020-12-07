<template>
  <div>
    <el-container>
      <el-header height="100px">
        <router-view name="header"></router-view>
      </el-header>
      <el-main>
        <router-view name="main"></router-view>
      </el-main>
      <el-footer>
        <router-view name="footer"></router-view>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
export default {};
</script>

<style scoped>
.el-header {
  padding: 0;
}
.el-footer {
  padding: 0;
}
.el-main {
  padding: 0;
}
</style>
