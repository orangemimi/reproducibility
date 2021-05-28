<!--a time extent -->
<template>
  <div class="main_extent">
    <el-card shadow="never" class="card">
      <!-- <el-date-picker v-model="timeExtents.year" type="daterange" range-separator="To" start-placeholder="Start year" end-placeholder="End year"></el-date-picker> -->
      <div class="year">
        <el-date-picker v-model="timeExtents.year.from" type="year" placeholder="Start year"></el-date-picker>
        <div class="to">
          To
        </div>
        <el-date-picker v-model="timeExtents.year.to" type="year" placeholder="End year"></el-date-picker>
      </div>
      <div class="month">
        <el-select v-model="timeExtents.month.from" clearable placeholder="Start month" class="start">
          <el-option v-for="item in options" :key="item" :label="item" :value="item"></el-option>
        </el-select>
        <div class="to">
          To
        </div>
        <el-select v-model="timeExtents.month.to" clearable placeholder="End month" class="end">
          <el-option v-for="item in options" :key="item" :label="item" :value="item"></el-option>
        </el-select>
      </div>
      <div class="day">
        <el-select v-model="timeExtents.month.from" clearable placeholder="Start day" class="start">
          <el-option v-for="item in options" :key="item" :label="item" :value="item"></el-option>
        </el-select>
        <div class="to">
          To
        </div>
        <el-select v-model="timeExtents.month.to" clearable placeholder="End month" class="end">
          <el-option v-for="item in options" :key="item" :label="item" :value="item"></el-option>
        </el-select>
      </div>
      <el-time-picker is-range v-model="timeExtents.second" range-separator="To" start-placeholder="Start second" end-placeholder="End second" placeholder="选择时间范围"></el-time-picker>
    </el-card>
  </div>
</template>

<script>
export default {
  watch: {
    timeExtents: {
      handler(val) {
        console.log('dwd', val);
        this.$emit('getReturnTime', val, this.count);
        this.count++;
      },
      deep: true //true 深度监听
    }
  },

  computed: {},

  data() {
    return {
      timeExtents: {
        year: {
          from: '',
          to: ''
        },
        month: {
          from: '',
          to: ''
        },
        day: {
          from: '',
          to: ''
        },
        second: {
          from: '',
          to: ''
        }
      },
      count: 0,
      options: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
    };
  },

  methods: {},

  mounted() {}
};
</script>
<style lang="scss" scoped>
.main_extent {
  margin: 0;

  /deep/.el-date-editor .el-range-separator {
    width: 10%;
  }
  /deep/.el-date-editor--daterange.el-input__inner {
    width: 100%;
  }
  /deep/.el-date-editor--timerange.el-input__inner {
    width: 100%;
  }
  .card {
    .yeat {
      .start {
        width: 45%;
        float: left;
      }
      .to {
        width: 10%;
        float: left;
        text-align: center;
      }
      .end {
        width: 45%;
      }
    }
    .month {
      .start {
        width: 45%;
        float: left;
      }
      .to {
        width: 10%;
        float: left;
        text-align: center;
      }
      .end {
        width: 45%;
      }
    }
    .day {
      .start {
        width: 45%;
        float: left;
      }
      .to {
        width: 10%;
        float: left;
        text-align: center;
      }
      .end {
        width: 45%;
      }
    }
  }
}
</style>
