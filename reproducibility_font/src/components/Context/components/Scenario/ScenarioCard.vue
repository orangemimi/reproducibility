<template>
  <div class="master" :style="type == 'plus' ? 'background-color: #f6f6f6;' : 'background-color: #d3dce6;'">
    <div v-if="type == 'plus'" class="plus" @click="clickPlus">
      <i class="el-icon-plus"></i>
    </div>
    <div class="content" v-else>
      <div class="icon">
        <i :class="type" style="font-size: 150px"></i>
      </div>
    </div>
    <el-dialog :visible.sync="plus" width="40%" title="Scenario">
      <add-scenario-card />
    </el-dialog>
  </div>
</template>

<script>
import addScenarioCard from './components/AddScenarioCard.vue';
export default {
  components: {
    addScenarioCard
  },
  data() {
    return {
      plus: false
    };
  },
  props: {
    type: {
      type: String
    }
  },

  methods: {
    clickPlus() {
      this.plus = true;
    }
  }
};
</script>

<style lang="scss" scoped>
.master {
  height: 300px;
  width: 250px;
  border-radius: 4px;
  background-color: #f6f6f6;
  margin-right: 8px;
  .plus {
    font-size: 50px;
    line-height: 250px;
    text-align: center;
    height: 250px;
    width: 200px;
    background-color: white;
    margin: auto;
    margin-top: 25px;
    border-radius: 4px;
  }
  .content {
    .icon {
      height: 300px;
      line-height: 300px;
      font-size: 150px;
      text-align: center;
    }
  }
}
</style>
