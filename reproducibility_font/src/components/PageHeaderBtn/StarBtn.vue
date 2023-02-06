<!--  -->
<template>
  <div class="btn">
    <div class="btn-left">
      <div @click="star" v-show="isStar == false">
        <i class="el-icon-star-off"></i>
        Star
      </div>
      <div @click="unStar" v-show="isStar">
        <i class="el-icon-star-on"></i>
        UnStar
      </div>
    </div>
    <div class="btn-right">
      <div>
        {{ starNumber }}
      </div>
    </div>
  </div>
</template>

<script>
import { starProject, isStarProject, getStarCount, unStarProject } from '@/api/request';
export default {
  components: {},

  watch: {},

  computed: {},

  data() {
    return {
      projectId: this.$route.params.id,
      isStar: false,
      starNumber: 0
    };
  },

  methods: {
    async init() {
      let data = await isStarProject(this.projectId);
      console.log(data);
      if (data == 1) {
        this.isStar = true;
      } else {
        this.isStar = false;
      }
      this.starNumber = await getStarCount(this.projectId);
    },

    async star() {
      let data = await starProject(this.projectId);
      console.log(data);
      this.isStar = true;
      this.starNumber = data.starCount;
    },

    async unStar() {
      let data = await unStarProject(this.projectId);
      console.log(data);
      this.isStar = false;
      this.starNumber = data.starCount;
    }
  },

  mounted() {
    console.log(this.$store.state.user.userId);
    this.init();
  }
};
</script>
<style lang="scss" scoped>
.btn {
  width: 120px;
  font-size: 12px;
  font-weight: 550;
  text-align: center;
  vertical-align: middle;

  box-shadow: $btnShadow;
  transition: 0.2s cubic-bezier(0.3, 0, 0.5, 1);

  .btn-left {
    width: 80px;
    float: left;
    border: 1px solid $btnBorder;
    border-radius: 4px 0px 0 4px;
    background-color: $contain2Background;
    transition-property: color, background-color, border-color;
  }
  .btn-left:hover {
    cursor: pointer;
    background-color: $btnHoverBg;
  }

  .btn-right {
    width: 40px;
    float: left;
    border: 1px solid $btnBorder;
    border-radius: 0px 4px 4px 0px;
    background-color: $contain2Background;
  }
  .btn-right:hover {
    cursor: pointer;
    color: $blueEmplasisFont;
  }
}
</style>
