<!--
 * 医生排班页面
 *
 * @Author: ShanZhu
 * @Date: 2023-11-17
-->
<template>
    <div>
        <!--排班日期选择-->
        <el-card>
            <div>
                <ul class="dateUl">
                    <li v-for="monthDay in monthDays" :key="monthDay">
                      <el-button
                          key="date-button"

                          type="primary"
                          plain
                          style="margin: 6px"
                          @click="dateClick(monthDay)"
                      >
                        {{ monthDay }}
                      </el-button>
                    </li>
                </ul>
            </div>
        </el-card>

       <!--科室选择-->
        <el-card>
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
          <div>
            <!--排班信息-->
            <el-row>
              <el-col :span="8">
                <span style="color: #3472a6">排班日期：<span style="color: #e75c09">{{arrangeTime}}</span> 科室：<span style="color: #e75c09">{{section}}</span></span>
              </el-col>
            </el-row>

            <el-row>
              <!--搜索栏-->
              <el-row type="flex">
                <el-col :span="8">
                  <el-input v-model="query"
                            placeholder="请输入姓名查询"
                            class="doctorInput"
                  >
                    <el-button
                        slot="append"
                        round
                        icon="iconfont icon-search-button"
                        @click="requestDoctors"
                    ></el-button>
                  </el-input>
                </el-col>
              </el-row>
            </el-row>

            <!--表格-->
            <el-row>
              <el-col>
                <el-table size="small" :data="doctorData" border>
                  <el-table-column label="账号" prop="dId" v-model="doctorData.dId"/>
                  <el-table-column label="姓名" prop="dName" v-model="doctorData.dName"/>
                  <el-table-column label="性别" prop="dGender" v-model="doctorData.dGender"/>
                  <el-table-column label="职位" prop="dPost" v-model="doctorData.dPost"/>
                  <el-table-column label="部门"  prop="dSection" v-model="doctorData.dSection"/>
                  <el-table-column label="操作" prop="dSection">
                    <template slot-scope="scope">
                      <el-button
                          v-if="scope.row.arrangeId == null"
                          size="mini"
                          type="primary"

                          style="font-size: 14px;"
                          @click="arrangeClick(scope.row.dId)"
                      > <i class="iconfont icon-edit-button" style="font-size: 12px;"></i>排班</el-button
                      >
                      <el-button
                          v-if="scope.row.arrangeId != null"
                          size="mini"
                          type="danger"
                          style="font-size: 14px;"
                          @click="deleteArrange(scope.row.arrangeId)"
                      > <i class="iconfont icon-delete-button" style="font-size: 12px;"></i>取消</el-button
                      >
                    </template>
                  </el-table-column>
                </el-table>

                <!-- 分页 -->
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    background
                    layout="total, sizes, prev, pager, next, jumper"
                    :current-page="pageNumber"
                    :page-size="size"
                    :page-sizes="[1, 2, 4, 8, 16]"
                    :total="total"
                >
                </el-pagination>
              </el-col>
            </el-row>

          </div>
        </el-card>

    </div>
</template>
<script>
import request from "@/utils/request";
const ARRANGEDATE = "arrangeDate";
export default {
    name: "ArrangeIndex",
    data() {
        return {
            //日期数据
            monthDays: [],
            monthDay: "",
            activePath: "",
            arrangeTime:"",
            //科室数据
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
            //科室医生表格数据
            section: "",
            doctorData: [],
            total: 3,
            pageNumber: 1,
            size: 8,
            query: "",
        };
    },

    methods: {
        //日历点击
        dateClick(monthDay) {
            const nowDate = new Date();
            let year = nowDate.getFullYear();
            let dateTime = year + "-" + monthDay;
            this.arrangeTime = dateTime;
            sessionStorage.setItem(ARRANGEDATE, dateTime);
        },
        //获取当天及后7天的日期星期
        nowDay(num) {
            let nowDate = new Date();
            nowDate.setDate(nowDate.getDate() + num);
            let month = nowDate.getMonth() + 1;
            let date = nowDate.getDate();
            if (date < 10) {
                date = "0" + date;
            }
            if (month < 10) {
                month = "0" + month;
            }
            let time = month + "-" + date;
            this.monthDays.push(time);
        },
        //科室点击
        sectionClick(section){
          this.section = section;
          this.requestDoctors();
        },
        //排班点击
        arrangeClick(dId) {
          request
              .get("arrange/addArrange", {
                params: {
                  arId: dId + sessionStorage.getItem("arrangeDate"),
                  arTime: sessionStorage.getItem("arrangeDate"),
                  dId: dId,
                },
              })
              .then((res) => {
                if (res.data.status !== 200)
                  return this.$message.error("已排班");
                this.$message.success("排班成功！");
                this.requestDoctors();
              });
        },
        deleteArrange(arrangeId) {
          request
              .get("arrange/deleteArrange", {
                params: {
                  arId: arrangeId,
                },
              })
              .then((res) => {
                if (res.data.status !== 200)
                  return this.$message.error("排班信息不存在");
                this.$message.success("删除排班信息成功！");
                this.requestDoctors();
              });
        },
        //页面大小改变时触发
        handleSizeChange(size) {
          this.size = size;
          this.requestDoctors();
        },
        //页码改变时触发
        handleCurrentChange(num) {
          this.pageNumber = num;
          this.requestDoctors();
        },
        //根据部门请求医生信息
        requestDoctors() {
          request
              .get("doctor/findDoctorBySectionPage", {
                params: {
                  pageNumber: this.pageNumber,
                  size: this.size,
                  query: this.query,
                  dSection: this.section,
                  arrangeDate: sessionStorage.getItem("arrangeDate"),
                },
              })
              .then((res) => {
                if (res.data.status !== 200)
                  return this.$message.error("数据请求失败");
                this.doctorData = res.data.data.doctors;
                this.total = res.data.data.total;
              });
        },
    },
    created() {
        //获取当天的后7天
        for (let i = 0; i < 25; i++) {
            this.nowDay(i);
        }
    },
};
</script>
<style scoped lang="scss">
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
    //margin: 5px;
    padding: 1px;
}
ul li {
  display: inline;
};
.el-breadcrumb{
  margin:8px;
}
</style>
