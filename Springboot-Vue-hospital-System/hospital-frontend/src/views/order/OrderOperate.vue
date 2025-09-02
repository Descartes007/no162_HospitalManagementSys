<!--
 * 病患挂号科室选择页面
 *
 * @Author: ShanZhu
 * @Date: 2023-11-17
-->
<template>
<div>
  <el-card>

    <span style="color: #2b5d88">请选择科室进行挂号</span>

    <el-divider></el-divider>

    <!--科室选择 -->
    <el-tabs type="border-card">
      <el-tab-pane label="内科">
        <ul>
          <li v-for="inter in inters" :key="inter">
            <el-button type="success" plain key="inter-button"  style="margin: 5px;" @click="sectionClick(inter)">{{ inter }}</el-button>
          </li>
        </ul>
      </el-tab-pane>
      <el-tab-pane label="外科">
        <ul>
          <li v-for="out in outs" :key="out">
            <el-button type="success" plain style="margin: 5px;" @click="sectionClick(out)">{{ out }}</el-button>
          </li>
        </ul>
      </el-tab-pane>
      <el-tab-pane label="妇产科">
        <ul>
          <li v-for="woman in women" :key="woman">
            <el-button type="success" plain style="margin: 5px;" @click="sectionClick(woman)">{{ woman }}</el-button>
          </li>
        </ul>
      </el-tab-pane>
      <el-tab-pane label="儿科">
        <ul>
          <li v-for="kid in kids" :key="kid">
            <el-button type="success" plain style="margin: 5px;" @click="sectionClick(kid)">{{ kid }}</el-button>
          </li>
        </ul>
      </el-tab-pane>
      <el-tab-pane label="五官科">
        <ul>
          <li v-for="five in fives" :key="five">
            <el-button type="success" plain style="margin: 5px;" @click="sectionClick(five)">{{ five }}</el-button>
          </li>
        </ul>
      </el-tab-pane>
      <el-tab-pane label="中医科">
        <ul>
          <li v-for="chinese in chinese" :key="chinese">
            <el-button type="success" plain style="margin: 5px;" @click="sectionClick(chinese)">{{ chinese }}</el-button>
          </li>
        </ul>
      </el-tab-pane>
      <el-tab-pane label="其他">
        <ul>
          <li v-for="others in others" :key="others">
            <el-button type="success" plain style="margin: 5px;" @click="sectionClick(others)">{{ others }}</el-button>
          </li>
        </ul>
      </el-tab-pane>
    </el-tabs>
  </el-card>

  <!--科室医生列表-->
  <el-card>
    <div class="head">
      <div>
        <i class="iconfont icon-doctor-list" style="margin: 5px; color: #2b5d88">医生列表</i>
        <span style="color: #2b5d88">（科室：<span style="color: #e75c09">{{section}}</span> 挂号时间：<span style="color: #e75c09">{{orderDate}}</span>）</span>
      </div>

      <el-divider></el-divider>

      <!-- 选择挂号时间 -->
      <div>
        <ul class="dateUl">
          <li v-for="monthDay in monthDays" :key="monthDay">
            <el-button
                size="mini"
                type="primary" plain
                @click="dateClick(monthDay)"
            >
              {{ monthDay }}</el-button>
          </li>
        </ul>
      </div>
    </div>

    <br/>
    <!-- 表格 -->
    <el-table :data="sectionData" size="small" stripe style="width: 100%" border>
      <el-table-column type="index" label="序号"width="60"/>
      <el-table-column prop="dId" label="工号" width="80"/>
      <el-table-column prop="dName" label="姓名" width="80"/>
      <el-table-column prop="dGender" label="性别" width="60"/>
      <el-table-column prop="dPost" label="职位" width="100"/>
      <el-table-column prop="dSection" label="科室" width="100"/>
      <el-table-column prop="dIntroduction" label="简介" />
      <el-table-column prop="dPrice" label="挂号费用/元" width="100"/>
      <el-table-column prop="dAvgStar" label="评价/5分" width="80"/>
      <el-table-column label="操作" width="140" v-if="clickTag">
        <template slot-scope="scope">
          <el-button
              class="iconfont icon-register-button"
              style="font-size: 14px"
              size="mini"
              type="primary"
              @click="openClick(scope.row.dId, scope.row.dName)"
          >
            挂号</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </el-card>
  <!-- 挂号对话框 -->
  <el-dialog title="填写挂号信息" :visible.sync="orderFormVisible">
    <el-form :model="orderForm" ref="orderForm" :rules="orderRules">
      <el-form-item
          label="挂号时间段"
          label-width="100px"
          prop="oTime"
      >
        <el-select
            v-model="orderForm.oTime"
            placeholder="请选择"
            no-data-text="请尝试预约明日"
        >
          <el-option
              v-for="time in times"
              :key="time"
              :label="time"
              :value="time"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="挂号日期" label-width="100px">
        <el-input
            v-model="orderForm.orderDate"
            autocomplete="off"
            disabled
        ></el-input>
      </el-form-item>
      <el-form-item label="医生工号" label-width="100px">
        <el-input
            v-model="orderForm.dId"
            autocomplete="off"
            disabled
        ></el-input>
      </el-form-item>
      <el-form-item label="医生姓名" label-width="100px">
        <el-input
            v-model="orderForm.dName"
            autocomplete="off"
            disabled
        ></el-input>
      </el-form-item>
      <el-form-item label="患者姓名" label-width="100px">
        <el-input
            v-model="orderForm.pName"
            autocomplete="off"
            disabled
        ></el-input>
      </el-form-item>
      <el-form-item label="患者身份证号" label-width="100px">
        <el-input
            v-model="orderForm.pCard"
            autocomplete="off"
            disabled
        ></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="orderFormVisible = false" style="font-size: 18px;">
        <i class="iconfont icon-cancel-button" style="font-size: 20px;"></i> 取 消
      </el-button>
      <el-button type="primary" @click="orderSuccess('orderForm')"style="font-size: 18px;">
        <i class="iconfont icon-sure-button" style="font-size: 20px;"></i> 确 定
      </el-button>
    </div>
  </el-dialog>


  </div>
