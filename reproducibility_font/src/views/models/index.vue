<template>
  <div class="container">
    <div class="top">
      <div class="top-container">
        <div class="backgroud">
          <picture class="bg-img">
            <img src="@/assets/images/orange1.jpg" />
          </picture>
        </div>
        <el-row class="top-info">
          <el-row class="top-title">Models</el-row>
          <el-row class="top-desc">Various, useful Models.</el-row>
          <el-row class="top-desc">Collected by generous community of OpenGMS Team. 🎁</el-row>
          <el-row class="input-container">
            <el-input @keyup.enter.native="searchData" v-model="value" placeholder="" prefix-icon="el-icon-search"></el-input>
          </el-row>
          <el-row class="search-note">Trending searches: Geodynamics,Geostatics,Hydrology,Coastal Vulnerability,Urban Noise</el-row>
          <el-row>
            <el-button plain class="add-btn" @click="addModelDialogShow = true">Add your model service ➔</el-button>
          </el-row>
        </el-row>
      </div>
    </div>

    <div class="main">
      <div class="main-container">
        <el-row>
          <el-col :span="4" v-for="(item, index) in data" :key="index">
            <serviceCard :item="item" type="Model"></serviceCard>
          </el-col>
        </el-row>
      </div>
    </div>
    <div style="text-align: center;">
      <el-pagination layout="prev, pager, next" :total="total" background small :pager-count="5" :page-size="12" :current-page="1" @current-change="currentChange"></el-pagination>
    </div>

    <!-- add model -->
    <el-dialog title="Add model in Reproducibilty" :visible.sync="addModelDialogShow" width="40%" :close-on-click-modal="false">
      <create-model></create-model>
    </el-dialog>
  </div>
</template>

<script>
import { getAllModelItems, getModelList } from '@/api/request';
import serviceCard from '_com/common/ServiceCard';
import createModel from './create';
export default {
  data() {
    return {
      data: [],
      is_extending: false,
      value: '',
      pageFilter: {
        pageSize: 8,
        page: 0,
      },
      addModelDialogShow: false,
      total: 0,
      // currentPage: 1,
      modelList: []
    };
  },
  computed: {
    noMore() {
      return this.count >= 20;
    },
    disabled() {
      return this.loading || this.noMore;
    },
  },
  methods: {
    async getModelList() {
      let params = {
        page: 0,
        pageSize: 12,
        searchText: '',
      };
      let result = await getModelList(params);
      this.total = result.data.total
      this.data = result.data.list
    },
    init() {
      this.getModelList()
    },
    async currentChange(val) {
      let params = {
        page: val - 1,
        pageSize: 12,
        searchText: ''
      }
      this.data = (await getModelList(params)).data.list
    },
    async searchData() {
      // this.pageFilter.page = 0;
      // this.getData();
      let params = {
        page: 0,
        pageSize: 12,
        searchText: this.value
      }
      let result = await getModelList(params)
      this.total = result.data.total,
      this.data = result.data.list
    },

    async getData() {
      let { content } = await getAllModelItems(this.pageFilter.page, this.pageFilter.pageSize);
      // let { content } = await get(
      //   `/modelItems/${this.pageFilter.page}/${this.pageFilter.pageSize}`
      // );
      // console.log(content);
      if (content.length == 0) {
        this.is_extending = false;
        return;
      } else {
        this.data = content;
        this.is_extending = true;
      }
      console.log(this.data);
    },


    addModelItem() {},
  },
  mounted() {
    // this.getData();
    this.init()
    // window.addEventListener('scroll', this.scrollDown);
  },
  components: { serviceCard, createModel },
};
</script>

<style lang="scss">
.container {
  .top {
    
    // background-color: #000;
    color: #fff;
    //   margin-bottom: 48px;
    .top-container {
      position: relative;
      .bg-img {
        position: relative;
        img {
          position: relative;
          object-fit: cover;
          //obackground-size: cover;
          width: 100%;
          height: calc(100vh - 60px);
          filter: brightness(0.8);
        }
      }
    }

    .top-info {
      position: absolute;
      left: 50%;
      top: 45%;
      width: 45%;
      transform: translate3d(-50%, -50%, 0);
      font-size: 1.4rem;
      font-family: Arial, Georgia, Times, 'Times New Roman', serif;
      .el-row {
        margin: 0.5rem 0;
      }
      .top-title {
        font-size: 3.5rem;
      }
      .input-container {
        .el-input {
          .el-input__inner {
            height: 50px;
          }
        }
      }
      .el-button {
        background: rgba($color: #fff, $alpha: 0.5);
        color: #fff;
        font-weight: 700;
        font-size: 16px;
        height: 50px;
        margin-top: 10px;
        //   border: 1px solid rgba($color: #fff, $alpha: 0.5);
      }
      .el-button:hover {
        border: 1.5px solid $selectOrangeBorderColor;
        color: $selectOrangeBorderColor;
      }
    }
  }

  .main {
    position: relative;
  }
  .main-container {
    p {
      position: relative;
      display: flex;
      justify-content: center;
      flex-wrap: wrap;
      overflow: hidden;
    }
  }

  .list-enter-active,
  .list-leave-active {
    transition: all 1s;
  }
  .list-enter,
  .list-leave-to {
    opacity: 0;
    transform: translateY(30px);
  }
}
</style>
