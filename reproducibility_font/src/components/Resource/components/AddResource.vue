<template>
  <div>
    <el-form ref="form" :model="form" label-width="90px" size="mini">
      <el-form-item label="Name">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="Description">
        <el-input type="textarea" v-model="form.desc" resize="none"></el-input>
      </el-form-item>
      <el-form-item label="Type">
        <el-select v-model="form.type" placeholder="Please select a type" @change="change">
          <el-option label="input" value="input"></el-option>
          <el-option label="parameter" value="parameter"></el-option>
          <el-option label="share_file" value="share_file"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="File">
        <div style="width: 100%">
          <el-radio v-model="form.radio" label="1" style="width: 60px" :disabled="form.type == '' || form.type != 'input'">User</el-radio>
          <el-input
            v-model="form.inputUser"
            class="input-with-select"
            style="margin-top: 5px; margin-bottom: 5px; width: calc(100% - 100px)"
            :disabled="form.type == '' || form.type != 'input' || form.radio == '2'"
            :readonly="true"
          >
            <el-button
              slot="append"
              icon="el-icon-more"
              :disabled="form.type == '' || form.type != 'input' || form.radio == '2'"
              @click="userFile = true"
            ></el-button>
          </el-input>
        </div>
        <div style="width: 100%">
          <el-radio v-model="form.radio" label="2" style="width: 60px" :disabled="form.type == '' || form.type != 'input'">Local</el-radio>
          <input type="file" ref="file" hidden @change="fileChange" />
          <el-input
            v-model="form.inputLocal"
            class="input-with-select"
            style="margin-top: 5px; margin-bottom: 5px; width: calc(100% - 100px)"
            :disabled="form.type == '' || form.type != 'input' || form.radio == '1'"
            :readonly="true"
          >
            <el-button
              slot="append"
              icon="el-icon-more"
              :disabled="form.type == '' || form.type != 'input' || form.radio == '1'"
              @click="btnChange"
            ></el-button>
          </el-input>
        </div>
      </el-form-item>
      <el-form-item label="Data Type">
        <el-radio :disabled="form.type == '' || form.type == 'parameter'" v-model="form.dataType" label="FILE"></el-radio>
        <el-radio :disabled="form.type == '' || form.type != 'parameter'" v-model="form.dataType" label="STRING"></el-radio>
        <el-radio :disabled="form.type == '' || form.type != 'parameter'" v-model="form.dataType" label="INT"></el-radio>
      </el-form-item>
      <el-form-item label="Value">
        <el-input :disabled="form.type == '' || form.type != 'parameter'" v-model="form.value"></el-input>
      </el-form-item>

      <el-form-item label="Share_Id">
        <el-input :disabled="form.type == '' || form.type != 'share_file'" v-model="form.shareId"></el-input>
      </el-form-item>
    </el-form>
    <div style="text-align: center">
      <el-button type="primary" @click="update" size="mini" v-if="parameter != undefined && parameter != null">Update</el-button>
      <el-button type="primary" @click="onSubmit" size="mini" v-else>Create</el-button>
    </div>

    <el-dialog :visible.sync="userFile" width="40%" :close-on-click-modal="false" v-if="userFile" append-to-body>
      <file-select @selectFile="selectFile" />
    </el-dialog>
  </div>
</template>

