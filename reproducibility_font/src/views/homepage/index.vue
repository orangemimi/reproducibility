<template>
  <div>
    <el-row>
      <el-col :span="3" :offset="4">
        <div class="left">
          <div class="block">
            <input type="file" class="input" :width="size" :style="'font-size:' + size + 'px'" v-if="editFlag" accept="image/*" title="change the picture" @change="onchange"/>
            <el-avatar :size="size" class="avatar 1" :src="pictureURL" v-if="editFlag == false">user</el-avatar>
            <el-avatar :size="size" class="avatar 2" :src="editURL" v-if="editFlag">user</el-avatar>
          </div>
          <div v-if="editFlag == false">
            <p>{{ localuser.name }}</p>
            <div v-if="myUserId == userId">
              <el-button round @click="editProfile" class="edit-btn">Edit Profile</el-button>
            </div>
            <div>
              <el-row class="social">
                <el-col :span="8">
                  <i class="el-icon-user">0 followers</i>
                </el-col>
                <el-col :span="8">
                  <i class="el-icon-user-solid">0 following</i>
                </el-col>
                <el-col :span="8">
                  <i class="el-icon-star-off">0</i>
                </el-col>
              </el-row>
            </div>
            <div>
              <div class="info">
                <i class="iconfont icon-email"></i>
                {{ localuser.email }}
              </div>
              <div class="info" v-if="remoteuser.country != undefined && remoteuser.country != null && remoteuser.country != ''">
                <i class="iconfont icon-location"></i>
                {{ remoteuser.country }}
              </div>
              <div class="info" v-if="remoteuser.organizations != undefined && remoteuser.organizations != null && remoteuser.organizations.length > 0">
                <i class="iconfont icon-company"></i>
                {{ remoteuser.organizations[0] }}
              </div>
              <div class="info" v-if="remoteuser.title != undefined && remoteuser.title != null && remoteuser.title != ''">
                <i class="iconfont icon-doctorial"></i>
                {{ remoteuser.title }}
              </div>
              <div class="info" v-if="remoteuser.phone != undefined && remoteuser.phone != null && remoteuser.phone != ''">
                <i class="iconfont icon-phone"></i>
                {{ remoteuser.phone }}
              </div>
            </div>
          </div>
          <div v-else style="margin-top: 10px">
            <el-form ref="form" :model="form" label-width="20px" size="mini">
              <el-form-item>
                <template #label>
                  <i class="iconfont icon-nickname"></i>
                </template>
                <el-input v-model="form.name" placeholder="name"></el-input>
              </el-form-item>
              <el-form-item>
                <template #label>
                  <i class="iconfont icon-location"></i>
                </template>
                <el-input v-model="form.country" placeholder="country"></el-input>
              </el-form-item>
              <el-form-item>
                <template #label>
                  <i class="iconfont icon-company"></i>
                </template>
                <el-input v-model="form.organizations" placeholder="organizations"></el-input>
              </el-form-item>
              <el-form-item>
                <template #label>
                  <i class="iconfont icon-doctorial"></i>
                </template>
                <el-select v-model="form.title" placeholder="Please select!">
                  <el-option v-for="(item, index) in options" :key="index" :label="item.label" :value="item.value"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <template #label>
                  <i class="iconfont icon-phone"></i>
                </template>
                <el-input v-model="form.phone" placeholder="phone"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="updateUserByJwtUserId">Submit</el-button>
                <el-button @click="editFlag = false">Cancel</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </el-col>
      <el-col :span="13">
        <div class="right">
          <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="Overview" name="first"><over-view :publicProjects="publicProjects" /></el-tab-pane>
            <el-tab-pane name="second">
              <template #label>
                <span>
                  Projects
                  <el-avatar :size="20" v-if="$route.params.userId == $store.state.user.userId">{{ joinedProjects.length }}</el-avatar>
                  <el-avatar :size="20" v-else>{{ publicProjects.length }}</el-avatar>
                </span>
              </template>
              <projects :joinedProjects="joinedProjects" :publicProjects="publicProjects" />
            </el-tab-pane>
            <el-tab-pane name="third">
              <template #label>
                <span>
                  Models
                  <el-avatar :size="20">0</el-avatar>
                </span>
              </template>
              角色管理
            </el-tab-pane>
            <el-tab-pane name="fourth">
              <template #label>
                <span>
                  Dataservices
                  <el-avatar :size="20">0</el-avatar>
                </span>
              </template>
              定时任务补偿
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { getAllUserInfo, getPrivacy, updateUserByJwtUserId } from '@/api/request';
import overView from './components/overview.vue';
import projects from './components/projects.vue';

