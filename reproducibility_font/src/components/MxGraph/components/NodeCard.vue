<template>
  <div class style="width:100%">
    <el-col :span="24">
      <div class="card">
        <!-- <img
          v-bind="nodeInfo"
          :src="`/static/images/ele/${nodeInfo}`"
          :alt="nodeInfo.title"
        />-->
        <!-- <i :class="nodeInfo"></i> -->
        <div>
          <span>{{ nodeInfo.toolName }}</span>
          <div class="bottom clearfix">
            <!-- <time class="time">{{ currentDate }}</time> -->
          </div>
        </div>
      </div>
    </el-col>
  </div>
</template>

<script>
export default {
  props: {
    item: {
      type: Object
    }
  },
  watch: {
    item: {
      handler(val) {
        this.nodeInfo = val;
      },
      deep: true
    }
  },
  components: {},
  data() {
    return {
      nodeInfo: this.item
    };
  },
  methods: {},
  created() {},
  mounted() {}
};
</script>
<style scoped>
.card {
  text-align: center;
  height: 50px;
  line-height: 50px;
  margin-bottom: 20px;

  transition: all 0.2s ease-in-out;
  border-radius: 4px;
  background: #ffffff;
  box-shadow: 0px 1.6px 3.6px rgba(0, 0, 0, 0.13), 0px 0px 2.9px rgba(0, 0, 0, 0.11);
}
.card:hover {
  cursor: pointer;
  box-shadow: 0px 0px 5px 1px rgba(27, 94, 238, 0.2);
}
</style>
