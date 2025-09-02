<!--
 * 管理员首页
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

export default {
    name: "AdminHome",
    data() {
        return {
        };
    },
    methods: {
    },
    created() {
    },
};
</script>
