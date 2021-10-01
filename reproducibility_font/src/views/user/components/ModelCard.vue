<template>
  <div>
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <el-link type="primary">{{ model.name }}</el-link>
          <div>
            <el-button type="primary" icon="el-icon-edit" circle></el-button>
            <el-button type="danger" icon="el-icon-delete" circle></el-button>
          </div>
        </div>
      </template>
      <div class="body">
        <div class="text item">
          <label>Description：</label>
          {{ model.description }}
        </div>
        <div class="text item">
          <label>Create Time：</label>
          {{ model.createTime }}
        </div>
        <div class="text item">
          <label>Privacy：</label>
          {{ model.privacy }}
        </div>
        <div class="text item">
          <label>Type：</label>
          {{ model.type }}
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  props: {
    model: {
      type: Object,
    },
  },
};
</script>

<style scoped lang="scss">
.el-card {
  width: 250px;
  .text {
    font-size: 16px;
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