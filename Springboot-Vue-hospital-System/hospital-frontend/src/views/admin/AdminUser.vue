<!--
 * 管理员主布局
 *
 * @Author: ShanZhu
 * @Date: 2023-11-17
-->
<template>
    <el-container>
      <!-- 头部 -->
      <el-header style="color: #eeeeee">
        <div class="font_words">
          <span @click="menuClick('adminLayout')">

            <div style="display: flex">

              <div style="">
                  <img src="../../assets/医院.png" style="width: 35px;position: relative; top: 2px;right: 6px"/>
              </div>
              <div style="flex: 1">
                 <span style="color: #ffffff;font-size: 26px">医院管理系统</span>
              </div>

            </div>




          </span>
        </div>

        <div class="font_words">


          <span>欢迎您，<b>{{ userName }}</b>&nbsp;管理员&nbsp;&nbsp;</span>
          <span><el-button type="danger"  size="mini" @click="logoutAdmin" >退出</el-button></span>
        </div>
      </el-header>

      <el-container>

      <!--侧边栏 -->
      <el-aside width="220px" >

        <!--导航菜单 -->
        <el-menu
          text-color="black"
          active-text-color="black"
          :default-active="activePath"
        >
          <el-menu-item index="adminLayout" @click="menuClick('adminLayout')">
            <i class="iconfont icon-homepage-menu" style="font-size: 18px;color: #2b5d88"> 首页</i>
          </el-menu-item>

          <el-submenu index="sub-menu1">
            <template slot="title">
              <span style="font-size: 18px;color: #2b5d88">用户管理</span>
            </template>

            <el-menu-item index="doctorList" @click="menuClick('doctorList')">
              <i class="iconfont icon-doctor-menu" style="font-size: 18px;"> 医生用户管理</i>
            </el-menu-item>

            <el-menu-item index="patientList" @click="menuClick('patientList')">
              <i class="iconfont icon-patient-menu" style="font-size: 18px;"> 患者用户管理</i>
            </el-menu-item>
          </el-submenu>

          <el-submenu index="sub-menu2">
            <template slot="title">
              <span style="font-size: 18px;color: #2b5d88">挂号管理</span>
            </template>
            <el-menu-item index="orderList" @click="menuClick('orderList')">
              <i class="iconfont icon-orders-menu" style="font-size: 18px;"> 挂号信息管理</i>
            </el-menu-item>
          </el-submenu>

          <el-submenu index="sub-menu3">
            <template slot="title">
              <span style="font-size: 18px;color: #2b5d88">医院管理</span>
            </template>
            <el-menu-item index="drugList" @click="menuClick('drugList')">
              <i class="iconfont icon-drug-menu" style="font-size: 18px;"> 药品信息管理</i>
            </el-menu-item>

            <el-menu-item index="checkList" @click="menuClick('checkList')">
              <i class="iconfont icon-checks-menu" style="font-size: 18px;"> 检查项目管理</i>
            </el-menu-item>

            <el-menu-item index="bedList" @click="menuClick('bedList')">
              <i class="iconfont icon-bed-menu" style="font-size: 18px;"> 病床分配管理</i>
            </el-menu-item>

            <el-menu-item index="arrangeIndex" @click="menuClick('arrangeIndex')">
              <i class="iconfont icon-arrange-menu" style="font-size: 18px;"> 医生排班管理</i>
            </el-menu-item>

          </el-submenu>

          <el-submenu index="submenu-4">
            <template slot="title">
              <span style="font-size: 18px;color: #2b5d88">数据统计</span>
            </template>
            <el-menu-item index="dataExpore" @click="menuClick('dataExpore')">
              <i class="iconfont icon-count-menu" style="font-size: 18px;"> 数据统计分析</i>
            </el-menu-item>
          </el-submenu>

        </el-menu>
      </el-aside>

      <el-main >
        <router-view>
        </router-view>
      </el-main>

    </el-container>
  </el-container>
</template>
<script>
import jwtDecode from "jwt-decode";
import { getToken, clearToken, getActivePath, setActivePath} from "@/utils/storage.js";
export default {
  name: "AdminUser",
  data() {
    return {
      userName: "",
      activePath:"",
    };
  },
  methods: {
    //解析Jwt的token
    getToken(token){
      if (token !== null)
      return jwtDecode(token);
    },
    //导航栏点击事件
    menuClick(path){
      this.activePath=path;
            setActivePath(path);
            if(this.$route.path !== "/"+path) this.$router.push(path);
    },
    //管理员
    logoutAdmin() {
      this.$confirm("此操作将退出登录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          clearToken();
          this.$message({
            type: "success",
            message: "退出登录成功!",
          });
          this.$router.push("login");
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消",
          });
        });
    },
  },
   created() {
    //获取激活路径
    this.activePath=getActivePath();
    //获取token
    this.userName = this.getToken(getToken()).aName;
   }
};
</script>

<style scoped lang="scss">

.title{
  cursor: pointer;
}

.el-header {
  background-color: #07278b;
  //background-color: rgb(53, 177, 21);
  display: flex;
  justify-content: space-between;
  align-items: center;
  .font_words {
    text-align: center;
    span {
      color: #ffffff;
    }
  }
  border-bottom: 1px solid lightgrey;
}

.el-container{
  height: 100%;
}

.el-aside {

  border-right: 1px solid lightgrey;
}

.el-menu{
  color: rgba(52, 114, 166, 0.18);;
  border: 0;
}

</style>
