<!--  -->
<template>
  <div class="main">
    <el-card :class="instance.status == 1 ? 'successCard' : 'runningCard'" shadow="hover">
      <div class="task_name">
        {{ instance.name }}
        <div v-if="!isStar" class="task_star" @click="changeStar">
          <i class="el-icon-star-off" />
        </div>
        <div v-else class="task_star" @click="changeStar">
          <i class="el-icon-star-on" />
        </div>
      </div>
      <div class="task_createTime">
        <i class="el-icon-time" />
        {{ instance.createTime }}
      </div>

      <div class="task_view" @click="showInstanceStatus">
        View
      </div>
      <div></div>
    </el-card>
  </div>
</template>

<script>
import { updateIntegrateTaskInstance, updatePerformanceById } from '@/api/request';
export default {
  props: {
    instanceItem: {
      type: Object
    },
    taskItem: {
      type: Object
    }
  },

  computed: {},

  data() {
    return {
      instance: this.instanceItem,
      isStar: false,
      currentTask: this.taskItem,
      projectId: this.$route.params.id
    };
  },

  methods: {
    init() {
      this.isStar = false;
      if (this.instance.id == this.currentTask.selectInstanceId) {
        this.isStar = true;
      }
    },
    showInstanceStatus() {
      this.$emit('showInstanceStatus', this.instance);
    },

    async changeStar() {
      //[0]instance id [1]isStar
      let isStar = this.isStar;
      if (!isStar) {
        await updateIntegrateTaskInstance(this.currentTask.id, this.instance.id);
        await this.updatePerformance();
      }
      if (isStar) {
        console.log('1111');
        await updateIntegrateTaskInstance(this.currentTask.id, null);
      }
      this.isStar = !this.isStar;
    },
    async updatePerformance() {
      let content = { content: 'Simulation Scenario', degree: '100%', type: 'success', icon: 'el-icon-sunny' };

      await updatePerformanceById('scenario', this.projectId, content);
    }
  },

  mounted() {
    this.init();
  }
};
</script>
<style lang="scss" scoped>
.main {
  height: 80px;
  width: 100%;
  margin-bottom: 15px;
  text-align: center;
  // padding-right: 50px;
  /deep/ .el-card {
    // padding: 5px;
    width: 100%;
    height: 100%;
  }
  /deep/ .el-card__body {
    padding: 10px 5px;
  }
  .successCard {
    background-color: #f0f9eb;
    border: 1px solid #67c23a;
    border-top: 5px solid #67c23a;
  }
  .runningCard {
    background-color: #fdf6ec;
    border: 1px solid #e6a23c;
    border-top: 5px solid #e6a23c;
  }

  .el-card:hover {
    // background-color: #3067d61c;
    box-shadow: 0px 0px 5px 1px rgba(27, 94, 238, 0.2);
    // cursor: pointer;
  }

  .task_name {
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 10px;
    .task_star {
      color: rgba($color: #ee624f, $alpha: 1);
    }
    .task_star:hover {
      cursor: pointer;
    }
  }
  .task_createTime {
    font-size: 12px;
    text-align: right;
    color: #5c5c5c;
    // font-weight: 600;
  }
  .task_view {
    font-size: 12px;
    text-align: right;
    color: #5c5c5c;
    // font-weight: 600;
  }
  .task_view:hover {
    cursor: pointer;
  }
}
</style>
