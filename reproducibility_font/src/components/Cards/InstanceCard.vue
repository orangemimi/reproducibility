<!--  -->
<template>
  <div class="main">
    <div class="delete">
      <el-popconfirm title="Are you sure you want to delete this record?" @confirm="delInstanceItem">
        <i class="el-icon-delete" slot="reference"></i>
      </el-popconfirm>
    </div>
    <el-card :class="instanceItem.status == 1 ? 'successCard' : 'runningCard'" shadow="hover">
      <div class="task_name">
        {{ instanceItem.name }}
        <div v-if="taskItem.selectInstanceId != instanceItem.id && role == 'builder'" class="task_star" @click="changeStar">
          <i class="el-icon-star-off" />
        </div>
        <div v-else-if="role == 'builder'" class="task_star" @click="changeStar">
          <i class="el-icon-star-on" />
        </div>
        <div v-if="role == 'reproductioner-builder' && instanceItem.authority == 'public'" @click="updateIntegrateTaskInstanceById(instanceItem.authority)">
          <i class="iconfont icon-icon-test1"></i>
        </div>
        <div
          v-else-if="role == 'reproductioner-builder' && instanceItem.authority != 'public'"
          @click="updateIntegrateTaskInstanceById(instanceItem.authority)"
        >
          <i class="iconfont icon-icon-test"></i>
        </div>
      </div>
      <div class="task_createTime">
        <i class="el-icon-time" />
        {{ getTime }}
      </div>

      <div class="task_view" @click="clickView" v-if="role == 'builder' || role == 'reproductioner-builder'">View</div>
      <div></div>
    </el-card>

    <el-drawer
      :title="instanceItem.name"
      :visible.sync="drawer"
      :direction="direction"
      destroy-on-close
      size="50%"
      v-if="role == 'builder' || role == 'reproductioner-builder'"
    >
      <instance-graph :instance="instanceItem" />
    </el-drawer>
  </div>
</template>

<script>
import { updateIntegrateTaskInstance, checkTaskStatus, updateIntegrateTaskInstanceById } from '@/api/request';
import instanceGraph from './components/InstanceGraph.vue';
import { dateFormat } from '@/utils/utils';
export default {
  props: {
    instanceItem: {
      type: Object,
    },
    taskItem: {
      type: Object,
    },
    role: {
      type: String,
    },
  },
  components: { instanceGraph },

  computed: {
    getTime() {
      return dateFormat(this.instanceItem.createTime);
    },
  },

  data() {
    return {
      instance: this.instanceItem,
      currentTask: this.taskItem,
      projectId: this.$route.params.id,
      direction: 'rtl',
      drawer: false,
      record: {},
      timer: '',
    };
  },

  watch: {
    instanceItem: {
      async handler(val, oldval) {
        if (oldval.status != 1 && oldval.id != val.id) {
          clearInterval(this.timer);
        }
        if (val.status != 1 && oldval.id != val.id) {
          await this.getOutputs(val.tid);
        }
      },
    },
  },

  methods: {
    async clickView() {
      this.drawer = true;
    },

    async changeStar() {
      let isStar = this.instanceItem.id == this.taskItem.selectInstanceId;
      if (!isStar) {
        let form = {
          id: this.taskItem.id,
          instanceId: this.instanceItem.id,
        };
        await updateIntegrateTaskInstance(form);

        this.$emit('changeSelectInstanceId', this.instanceItem.id);
      }
      if (isStar) {
        let form = {
          id: this.taskItem.id,
          instanceId: '',
          type: 'unstar',
        };
        await updateIntegrateTaskInstance(form);
        this.$emit('changeSelectInstanceId', '');
      }
    },


    async updateIntegrateTaskInstanceById(authority) {
      let json = { authority: '' };
      if (authority == 'public') {
        json.authority = 'private';
      } else {
        json.authority = 'public';
      }
      let data = await updateIntegrateTaskInstanceById(this.instanceItem.id, json);
      this.$emit('authority', data);
    },

    async getOutputs(tid) {
      //获得结果

      this.record = {};
      this.timer = setInterval(async () => {
        if (this.record.status == 1) {
          clearInterval(this.timer);
          return;
        } else {
          let data = await checkTaskStatus(tid);
          this.record = data;
          this.$emit('check', data);
        }
      }, 3000);
    },

    delInstanceItem() {
      this.$emit('delInstanceItem', this.instanceItem.id);
    },

    async init() {
      if (this.instanceItem.status != 1) {
        console.log(this.timer);
        await this.getOutputs(this.instanceItem.tid);
      }
    },
  },
  async mounted() {
    await this.init();
  },
  beforeDestroy() {
    if (this.timer != '') {
      clearInterval(this.timer);
    }
  },
};
</script>
<style lang="scss" scoped>
.main {
  // min-height: 100px;
  width: 100%;
  margin-bottom: 9px;
  text-align: center;
  position: relative;
  .delete {
    position: absolute;
    z-index: 1;
    margin-top: 5px;
    margin-left: calc(100% - 22px);
  }
  .delete:hover {
    cursor: pointer;
  }
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
