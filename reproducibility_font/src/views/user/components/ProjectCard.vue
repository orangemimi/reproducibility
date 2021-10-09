<template>
  <div>
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <div class="title" :title="projectInfo.name">{{ projectInfo.name }}</div>
          <div>
            <el-button type="primary" icon="el-icon-edit" circle @click.native="editProjectInfoDialogShow = true"></el-button>
            <el-button type="danger" icon="el-icon-delete" circle></el-button>
          </div>
        </div>
      </template>
      <div class="body" @click="clickProjectCard">
        <div class="text item" :title="projectInfo.description">
          <label>Description：</label>
          {{ projectInfo.description }}
        </div>
        <div class="text item" :title="projectInfo.createTime">
          <label>Create Time：</label>
          {{ projectInfo.createTime }}
        </div>
        <div class="text item" :title="projectInfo.privacy">
          <label>Privacy：</label>
          {{ projectInfo.privacy }}
        </div>
      </div>
    </el-card>

    <el-dialog title="Edit Project Info" :visible.sync="editProjectInfoDialogShow" width="40%" :close-on-click-modal="false">
      <edit-info-form :projectInfo="project" @editProjectInfoResponse="editProjectInfoResponse"></edit-info-form>
    </el-dialog>
  </div>
</template>

<script>
import editInfoForm from '../../project/components/EditProjectInfo.vue';
export default {
  props: {
    project: {
      type: Object,
    },
  },
  components: { editInfoForm },
  data() {
    return {
      editProjectInfoDialogShow: false,
      projectInfo: '',
    };
  },
  methods: {
    async editProjectInfoResponse(val) {
      if (val) {
        this.editProjectInfoDialogShow = false;
        this.projectInfo = val.data;
        this.$notify({
          title: 'Success',
          message: 'You have update the project successfully!',
          type: 'success',
        });
      }
    },
    async clickProjectCard() {
      await this.$store.dispatch('permission/getRole', {
        project: this.project,
        userId: this.$store.userId,
      });
      this.$router.push({ path: `/project/${this.projectInfo.id}/info` });
    },
  },
  created() {
    this.projectInfo = this.project;
  },
};
</script>

<style scoped lang="scss">
.el-card {
  width: 250px;
  .text {
    font-size: 16px;
  }

  .title:hover {
    cursor: pointer;
  }
  .title {
    width: 100px;
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
    font-size: 20px;
    font-weight: 500;
    color: #409EFF;
  }

  .item, label:hover {
    cursor: pointer;
  }
  .item {
    margin-bottom: 18px;
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
  }
  label {
    font-weight: bolder;
  }
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  .el-link {
    font-size: 20px;
  }
}
</style>