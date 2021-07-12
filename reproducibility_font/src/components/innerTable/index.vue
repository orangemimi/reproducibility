<template>
  <el-table :data="configuration(behavior)" border>
    <el-table-column prop="name" label="Name" width="180"> </el-table-column>
    <el-table-column prop="type" label="Type" width="180"> </el-table-column>
    <el-table-column prop="description" label="Description" width="180">
    </el-table-column>

    <el-table-column label="Data or Parameter">
      <template slot-scope="inner_scope">
        <el-button
          v-if="inner_scope.row.dataServiceId != null"
          @click="download(inner_scope.row.dataServiceId)"
          icon="el-icon-download"
          size="small"
          round
          >Downoload</el-button
        >
        <el-input
          :disabled="true"
          v-if="inner_scope.row.value != null"
          v-model="inner_scope.row.value"
        ></el-input>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
import config from "@/config";
export default {
  props: ["behavior"],
  methods: {
    download(dataServiceId) {
      window.open(`${config.containerURL}/data_service/fetch/${dataServiceId}`);
    },
    configuration({ inputs, parameters, outputs }) {
      let arr = [];

      inputs.forEach(({ name, description, dataServiceId }) => {
        if (dataServiceId != null) {
          arr.push({
            name,
            type: "input",
            description,
            dataServiceId
          });
        }
      });
      if (parameters != null) {
        parameters.forEach(({ name, description, value }) => {
          arr.push({
            name,
            type: "parameter",
            description,
            value
          });
        });
      }

      outputs.forEach(({ name, description, dataServiceId }) => {
        if (dataServiceId != null) {
          arr.push({
            name,
            type: "output",
            description,
            dataServiceId
          });
        }
      });
      return arr;
    }
  }
};
</script>

<style></style>
