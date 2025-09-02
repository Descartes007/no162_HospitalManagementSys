<!--
 * 患者管理主布局
 *
 * @Author: ShanZhu
 * @Date: 2023-11-17
-->
<template>
    <el-container>

      <!-- 头部 -->
      <el-header>
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
          <span>欢迎您，<b>{{ userName }}</b>&nbsp;病人&nbsp;</span>
          <span><el-button type="danger" round size="mini" @click="logoutPatient">退出</el-button></span>
        </div>
      </el-header>
        <el-container>
            <!-- 侧边栏 -->
            <el-aside width="200px">
                <!-- 导航菜单 -->
                <el-menu  background-color="white"
                    text-color="black"
                    active-text-color="black"
                    :default-active="activePath"
                    style="font-size: 22px"
                >

                    <el-menu-item  index="patientLayout"  @click="menuClick('patientLayout')">
                        <i class="iconfont icon-homepage-menu" style="font-size: 18px">首页</i>
                    </el-menu-item>
                    <el-menu-item index="oderOperate" @click="menuClick('orderOperate')">
                        <i class="iconfont icon-arrange-menu" style="font-size: 18px">预约挂号</i>
                    </el-menu-item>
                    <el-menu-item index="myOrder" @click="menuClick('myOrder')">
                        <i class="iconfont icon-orders-menu" style="font-size: 18px">我的挂号</i>
                    </el-menu-item>
                    <el-menu-item index="myBed" @click="menuClick('myBed')">
                        <i class="iconfont icon-bed-menu" style="font-size: 18px">住院信息</i>
                    </el-menu-item>
                    <el-menu-item index="patientCard"@click="menuClick('patientCard')">
                        <i class="iconfont icon-doctor-menu" style="font-size: 18px"> 个人信息</i>
                    </el-menu-item>

                </el-menu>
            </el-aside>
            <el-main>
                <!-- 子路由入口 -->
                <router-view></router-view>
            </el-main>
        </el-container>
    </el-container>
</template>
<script>
import jwtDecode from "jwt-decode";
import {
    getToken,
    clearToken,
    getActivePath,
    setActivePath,
} from "@/utils/storage.js";
export default {
    name: "Patient",
    data() {
        return {
            userName: "",
            activePath: "",
        };
    },
    methods: {
        //token解码
        tokenDecode(token) {
            if (token !== null) return jwtDecode(token);
        },
        //导航栏点击事件
        menuClick(path) {
            this.activePath = path;
            setActivePath(path);
            if (this.$route.path !== "/" + path) this.$router.push(path);
        },
        //退出登录
        logoutPatient() {
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
        this.activePath = getActivePath();
        //解码token
        this.userName = this.tokenDecode(getToken()).pName;
    },
};
</script>
<style scoped lang="scss">
.title {
    cursor: pointer;
}
.el-header {
  background-color: #07278b;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid lightgrey;
    .font_words {
        text-align: center;
        span {
            color: #ffffff;
        }
    }
}
.el-container {
    height: 100%;
}
.el-aside {
    background-color: white;
    border-right: 1px solid lightgrey;
}
.el-menu {
    border: 0;
}
</style>
