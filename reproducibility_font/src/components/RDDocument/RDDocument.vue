<template>
  <div class="main">
    <el-timeline>
        <el-timeline-item v-for="(item, index) in processList" :key="index" center :timestamp="'步骤' + (index + 1)"
          placement="top">
          <div class="card">
          <process-card :stepInfo="item" :stepInfoId="index">
          </process-card>
            <div class="button" style="position: absolute;z-index: 99;top: 40px;right: 50px;">
              <el-button type="primary" icon="el-icon-edit"  @click="editClick(index)" ></el-button>
              <el-button type="danger" icon="el-icon-delete"  @click="deleteClick(index)"></el-button>
            </div>
          </div>
        </el-timeline-item>
        <el-timeline-item timestamp="添加步骤" placement="top">
          <div class="add" @click="addProcessClick">
              <i class="el-icon-plus"></i>
          </div>
        </el-timeline-item>
      </el-timeline>
    <el-dialog v-if="addFlag" :visible.sync="addFlag" width="600px" title="添加步骤">
      <add-process :processItem="processItem" :operateType="operateType" :processIndex="updateIndex"></add-process>
    </el-dialog>
  </div>
  
</template>

<script>
import ProcessCard from './components/ProcessCard.vue';
import AddProcess from './components/addProcess.vue';
import { deleteProcess } from '@/api/request';
    
export default {
  components: {
    ProcessCard,
    AddProcess,
  },
  props: {
      taskInfoInit: {
        type: Object
      }
    },
  data() {
    return {
      projectId: this.$route.params.id,
      forkingProjectId: this.$route.query.forkingProjectId,
      addFlag: false,
      updateFlag: false,
      processList:this.taskInfoInit.processes,
      processItem:{},
      operateType:"add",
      updateIndex:-1
    };
  },

  methods: {
    addProcessClick(){
        this.processItem = {
          name: "",
          stepType: "",
          operateType: "",
          description: "",
          references: [],
          other: "",
          pictures: [],
          modelResource:{
            // id:"",
            // format:"",
            // state:"",
            // activityAttribute:{},
            // agentAttribute:{}
          },
          modelInputs:[],
          parameters:[],
          modelOutputs:[],
        };
        this.operateType = "add"
        this.addFlag = true;
      },

      editClick(index){
        this.processItem = this.processList[index];
        this.operateType = "update";
        this.updateIndex = index;
        this.addFlag = true;
      },

      async deleteClick(index){
        await deleteProcess(this.projectId, {index:index})
        this.$notify({
            title: 'Success',
            message: 'Successfully deleted!',
            type: 'success'
          });
        this.processList.splice(index, 1)
      }
  },

};
</script>
<style lang="scss" scoped>
.main {
  .button {
    position: absolute;
    z-index: 99;
    top: 20px;
    right: 20px;
  }
  .card {
    position: relative;
  
    .button {
      position: absolute;
      z-index: 99;
      top: 20px;
      right: 20px;
    }
  }
  .example {
    margin-bottom: 20px;
  
    .example-text {
      margin-left: 50px;
      margin-top: 20px;
      margin-bottom: 20px;
    }
  }
  
  .add {
    height: 100px;
    width: 100px;
    border: 2px dashed #eaeaea;
  
    &:hover {
      border-color: lightblue;
      cursor: pointer;
    }
    .el-icon-plus{
      margin-top: 38px;
      margin-left: 38px;
      font-size: 20px;
    }
  }
  
}
</style>
