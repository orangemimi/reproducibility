<template>
  <div class="container">
    <div class="top">
      <div class="top-container">
        <div class="backgroud">
          <picture class="bg-img">
            <img src="@/assets/images/orange4.jpg" />
          </picture>
        </div>
        <el-row class="top-info">
          <el-row class="top-title">Official Repository</el-row>
          <el-row class="top-desc">Various, useful DataServices.</el-row>
          <el-row class="top-desc">Collected by generous community of OpenGMS Team. 🎁</el-row>
          <el-row class="input-container">
            <el-input @keyup.enter.native="searchData" v-model="value" placeholder="" prefix-icon="el-icon-search"></el-input>
          </el-row>
          <el-row class="search-note">Trending searches: Geodynamics,Geostatics,Hydrology,Coastal Vulnerability,Urban Noise</el-row>
          <el-row>
            <!-- <el-button plain class="add-btn" @click="addModelDialogShow = true">Add your model service ➔</el-button> -->
            <div class="Categories">
              <div class="head">Method Categories</div>
              <div class="bottom">
                <div :class="select == 1 ? 'select bottom-content' : 'bottom-content'" @click="changeSelect(1)">Conversion</div>
                <div :class="select == 2 ? 'select bottom-content' : 'bottom-content'" @click="changeSelect(2)">Processing</div>
                <div :class="select == 3 ? 'select bottom-content' : 'bottom-content'" @click="changeSelect(3)">Visualization</div>
              </div>
            </div>
          </el-row>
        </el-row>
      </div>
    </div>

    <div class="main">
      <div class="main-container">
        <el-row>
          <el-col :span="4" v-for="(item, index) in data" :key="index">
            <data-service-card :item="item"></data-service-card>
          </el-col>
        </el-row>
      </div>
    </div>
    <div class="page">
      <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        small
        @current-change="currentChange($event, method)"
        :pager-count="5"
      ></el-pagination>
    </div>

    <!-- add model -->
    <el-dialog title="Add model in Reproducibilty" :visible.sync="addModelDialogShow" width="40%" :close-on-click-modal="false">
      <create-model></create-model>
    </el-dialog>
  </div>
</template>

<script>
import { getDataServiceByPortal } from '@/api/request';
import dataServiceCard from './components/DataServiceCard.vue';
import createModel from './create';
export default {
  data() {
    return {
      data: [],
      total: 0,
      value: '',
      method: 'conversion',
      select: 1,
      addModelDialogShow: false
    };
  },

  methods: {
    searchData() {
      this.getData(this.method, this.value);
    },

    async changeSelect(val) {
      if (val == 1 && this.select != 1) {
        await this.getData('conversion', '');
        this.method = 'conversion';
      } else if (val == 2 && this.select != 2) {
        await this.getData('processing', '');
        this.method = 'processing';
      } else if (val == 3 && this.select != 3) {
        await this.getData('visualization', '');
        this.method = 'visualization';
      }
      this.select = val;
    },

    async getData(method, searchText) {
      let jsonData = {
        method: method,
        page: 1,
        pageSize: 12,
        searchText: searchText
      };
      let data = await getDataServiceByPortal(jsonData);

      this.data = data.data.list;
      this.total = data.data.total;
    },

    async currentChange(val, method) {
      let jsonData = {
        method: method,
        page: val,
        pageSize: 12,
        searchText: ''
      };
      let data = await getDataServiceByPortal(jsonData);
      this.data = data.data.list;
      this.total = data.data.total;
    }
  },
  mounted() {
    this.getData(this.method, '');
  },
  components: { dataServiceCard, createModel }
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
      //   .el-button {
      //     background: rgba($color: #fff, $alpha: 0.5);
      //     color: #fff;
      //     font-weight: 700;
      //     font-size: 16px;
      //     height: 50px;
      //     margin-top: 10px;
      //     //   border: 1px solid rgba($color: #fff, $alpha: 0.5);
      //   }
      //   .el-button:hover {
      //     border: 1.5px solid $selectOrangeBorderColor;
      //     color: $selectOrangeBorderColor;
      //   }
      .Categories {
        width: 450px;
        height: 250px;
        background: rgba($color: #fff, $alpha: 0.5);
        border-radius: 8px;
        .head {
          height: 60px;
          background-color: #eaeaea;
          border-top-left-radius: 8px;
          border-top-right-radius: 8px;
          text-align: center;
          line-height: 60px;
          color: #76b9ed;
        }
        .bottom {
          padding: 20px;
          .bottom-content {
            height: 50px;
            line-height: 50px;
            padding-left: 10px;
          }
          .bottom-content:hover {
            cursor: pointer;
          }
          .select {
            background-color: rgba($color: #d9edf7, $alpha: 0.5);
            color: rosybrown;
          }
        }
      }
    }
  }

  .main {
    position: relative;
    .page {
      text-align: center;
      margin-top: 10px;
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
