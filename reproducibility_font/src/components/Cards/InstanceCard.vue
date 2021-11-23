<!--  -->
<template>
  <div class="main">
    <el-card :class="instanceItem.status == 1 ? 'successCard' : 'runningCard'" shadow="hover">
      <div class="task_name">
        {{ instanceItem.name }}
        <div v-if="!(taskItem.selectInstanceId.indexOf(instanceItem.id) > -1) && role == 'builder'" class="task_star" @click="changeStar">
          <i class="el-icon-star-off" />
        </div>
        <div v-else-if="role == 'builder'" class="task_star" @click="changeStar">
          <i class="el-icon-star-on" />
        </div>
        <div v-if="role == 'reproductioner-builder' && instanceItem.authority == 'public'" @click="updateIntegrateTaskInstanceById(instanceItem.authority)">
          <i class="iconfont icon-icon-test1"></i>
        </div>
        <div v-else-if="role == 'reproductioner-builder' && instanceItem.authority != 'public'" @click="updateIntegrateTaskInstanceById(instanceItem.authority)">
          <i class="iconfont icon-icon-test"></i>
        </div>
      </div>
      <div class="task_createTime">
        <i class="el-icon-time" />
        {{ instanceItem.createTime }}
      </div>

      <div class="task_view" @click="clickView" v-if="role == 'builder' || role == 'reproductioner-builder'">View</div>
      <div></div>
    </el-card>

    <el-drawer :title="instanceItem.name" :visible.sync="drawer" :direction="direction" destroy-on-close size="50%" v-if="role == 'builder' || role == 'reproductioner-builder'">
      <instance-graph :instanceId="instanceItem.id"/>
    </el-drawer>
  </div>
</template>

<script>
import { updateIntegrateTaskInstance, updatePerformanceById, checkTaskStatus, updateIntegrateTaskInstanceById } from '@/api/request';
import instanceGraph from './components/InstanceGraph.vue';

export default {
  props: {
    instanceItem: {
      type: Object,
    },
    taskItem: {
      type: Object,
    },
    role: {
      type: String
    }
  },
  components: { instanceGraph },


  data() {
    return {
      instance: this.instanceItem,
      currentTask: this.taskItem,
      projectId: this.$route.params.id,
      direction: 'rtl',
      drawer: false,
    };
  },


  methods: {

    async clickView() {
      
      if (this.instanceItem.status != 1) {
        let data = await checkTaskStatus(this.instanceItem.tid);
        this.$emit("instance", data)
      }
      this.drawer = true;
      
    },

    async changeStar() {
      let isStar = this.taskItem.selectInstanceId.indexOf(this.instanceItem.id) > -1;
      if (!isStar) {
        let form = {
          id: this.taskItem.id,
          instanceId: this.instanceItem.id,
          type: 'star'
        }
        await updateIntegrateTaskInstance(form);
        await this.updatePerformance();
        this.$emit('changeSelectInstanceId', {id: this.instanceItem.id, type: 'add'});
      }
      if (isStar) {
        let form = {
          id: this.taskItem.id,
          instanceId: this.instanceItem.id,
          type: 'unstar'
        }
        await updateIntegrateTaskInstance(form);
        this.$emit('changeSelectInstanceId', {id: this.instanceItem.id, type: 'remove'});
      }
    },
    async updatePerformance() {
      let content = { content: 'Simulation Scenario', degree: '100%', type: 'success', icon: 'el-icon-sunny' };
      await updatePerformanceById('scenario', this.projectId, content);
    },

    async updateIntegrateTaskInstanceById(authority) {
      let json = {authority: ''}
      if(authority == 'public') {
        json.authority = 'private'
      } else {
        json.authority = 'public'
      }
      let data = await updateIntegrateTaskInstanceById(this.instanceItem.id, json)
      this.$emit("authority", data)
    }
  },
  mounted() {
  }

};
</script>
<style lang="scss" scoped>
.main {
  // min-height: 100px;
  width: 100%;
  margin-bottom: 15px;
  text-align: center;
  /deep/ .el-card {
    width: 100%;
    // height: 100%;
    min-height: 98px;
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
  /deep/ .rtl {
    width: 50%;
  }
}

</style>