<script>
import fileSelect from './FileSelect.vue';
import {
  saveDataItemOfUploaded,
  saveDataItem,
  saveDataItemOfNoUpload,
  updateDataItemById,
  updateDataItemOfUploaded,
  updateDataItemOfNoUpload
} from '@/api/request';
export default {
  data() {
    return {
      form: {
        name: '',
        type: '',
        dataType: '',
        desc: '',
        value: '',
        radio: '',
        inputUser: '',
        inputLocal: '',
        shareId: ''
      },
      userFile: false,
      resource: '',
      localFile: ''
    };
  },

  props: {
    parameter: {
      type: Object
    }
  },

  components: {
    fileSelect
  },
  methods: {
    async onSubmit() {
      if (this.form.name == '') {
        this.message('warning', 'Name cannot be empty!');
      } else if (this.form.type == '') {
        this.message('warning', 'Please select a type!');
      }
      if (this.form.type == 'input') {
        if ((this.form.radio == '1' && this.form.inputUser == '') || (this.form.radio == '2' && this.form.inputLocal == '')) {
          this.message('warning', 'Please select the file to upload!');
        } else if (this.form.radio == '1' && this.form.inputUser != '') {
          let data = await this.saveDataItemOfUploaded();
          this.$emit('dataItem', data);
        } else if (this.form.radio == '2' && this.form.inputLocal != '') {
          let data = await this.saveDataItemOfNoUpload();
          this.$emit('dataItem', data);
        }
      } else if (this.form.type == 'parameter') {
        if (this.form.value == '') {
          this.message('warning', 'Value cannot be empty!');
        } else {
          let data = await this.saveFileOfParameter();
          this.$emit('dataItem', data);
        }
      } else if (this.form.type == 'share_file') {
        if (this.form.shareId == '') {
          this.message('warning', 'Share_Id cannot be empty!');
        } else {
          let data = await this.saveFileOfShare();
          this.$emit('dataItem', data);
        }
      }
    },

    async update() {
      if (this.form.name == '') {
        this.message('warning', 'Name cannot be empty!');
      } else if (this.form.type == '') {
        this.message('warning', 'Please select a type!');
      }
      if (this.form.type == 'input') {
        if ((this.form.radio == '1' && this.form.inputUser == '') || (this.form.radio == '2' && this.form.inputLocal == '')) {
          this.message('warning', 'Please select the file to upload!');
        } else if (this.form.radio == '1' && this.form.inputUser != '') {
          await this.updateDataItemById('uploaded');
        } else if (this.form.radio == '2' && this.form.inputLocal != '') {
          await this.updateDataItemById('noUpload');
        }
      } else if (this.form.type == 'parameter') {
        if (this.form.value == '') {
          this.message('warning', 'Value cannot be empty!');
        } else {
          await this.updateDataItemById('parameter');
        }
      } else if (this.form.type == 'share_file') {
        if (this.form.shareId == '') {
          this.message('warning', 'Share_Id cannot be empty!');
        } else {
          await this.updateDataItemById('share_file');
        }
      }
      this.form.id = this.parameter.id;
      this.$emit('update', this.form);
    },

    async updateDataItemById(type) {
      if (type == 'parameter' || type == 'share_file') {
        let form = {
          name: this.form.name,
          description: this.form.desc,
          format: this.form.type,
          type: this.form.dataType,
          value: this.form.value
        };
        await updateDataItemById(this.parameter.id, form);
      } else if (type == 'uploaded') {
        let form = {
          resource: this.resource,
          dataId: this.parameter.id,
          dataItem: {
            name: this.form.name,
            description: this.form.desc,
            format: this.form.type,
            type: this.form.dataType
          }
        };
        await updateDataItemOfUploaded(form);
      } else if (type == 'noUpload') {
        let form = new FormData();
        form.append('name', this.form.name);
        form.append('format', this.form.type);
        form.append('description', this.form.desc);
        form.append('type', this.form.dataType);
        form.append('dataId', this.parameter.id);
        form.append('multipartFile', this.localFile);
        await updateDataItemOfNoUpload(form);
      }
    },

    message(type, text) {
      this.$notify({
        title: type,
        message: text,
        type: type
      });
    },

    //将用户文件夹（已上传）的文件上传
    async saveDataItemOfUploaded() {
      let form = {
        resource: this.resource,
        dataItem: {
          name: this.form.name,
          description: this.form.desc,
          format: this.form.type,
          type: this.form.dataType,
          projectId: this.$route.params.id
        }
      };
      let data = await saveDataItemOfUploaded(form);
      form.dataItem.id = data;
      console.log(data);
      return form.dataItem;
    },
    //保存未上传过的文件
    async saveDataItemOfNoUpload() {
      let form = new FormData();
      form.append('name', this.form.name);
      form.append('description', this.form.desc);
      form.append('format', this.form.type);
      form.append('type', this.form.dataType);
      form.append('projectId', this.$route.params.id);
      form.append('localFile', this.localFile);
      let data = await saveDataItemOfNoUpload(form);
      return {
        name: this.form.name,
        description: this.form.desc,
        format: this.form.type,
        type: this.form.dataType,
        id: data
      };
    },

    //保存parameter文件
    async saveFileOfParameter() {
      let form = {
        name: this.form.name,
        description: this.form.desc,
        format: this.form.type,
        type: this.form.dataType,
        projectId: this.$route.params.id,
        value: this.form.value
      };
      return await saveDataItem(form);
    },
    //保存share文件
    async saveFileOfShare() {
      let form = {
        name: this.form.name,
        description: this.form.desc,
        format: this.form.type,
        type: this.form.dataType,
        projectId: this.$route.params.id,
        value: this.form.shareId
      };
      return await saveDataItem(form);
    },

    change(val) {
      if (val == 'input') {
        this.form.radio = '1';
        this.form.dataType = 'FILE';
        this.form.value = '';
        this.form.shareId = '';
      } else if (val == 'parameter') {
        this.form.radio = '';
        this.form.dataType = 'STRING';
      } else if (val == 'share_file') {
        this.form.radio = '';
        this.form.dataType = 'FILE';
      }
    },
    selectFile(val) {
      let str = 'user >';
      for (let i = 1; i < val.stack.length; i++) {
        str = str + ' ' + val.stack[i].name + ' >';
      }
      str = str + ' ' + val.row.name;
      this.form.inputUser = str;
      this.userFile = false;
      this.resource = {
        id: val.row.id,
        parent: val.stack[val.stack.length - 1].id,
        storey: val.stack.length - 1
      };
    },
    fileChange() {
      try {
        const fu = this.$refs.file;
        if (fu == null) return;
        // this.form.imgSavePath = fu.files[0].path;
        this.localFile = fu.files[0];
        this.form.inputLocal = fu.files[0].name;
      } catch (error) {
        console.debug('choice file err:', error);
      }
    },
    btnChange() {
      this.$refs.file.click();
    },

    init() {
      if (this.parameter != undefined && this.parameter != null) {
        console.log(this.parameter);
        this.form.name = this.parameter.name;
        this.form.desc = this.parameter.description;
        if (this.parameter.format == 'input') {
          this.form.type = 'input';
          this.form.dataType = this.parameter.type;
          this.form.radio = '1';
        } else if (this.parameter.format == 'parameter') {
          this.form.type = 'parameter';
          this.form.radio = '';
          this.form.dataType = this.parameter.type;
          this.form.value = this.parameter.value;
        } else if (this.parameter.format == 'share_file') {
          this.form.type = 'share_file';
          this.form.radio = '';
          this.form.dataType = 'FILE';
        }
      }
    }
  },
  mounted() {
    this.init();
  }
};
</script>

<style lang="scss" scoped></style>
