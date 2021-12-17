<template>
  <div>
    <vue-scroll style="height: 700px" :ops="ops">
      <div class="context">
        <el-row :gutter="20">
          <el-col :span="8"><context-card :type="'EssentialInformation'"/></el-col>
          <el-col :span="8"><context-card /></el-col>
          <el-col :span="8"><context-card /></el-col>
          <el-col :span="8"><context-card /></el-col>
          <el-col :span="8"><context-card /></el-col>
          <el-col :span="8"><context-card /></el-col>
          <!-- <el-col :span="8"><context-card /></el-col> -->
        </el-row>
        
      </div>
    </vue-scroll>
    
  </div>
</template>

<script>
import contextCard from './ContextCard.vue';
export default {
  data() {
    return {
      ops: {
        bar: {
          onlyShowBarOnScroll: false,
          keepShow: true,
          background: '#c1c1c1',
          opacity: 1,
          hoverStyle: false,
          specifyBorderRadius: false,
          minSize: 0,
          size: '6px',
          disable: false,
        },
      }
    };
  },
  components: {
    contextCard,
  },
  methods: {

  }
};
</script>

<style lang="scss" scoped>
.context {
  padding-left: 20px;
  padding-right: 20px;
}
</style>