<!--  -->
<template>
  <div class="main">
    <div class="check">
      <el-row>
        <el-col :span="20">
          <el-checkbox-group v-model="checkList" @change="handleCheckAllChange">
            <el-checkbox label="Public" :checked="true"></el-checkbox>
            <el-checkbox label="Private" :checked="true"></el-checkbox>
          </el-checkbox-group>
        </el-col>
        <el-col :span="4">
          <div class="btn">
            <el-button type="text">Create</el-button>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="main-container">
      <el-row class="infinite-list" style="overflow: auto">
        <el-col :span="4" v-for="(item, index) in models" :key="index">
          <div v-if="item.privacy == 'public'">
            <div v-show="publicType">
              <model-card :model="item" />
            </div>
          </div>
          <div v-else>
            <div v-show="privateType">
              <model-card :model="item" />
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import modelCard from './ModelCard.vue';
import { getModelItemsByProvider } from '@/api/request';
export default {
  components: { modelCard },

  computed: {},

  data() {
    return {
      models: {},
      checkList: [],
      publicType: true,
      privateType: true,
      pageFilter: {
        pageSize: 8,
        page: 0
      }
    };
  },

  methods: {
    async getModel() {
      let temp = await getModelItemsByProvider(this.pageFilter.page, this.pageFilter.pageSize);
      this.models = temp.content;
      console.log(this.models);
    },
    handleCheckAllChange(val) {
      if (val.length == 2) {
        this.publicType = true;
        this.privateType = true;
      } else if (val.length == 0) {
        this.publicType = false;
        this.privateType = false;
      } else {
        if (val[0] == 'Public') {
          this.publicType = true;
          this.privateType = false;
        } else {
          this.publicType = false;
          this.privateType = true;
        }
      }
    }
  },

  mounted() {
    this.getModel();
  }
};
</script>

<style lang="scss" scoped>
.main {
  width: 90%;
  margin: 30px auto;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  height: 90%;
  background: white;
  border-radius: 4px;
  .check {
    border-bottom: 1px solid #dadce0;
    height: 50px;
    .el-checkbox {
      margin-top: 15px;
      margin-left: 30px;
    }
    .btn {
      margin-top: 5px;
    }
  }
}
.main-container {
  padding: 20px 20px;
}
</style>