export default {
  data() {
    return {
      userId: this.$route.params.userId,
      myUserId: this.$store.getters.userId,
      joinedProjects: [],
      publicProjects: [],
      localuser: {},
      remoteuser: {},
      size: document.body.clientWidth / 8,
      activeName: 'first',
      editFlag: false,
      form: {
        name: '',
        country: '',
        organizations: '',
        title: '',
        phone: '',
        avatar: ''
      },
      options: [
        { label: 'Professor', value: 'Professor' },
        { label: 'Dr', value: 'Dr' },
        { label: 'Mr', value: 'Mr' },
        { label: 'Ms', value: 'Ms' },
        { label: 'Miss', value: 'Miss' },
        { label: 'Mrs', value: 'Mrs' },
        { label: 'Mx', value: 'Mx' },
        { label: 'Other', value: 'Unfilled' },
      ],
      editAvatarFlag: false,
      pictureURL: '',
      editURL: ''
    };
  },
  components: {
    overView,
    projects
  },
  methods: {
    async init() {
      let data = await getAllUserInfo(this.userId);
      this.localuser = data.localhost;
      this.remoteuser = data.remote.data;
      this.pictureURL = 'http://172.21.212.103:8088/userServer' + data.remote.data.avatar
      console.log(this.remoteuser);
      if (this.remoteuser.name != undefined && this.remoteuser.name != null && this.remoteuser.name != '') {
        this.form.name = this.remoteuser.name;
      }
      if (this.remoteuser.country != undefined && this.remoteuser.country != null && this.remoteuser.country != '') {
        this.form.country = this.remoteuser.country;
      }
      if (this.remoteuser.organizations != undefined && this.remoteuser.organizations != null && this.remoteuser.organizations.length > 0) {
        this.form.organizations = this.remoteuser.organizations[0];
      }
      if (this.remoteuser.title != undefined && this.remoteuser.title != null && this.remoteuser.title != '') {
        this.form.title = this.remoteuser.title;
      }
      if (this.remoteuser.phone != undefined && this.remoteuser.phone != null && this.remoteuser.phone != '') {
        this.form.phone = this.remoteuser.phone;
      }

      if (data.localhost.joinedProjects != null) {
        for (let i = 0; i < data.localhost.joinedProjects.length; i++) {
          this.joinedProjects.push(data.localhost.joinedProjects[i]);
        }
      }
      if (data.localhost.createdProjects != null) {
        for (let i = 0; i < data.localhost.createdProjects.length; i++) {
          this.joinedProjects.push(data.localhost.createdProjects[i]);
        }
      }
      for (let i = 0; i < this.joinedProjects.length; i++) {
        if ((await getPrivacy(this.joinedProjects[i])) == 'public') {
          this.publicProjects.push(this.joinedProjects[i]);
        }
      }
    },

    handleClick() {},

    async updateUserByJwtUserId() {
      this.form.avatar = this.editURL
      let data = await updateUserByJwtUserId(this.form);
      console.log(data);
      this.remoteuser.name = this.form.name;
      this.remoteuser.country = this.form.country;
      this.remoteuser.organizations[0] = this.form.organizations;
      this.remoteuser.title = this.form.title;
      this.remoteuser.phone = this.form.phone;
      this.pictureURL = this.editURL
      console.log(this.$store)
      this.$store.commit('user/setAvatar', data.data.avatar)
      this.$store.commit('user/setName', data.data.name)
      this.editFlag = false;
    },

    editProfile() {
      this.editFlag = true
      this.editURL = this.pictureURL
    },

    onchange(val) {
      let read = new FileReader()
      read.readAsDataURL(val.target.files[0])
      read.onload = e => {
        console.log(e.target.result)
        this.editURL = e.target.result
      }
    },

    getfile(val) {
      console.log(val);
    },
  },
  mounted() {
    this.init();
    // console.log(this.$route.params)
  },
};
</script>

<style scoped lang="scss">
.block {
  position: relative;
  overflow: hidden;
  margin-top: 55px;
  .avatar:hover {
    cursor: pointer;
  }
  .input {
    position: absolute;
    top: 0;
    opacity: 0;
  }
  
}
.edit-btn {
  margin-top: 10px;
  width: 100%;
}
.social {
  margin-top: 20px;
  font-size: 12px;
  margin-bottom: 10px;
}
p {
  margin-top: 10px;
  font-size: 25px;
}
.info {
  margin-bottom: 5px;
}

.right {
  margin-top: 55px;
  margin-left: 40px;
}
</style>