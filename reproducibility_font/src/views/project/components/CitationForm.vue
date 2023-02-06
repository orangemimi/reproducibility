<template>
  <div>
    <el-steps :space="200" :active="activeCount" finish-status="success" align-center simple>
      <el-step ref="step1" :title="title1"></el-step>
      <el-step ref="step2" :title="title2"></el-step>
      <el-step ref="step3" :title="title3"></el-step>
    </el-steps>
    <div class="select" v-if="selectFlag">
      <el-select v-model="classValue" placeholder="Please select your article type" size="small" style="margin: 10px 0; width: 300px">
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
      </el-select>
    </div>
    <div v-if="formFlag">
      <el-form label-position="top" label-width="80px" :model="formData" size="small">
        <el-form-item :label="classValue == 'Book' ? 'Book name' : 'Title'">
          <el-input v-model="formData.name"></el-input>
        </el-form-item>
        <el-form-item :label="getAuthorLabel(classValue)">
          <el-input v-model="formData.author"></el-input>
        </el-form-item>
        <el-form-item :label="getSourceLable(classValue)">
          <el-input v-model="formData.source"></el-input>
        </el-form-item>
        <el-form-item label="Date">
          <el-row>
            <el-col :span="8">
              <el-date-picker v-model="date.year" type="year" placeholder="Accurate to the year" @change="change(1)"></el-date-picker>
            </el-col>
            <el-col :span="8">
              <el-date-picker v-model="date.month" type="month" placeholder="Accurate to the month" @change="change(2)"></el-date-picker>
            </el-col>
            <el-col :span="8">
              <el-date-picker v-model="date.date" type="date" placeholder="Accurate to the date" @change="change(3)"></el-date-picker>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </div>
    <div v-if="promptFlag" class="prompt">
      <h4>请确保以上真实有效</h4>
    </div>
    <div class="btn">
      <el-button round size="small" style="width: 100px" class="previous" @click="previousClick">Previous</el-button>
      <el-button type="primary" round size="small" style="width: 100px" class="next" @click="nextClick" v-if="promptFlag == false">Next</el-button>
      <el-button type="success" round size="small" style="width: 100px" class="next" v-if="promptFlag" @click="submit">Submit</el-button>
    </div>
  </div>
</template>

<script>
import { updateCitation } from '@/api/request';
export default {
  data() {
    return {
      classValue: '',
      options: [
        { label: 'Thesis', value: 'Thesis' },
        { label: 'Dissertation', value: 'Dissertation' },
        { label: 'Meeting', value: 'Meeting' },
        { label: 'Newspaper', value: 'Newspaper' },
        { label: 'Almanac', value: 'Almanac' },
        { label: 'Book', value: 'Book' },
        { label: 'Patent', value: 'Patent' },
        { label: 'Standard', value: 'Standard' },
        { label: 'Achievement', value: 'Achievement' }
      ],
      title1: 'loading',
      title2: 'wating',
      title3: 'wating',
      activeCount: 0,
      selectFlag: true,
      formFlag: false,
      promptFlag: false,
      formData: {
        name: '',
        author: '',
        source: ''
      },
      date: {
        selectedId: -1,
        year: '',
        month: '',
        date: ''
      }
    };
  },

  props: {
    citationData: {
      type: Object
    }
  },

  computed: {
    getAuthorLabel() {
      return function(type) {
        if (type == 'Almanac') {
          return 'Almanac name';
        } else if (type == 'Standard') {
          return 'Standard number';
        } else if (type == 'Achievement') {
          return 'Finisher';
        } else {
          return 'Author';
        }
      };
    },
    getSourceLable() {
      return function(type) {
        if (type == 'Thesis') {
          return 'Periodical';
        } else if (type == 'Dissertation') {
          return 'Degree-granting unit';
        } else if (type == 'Meeting') {
          return 'Meeting name';
        } else if (type == 'Newspaper') {
          return 'Newspaper name';
        } else if (type == 'Almanac') {
          return 'Column';
        } else if (type == 'Book') {
          return 'Publishing house';
        } else if (type == 'Patent') {
          return 'applicant';
        } else if (type == 'Standard') {
          return 'Source';
        } else if (type == 'Achievement') {
          return 'First completion unit';
        }
      };
    }
  },

  methods: {
    init() {
      if (this.citationData != undefined && this.citationData != null) {
        this.classValue = this.citationData.type;
        this.formData.name = this.citationData.name;
        this.formData.author = this.citationData.author;
        this.formData.source = this.citationData.source;
        console.log(this.citationData);
        if (this.citationData.dateType == 'year') {
          this.date.selectedId = 1;
          this.date.year = this.citationData.date;
        } else if (this.citationData.dateType == 'month') {
          this.date.selectedId = 2;
          this.date.month = this.citationData.date;
        } else if (this.citationData.dateType == 'date') {
          this.date.selectedId = 3;
          this.date.date = this.citationData.date;
        }
      }
      console.log(this.date);
    },
    nextClick() {
      if (this.activeCount == 0) {
        if (this.classValue != '') {
          this.selectFlag = false;
          this.activeCount = 1;
          this.title1 = 'finish';
          this.title2 = 'loading';
          this.formFlag = true;
        }
      } else if (this.activeCount == 1) {
        this.formFlag = false;
        this.activeCount = 2;
        this.title2 = 'finish';
        this.title3 = 'loading';
        this.promptFlag = true;
      }
    },
    previousClick() {
      if (this.activeCount == 1) {
        this.formFlag = false;
        this.activeCount = 0;
        this.title1 = 'loading';
        this.title2 = 'wating';
        this.selectFlag = true;
      } else if (this.activeCount == 2) {
        this.promptFlag = false;
        this.activeCount = 1;
        this.title2 = 'loading';
        this.title3 = 'wating';
        this.formFlag = true;
      }
    },

    async submit() {
      let form = {
        name: this.formData.name,
        author: this.formData.author,
        source: this.formData.source,
        type: this.classValue
      };
      if (this.date.selectedId == 1) {
        form['date'] = this.date.year;
        form['dateType'] = 'year';
      } else if (this.date.selectedId == 2) {
        form['date'] = this.date.month;
        form['dateType'] = 'month';
      } else if (this.date.selectedId == 3) {
        form['date'] = this.date.date;
        form['dateType'] = 'date';
      }
      await updateCitation(this.$route.params.id, form);
      this.$emit('citation', form);
    },

    change(flag) {
      if (flag == 1) {
        this.date.month = '';
        this.date.date = '';
        this.date.selectedId = 1;
      } else if (flag == 2) {
        this.date.date = '';
        this.date.year = '';
        this.date.selectedId = 2;
      } else if (flag == 3) {
        this.date.month = '';
        this.date.year = '';
        this.date.selectedId = 3;
      }
    }
  },
  mounted() {
    this.init();
  }
};
</script>

<style lang="scss" scoped>
.select {
  margin-top: 10px;
}
.prompt {
  text-align: center;
  margin: 20px 0;
}
.btn {
  .next {
    float: right;
  }
}
</style>
