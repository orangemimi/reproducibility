<!-- record -->
<template>
  <div>
    <el-col :span="24" v-if="recordList != undefined">
      <el-row>
        <el-timeline v-for="(record, index) in recordList" :key="index">
          <el-timeline-item
            v-if="record.eventType == 'contextDefinition'"
            icon="el-icon-edit"
            type="primary"
            size="large"
            :timestamp="record.createTime"
          >
            <div>{{ record.content }}</div>
          </el-timeline-item>
          <el-timeline-item
            v-if="record.eventType == 'resourceCollection'"
            icon="el-icon-folder"
            type="success"
            color="#0bbd87"
            :timestamp="record.createTime"
          >
            <div>{{ record.content }}</div>
          </el-timeline-item>
          <el-timeline-item
            v-if="record.eventType == 'dataProcessing'"
            icon="el-icon-coin"
            type="success"
            color="#0bbd87"
            :timestamp="record.createTime"
          >
            <div>{{ record.content }}</div>
          </el-timeline-item>
          <el-timeline-item
            v-if="record.eventType == 'simulationExecution'"
            icon="el-icon-coordinate"
            type="success"
            color="#0bbd87"
            :timestamp="record.createTime"
          >
            <div>{{ record.content }}</div>
          </el-timeline-item>
          <el-timeline-item
            v-if="record.eventType == 'resultAnalysis'"
            icon="el-icon-reading"
            type="success"
            color="#0bbd87"
            :timestamp="record.createTime"
          >
            <div>{{ record.content }}</div>
          </el-timeline-item>
        </el-timeline>
      </el-row>
      <el-row>
        <el-button type="warning" @click="creatDocument" size="small" style="float: right">
          Create Document
        </el-button>
      </el-row>
    </el-col>
    <div v-else>There is no record exists!</div>
  </div>
</template>

<script>
import { get } from '@/axios';
export default {
  props: {
    projectInfo: {
      type: Object
    }
  },

  watch: {
    projectInfo: {
      handler(val) {
        if (val != null) {
          this.projectId = val.id;
          this.getAllRecords();
        }
      }
    }
  },

  computed: {},

  data() {
    return { projectId: this.projectInfo.id, recordList: [] };
  },

  methods: {
    // async init() {
    //   await this.getAllRecords();
    // },
    async getAllRecords() {
      let data = await get(`/records/all/${this.projectId}`);
      this.recordList = data;
    },
    creatDocument() {}
  }

  //   mounted() {
  //     this.init();
  //   }
};
</script>
<style lang="scss" scoped></style>
