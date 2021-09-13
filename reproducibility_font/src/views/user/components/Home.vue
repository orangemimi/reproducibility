<!--  -->
<template>
  <div class="main">
    <div class="main-top">
      <div>
        <div class="title"><h3 class="h3">My Space</h3></div>
        <div class="subtitle"><h5 class="h5">You can manage your model and data here</h5></div>
      </div>
    </div>

    <div class="main-centre">
      <el-row :gutter="20" style="margin-bottom: 20px;" justify="center">
        <el-col :span="8" :offset="4">
          <user-home-card>
            <template v-slot:tit>
              <img src="http://geomodeling.njnu.edu.cn/static/img/model/model.png" alt="" style="width: 24px" />
              Model
            </template>
            <template v-slot:describe>
              <p>You can manage model provided by yourself</p>
            </template>
            <template v-slot:man>
              <h4>Manage</h4>
              <h4 style="margin-top: 10px; margin-bottom: 10px">my</h4>
              <h4>model</h4>
            </template>
            <template v-slot:nav>
              <el-button icon="el-icon-right" type="primary" circle @click="butclick('UserResource')" :plain="true"></el-button>
            </template>
          </user-home-card>
        </el-col>
        <el-col :span="8">
          <user-home-card>
            <template v-slot:tit>
              <img src="http://geomodeling.njnu.edu.cn/static/img/model/calcModel.png" alt="" style="width: 24px" />
              dataservice
            </template>
            <template v-slot:describe>
              <p>You can manage model provided by yourself</p>
            </template>
            <template v-slot:man>
              <h4>Manage</h4>
              <h4 style="margin-top: 10px; margin-bottom: 10px">my</h4>
              <h4>model</h4>
            </template>
            <template v-slot:nav>
              <el-button icon="el-icon-right" type="primary" circle @click="butclick('UserModel')" :plain="true"></el-button>
            </template>
          </user-home-card>
        </el-col>
      </el-row>
      <el-row :gutter="20" justify="center">
        <el-col :span="8" :offset="4">
          <user-home-card>
            <template v-slot:tit>
              <img src="http://geomodeling.njnu.edu.cn/static/img/model/logical.png" alt="" style="width: 24px" />
              projects
            </template>
            <template v-slot:describe>
              <p>You can manage model provided by yourself</p>
            </template>
            <template v-slot:man>
              <h4>Manage</h4>
              <h4 style="margin-top: 10px; margin-bottom: 10px">my</h4>
              <h4>model</h4>
            </template>
            <template v-slot:nav>
              <el-button icon="el-icon-right" type="primary" circle @click="butclick('UserAccount')" :plain="true"></el-button>
            </template>
          </user-home-card>
        </el-col>
        <el-col :span="8">
          <user-home-card>
            <template v-slot:tit>
              <img src="http://geomodeling.njnu.edu.cn/static/img/model/conceptual.png" alt="" style="width: 24px" />
              dataservice
            </template>
            <template v-slot:describe>
              <p>You can manage model provided by yourself</p>
            </template>
            <template v-slot:man>
              <h4>Manage</h4>
              <h4 style="margin-top: 10px; margin-bottom: 10px">my</h4>
              <h4>model</h4>
            </template>
            <template v-slot:nav>
              <el-button icon="el-icon-right" type="primary" circle @click="butclick('UserMessage')" :plain="true"></el-button>
            </template>
          </user-home-card>
        </el-col>
      </el-row>
    </div>
    <div class="main-bottom"></div>
  </div>
</template>

<script>
import userHomeCard from '_com/Cards/UserHomeCard.vue';

export default {
  components: { userHomeCard },

  watch: {},

  computed: {},

  data() {
    return {};
  },

  methods: {
    butclick(address) {
      this.$router.push({
        name: address ,
      });
    },
  },

  mounted() {},
};
</script>
<style lang="scss" scoped>
.main-centre {
  padding-left: 20px;
  padding-right: 20px;
}
.main {
  width: 100%;
  height: 100%;
  .title,
  .subtitle {
    text-align: center;
  }
  .title {
    margin-top: 30px;
    margin-bottom: 20px;
  }
  .subtitle {
    margin-bottom: 30px;
  }
  .h3 {
    font-size: 24px;
  }
  .h5 {
    font-size: 14px;
  }

}
</style>
