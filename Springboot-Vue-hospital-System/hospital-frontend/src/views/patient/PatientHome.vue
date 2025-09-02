<!--
 * 病患首页
 *
 * @Author: ShanZhu
 * @Date: 2023-11-17
-->
<template>
    <div>
      <el-row>
        <el-col :span="24">
          <div style="text-align: center;width: 100%;font-size: 32px;">
            <h1 style="margin-bottom: 20px"><span style="color: #000000;">欢 迎 登 录 医 院 管 理 系 统</span></h1>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <div style="text-align: center;width: 100%;">
            <img src="@/assets/1.png" style="width: 80%;">
          </div>
        </el-col>
      </el-row>
    </div>
</template>
<script>
import request from "@/utils/request.js";
export default {
    name: "PatientLayout",
    data() {
        return {
            orderPeople: 1,
            bedPeople: 1,
        };
    },
    methods: {
        requestPeople() {
            request
                .get("order/orderPeople")
                .then((res) => {
                    if (res.data.status !== 200)
                        return this.$message.error("数据请求失败");
                    this.orderPeople = res.data.data;
                })
                .catch((err) => {
                    console.error(err);
                });
        },
        requestBed() {
            request
                .get("bed/bedPeople")
                .then((res) => {
                    if (res.data.status !== 200)
                        return this.$message.error("数据请求失败");
                    this.bedPeople = res.data.data;
                })
                .catch((err) => {
                    console.error(err);
                });
        },
    },
    created() {
        this.requestPeople();
        this.requestBed();
    },
};
</script>
<style lang="scss" scoped>
.userFont {
    height: 150px;
    width: 250px;
    float: right;
    color: white;
    .spanCure {
        font-size: 15px;
        margin-top: 60px;
        margin-bottom: 15px;
    }
    .spanPeople {
        font-size: 18px;
    }
}

.userImage {
    height: 150px;
    width: 150px;
    font-size: 130px;
    color: white;
    position: relative;
    left: 40px;
    top: 10px;
    float: left;
}
.indexPeople {
    height: 200px;
    width: 440px;
    background: #58b9ae;
    float: left;
    margin: 30px;
}
</style>
