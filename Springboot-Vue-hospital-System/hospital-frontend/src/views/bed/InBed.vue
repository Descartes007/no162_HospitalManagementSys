<!--
 * 患者住院列表
 *
 * @Author: ShanZhu
 * @Date: 2023-11-17
-->
<template>
    <div>
        <el-card>
            <!-- 搜索栏 -->
            <el-row type="flex">
                <el-col :span="8">
                    <el-input v-model="query" placeholder="请输入患者id查询">
                        <el-button
                            slot="append"
                            icon="iconfont icon-search-button"
                            @click="requestOrders"
                        ></el-button>
                    </el-input>
                </el-col>
            </el-row>

            <el-table size="small" :data="orderData" stripe border>
                <el-table-column label="挂号单号" prop="oId"/>
                <el-table-column label="患者id" prop="pId"/>
                <el-table-column label="医生id" prop="dId"/>
                <el-table-column label="挂号时间"  prop="oStart"/>
                <el-table-column label="结束时间" prop="oEnd"/>
                <el-table-column label="挂号状态" prop="oState">
                    <template slot-scope="scope">
                        <el-tag v-if="scope.row.oState === 1" type="primary"
                            >已完成
                        </el-tag>
                        <el-tag v-if="scope.row.oState === 0" type="danger"
                            >未完成
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="160" fixed="right">
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            type="warning"
                            style="font-size: 14px"
                            @click="BedDiag(scope.row.pId, scope.row.dId)"
                        >
                          <i class="iconfont icon-hotel-button" style="font-size: 12px;"></i>申请住院</el-button
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
        </el-card>

        <!-- 住院对话框 -->
        <el-dialog title="申请住院" :visible.sync="BedFormVisible">
            <el-form class="findPassword" :model="bedForm">
                <el-form-item label="患者账号" label-width="80px" prop="pId">
                    <el-input v-model="bedForm.pId" disabled></el-input>
                </el-form-item>
                <el-form-item label="医生账号" label-width="80px">
                    <el-input v-model="bedForm.dId" disabled></el-input>
                </el-form-item>
                <el-form-item label="申请原因" label-width="80px">
                    <el-input
                        v-model="bedForm.bReason"
                        type="textarea"
                        :rows="4"
                    ></el-input>
                </el-form-item>

                <el-form-item label="病床号" label-width="80px" prop="bId">
                    <el-select v-model="bedForm.bId">
                        <el-option
                            v-for="item in nullBed"
                            :key="item.bId"
                            :label="item.bId"
                            :value="item.bId"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button size="mini" @click="BedFormVisible = false" style="font-size: 18px;"><i class="iconfont icon-cancel-button" style="font-size: 20px;"></i> 取 消</el-button>
                <el-button size="mini" type="primary" @click="bedClick"style="font-size: 18px;"><i class="iconfont icon-sure-button" style="font-size: 20px;"></i> 确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>
<script>
import jwtDecode from "jwt-decode";
import { getToken } from "@/utils/storage.js";
import request from "@/utils/request.js";
export default {
    name: "InBed",
    data() {
        return {
            userId: 1,
            userName: "",
            pageNumber: 1,
            size: 4,
            query: "",
            total: 3,
            orderData: [],
            //申请住院对话框
            BedFormVisible: false,
            bedForm: {},
            nullBed: [],
        };
    },
    methods: {
        //点击申请床位确认按钮
        bedClick() {
            request
                .get("bed/updateBed", {
                    params: {
                        bId: this.bedForm.bId,
                        dId: this.bedForm.dId,
                        pId: this.bedForm.pId,
                        bReason: this.bedForm.bReason,
                    },
                })
                .then((res) => {
                    if (res.data.status !== 200)
                        return this.$message.error("来晚了...该床位已被占用");
                    this.BedFormVisible = false;
                    this.$message.success("申请住院成功！");
                    this.requestOrders();
                });
        },

        //请求所有空床位
        requestBeds() {
            request
                .get("bed/findNullBed")
                .then((res) => {
                    if (res.data.status !== 200)
                        return this.$message.error("数据请求失败");
                    this.nullBed = res.data.data;
                })
                .catch((err) => {
                });
        },
        //打开申请住院对话框
        BedDiag(pId, dId) {
            this.bedForm.pId = pId;
            this.bedForm.dId = dId;
            this.BedFormVisible = true;
            this.requestBeds();
        },
        //页面大小改变时触发
        handleSizeChange(size) {
            this.size = size;
            this.requestOrders();
        },
        //页码改变时触发
        handleCurrentChange(num) {
            this.pageNumber = num;
            this.requestOrders();
        },
        //获取已完成的订单信息
        requestOrders() {
            request
                .get("order/findOrderFinish", {
                    params: {
                        dId: this.userId,
                        pageNumber: this.pageNumber,
                        size: this.size,
                        query: this.query,
                    },
                })
                .then((res) => {
                    if (res.data.status !== 200)
                        return this.$message.error("数据请求失败");
                    this.orderData = res.data.data.orders;
                    this.total = res.data.data.total;
                });
        },
        //token解码
        tokenDecode(token) {
            if (token !== null) return jwtDecode(token);
        },
    },
    created() {
        //解码token信息
        this.userId = this.tokenDecode(getToken()).dId;
        this.userName = this.tokenDecode(getToken()).dName;
        //获取订单信息
        this.requestOrders();
    },
};
</script>
<style lang="scss" scoped>
.el-table {
    margin-top: 20px;
    margin-bottom: 20px;
}
</style>