</template>
<script>
import request from "@/utils/request";
import jwtDecode from "jwt-decode";
import {getToken} from "@/utils/storage";

export default {
  name: "orderOperate",
  data() {
    return {
      //科室数据
      section:"",
      inters: [
        "神经内科",
        "呼吸与危重症医学科",
        "内分泌科",
        "消化内科",
        "心血管内科",
        "肾内科",
        "发热门诊",
      ],
      outs: [
        "手足外科",
        "普通外科",
        "肛肠外科",
        "神经外科",
        "泌尿外科",
        "骨科",
        "烧伤整形外科",
      ],
      women: ["妇科", "产科"],
      kids: ["儿科", "儿童保健科"],
      fives: ["耳鼻咽喉科", "眼科", "口腔科"],
      chinese: ["中医科"],
      others: ["康复医学科", "急诊科", "皮肤性病科", "功能科"],
      //医生列表数据
      sectionOpt: this.$route.query.sectionOpt,
      sectionData: [],
      monthDays: [],
      clickTag: false,
      orderFormVisible: false,
      orderForm: { orderDate: "" },
      times: [],
      orderRules: {
        oTime: [
          { required: true, message: "选择时间段", trigger: "blur" },
        ],
      },
      //挂号日期
      orderDate: "",
      //拼接时间和日期成为oId
      idTime: "",
    };
  },
  methods: {
    //点击科室
    sectionClick(sectionOpt){
      //获取当天的后7天
      this.monthDays = [];
      for (var i = 0; i < 7; i++) {
        this.nowDay(i);
      }
      this.section = sectionOpt;
      //日期已经选了时候，请求当日值班科室医生
      if(this.orderDate != undefined){
        request
            .get("/arrange/findByTime", {
              params: {
                arTime: this.orderDate,
                dSection: this.section,
              },
            })
            .then((res) => {
              this.sectionData = res.data.data.map((item) => item.doctor);
            });
      }

      // 解码token
      this.orderForm.pName = this.tokenDecode(getToken()).pName;
      this.orderForm.pCard = this.tokenDecode(getToken()).pCard;
      this.orderForm.pId = this.tokenDecode(getToken()).pId;
    },
    //打开挂号对话框触发,获取挂号时间段已剩余票数
    requestTime(id) {
      this.idTime = id + this.orderDate;
      request
          .get("order/findOrderTime", {
            params: {
              arId: this.idTime,
            },
          })
          .then((res) => {
            const date = new Date(this.orderDate);
            const today = new Date();
            const isToday =
                date.getFullYear() === today.getFullYear() &&
                date.getMonth() === today.getMonth() &&
                date.getDate() === today.getDate();
            var array = [];
            if (!this.isTimeAfterTarget("09:30") || !isToday) {
              array.push(
                  "08:30-09:30  " + "   余号 " + "1"
              );
            }
            if (!this.isTimeAfterTarget("10:30") || !isToday) {
              array.push(
                  "09:30-10:30  " + "   余号 " + "1"
              );
            }
            if (!this.isTimeAfterTarget("11:30") || !isToday) {
              array.push(
                  "10:30-11:30  " + "   余号 " + "1"
              );
            }
            if (!this.isTimeAfterTarget("15:30") || !isToday) {
              array.push(
                  "14:30-15:30  " + "   余号 " + "1"
              );
            }
            if (!this.isTimeAfterTarget("16:30") || !isToday) {
              array.push(
                  "15:30-16:30  " + "   余号 " + "1"
              );
            }
            if (!this.isTimeAfterTarget("17:30") || !isToday) {
              array.push(
                  "16:30-17:30  " + "   余号 " + "1"
              );
            }
            this.times = array;
          });
    },
    isTimeAfterTarget(timeString) {
      // 判断时间是否超过timeString(入参格式如：09:30)
      const currentTime = new Date();

      // 解析传入的目标时间字符串，获取小时和分钟
      const [targetHour, targetMinute] = timeString.split(":");

      // 设置要比较的时间
      const targetTime = new Date();
      targetTime.setHours(targetHour);
      targetTime.setMinutes(targetMinute);
      targetTime.setSeconds(0);

      // 比较当前时间是否超过了目标时间
      return currentTime > targetTime;
    },
    //挂号点击确认
    orderSuccess(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          request
              .get("patient/addOrder", {
                params: {
                  pId: this.orderForm.pId,
                  dId: this.orderForm.dId,
                  oStart:
                      this.orderForm.orderDate +
                      " " +
                      this.orderForm.oTime,
                  arId: this.idTime,
                },
              })
              .then((res) => {
                if (res.data.status != 200)
                  return this.$message.error(
                      res.data.msg
                  );
                this.orderFormVisible = false;
                this.$message.success("挂号成功！");
                this.orderForm.oTime = '';
              });
        } else {
          return false;
        }
      });
    },
    //token解码
    tokenDecode(token) {
      if (token !== null) return jwtDecode(token);
    },
    //打开挂号对话框
    openClick(id, name) {
      this.orderForm.dId = id;
      this.orderForm.dName = name;
      this.orderFormVisible = true;
      //请求挂号时间段
      this.requestTime(id);
    },
    //选择日期触发时间
    dateClick(date) {
      //获取挂号年月日
      const nowDate = new Date();
      let year = nowDate.getFullYear();
      this.orderForm.orderDate = year + "-" + date;
      let dateYear = year + "-" + date;
      this.orderDate = dateYear;
      request
          .get("/arrange/findByTime", {
            params: {
              arTime: dateYear,
              dSection: this.section,
            },
          })
          .then((res) => {
            this.sectionData = res.data.data.map((item) => item.doctor);
            this.clickTag = true;
          });
    },
    //获取当天及后7天的日期星期
    nowDay(num) {
      var nowDate = new Date();
      var currentHour = nowDate.getHours();
      var currentMinute = nowDate.getMinutes();

      // 判断当前时间是否已经过了17:30
      if (
          currentHour > 17 ||
          (currentHour === 17 && currentMinute > 30)
      ) {
        num++; // 次日
      }

      nowDate.setDate(nowDate.getDate() + num);
      let month = nowDate.getMonth() + 1;
      let date = nowDate.getDate();
      if (date < 10) {
        date = "0" + date;
      }
      if (month < 10) {
        month = "0" + month;
      }
      var time = month + "-" + date;

      this.monthDays.push(time);
    },

  },
};
</script>

<style scoped lang="scss">
ul li {
  display: inline;
};
.el-breadcrumb{
  margin:8px;
}
.disabled {
  background-color: #ddd;
  border-color: #ddd;
  color: black;
  cursor: not-allowed; // 鼠标变化
  pointer-events: none;
}
.router-view {
  margin-top: 20px;
}
.sectionUl li {
  display: inline;
  padding: 60px;
}
.dateUl li {
  display: inline;
  margin: 5px;
  padding: 1px;
}
ul li {
  display: inline;
};
.el-breadcrumb{
  margin:8px;
}
</style>
