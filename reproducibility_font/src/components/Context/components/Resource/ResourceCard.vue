<template>
  <div class="master" :style="type == 'plus' ? 'background-color: #f6f6f6;' : 'background-color: #d3dce6;'">
    <router-link target="_blank" :to="{ path: `/project/resource/${projectId}` }" v-if="type == 'plus'">
      <div class="plus">
        <i class="el-icon-plus"></i>
      </div>
    </router-link>
    <div class="content" v-else @click="go">
      <div class="name">
        {{ resourceItem.name }}
        <i class="el-icon-view" v-if="type == 'input'"></i>
      </div>
      <el-row>
        <el-col :span="24">
          Type:
          <el-tag :type="getTag">{{ type }}</el-tag>
        </el-col>
        <el-col :span="24">{{ 'Event Name: ' + resourceItem.eventName }}</el-col>
        <el-col :span="24" v-if="type == 'input'">Data Type: FILE</el-col>
        <el-col :span="24" v-if="type == 'output'">{{ 'Data Type: ' + resourceItem.type }}</el-col>
        <el-col :span="24" v-if="type == 'parameter'">{{ 'Data Type: ' + resourceItem.type }}</el-col>
        <!-- <el-col :span="12" v-if="type == 'parameter'">{{ 'Value: ' + resourceItem.value }}</el-col> -->
        <el-col :span="24">Description:</el-col>
        <el-col :span="24" style="margin-top: 5px">
          <el-input
            type="textarea"
            :rows="3"
            placeholder="Please enter"
            v-model="resourceItem.description"
            style="width: 85%"
            resize="none"
            :disabled="true"
          ></el-input>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  components: {},
  data() {
    return {
      // textarea: '',
      projectId: this.$route.params.id
    };
  },
  computed: {
    getTag() {
      if (this.type == 'input') {
        return '';
      } else if (this.type == 'parameter') {
        return 'info';
      } else {
        return 'success';
      }
    }
  },
  props: {
    type: {
      type: String
    },
    resourceItem: {
      type: Object
    }
  },
  methods: {
    // init() {
    //   let temp = JSON.parse(JSON.stringify(this.resourceItem));
    //   this.textarea = temp.description;
    // },
    go() {
      let temp = JSON.parse(JSON.stringify(this.resourceItem));
      temp.format = this.type;
      let obj = JSON.stringify(temp);
      let routeData = this.$router.resolve({
        path: `/project/resourcecollection/${this.projectId}`,
        query: { resourceItem: encodeURIComponent(obj) }
      });
      window.open(routeData.href, '_blank');
    }
  },
  mounted() {
    // this.init()
    // console.log(this.resourceItem)
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
    padding-top: 20px;
    padding-left: 20px;
    .name {
      font-family: -apple-system, BlinkMacSystemFont, Segoe UI, Helvetica, Arial, sans-serif, Apple Color Emoji, Segoe UI Emoji;
      font-size: 16px;
      font-weight: 700;
      width: 100%;
    }
    .el-row {
      font-size: 10px;
      font-family: -apple-system, BlinkMacSystemFont, Segoe UI, Helvetica, Arial, sans-serif, Apple Color Emoji, Segoe UI Emoji;
      .el-col {
        margin-top: 10px;
      }
    }
  }
}
</style>
