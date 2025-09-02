<!--
 * 药物管理列表
 *
 * @Author: ShanZhu
 * @Date: 2023-11-17
-->
<template>
    <div>
        <el-card>
            <!--搜索栏 -->
            <el-row type="flex">
                <el-col :span="8">
                    <el-input v-model="query" placeholder="请输入药物名称查询">
                        <el-button
                            round
                            slot="append"
                            icon="iconfont icon-search-button"
                            @click="requestDrugs"
                        ></el-button>
                    </el-input>
                </el-col>
                <el-col :span="1"></el-col>
            </el-row>

            <el-row type="flex">
                <el-col :span="4">
                    <br>
                    <!--添加药物按钮-->
                    <el-button type="success" size="small" @click="addFormVisible = true" style="font-size: 15px">
                    <i class="iconfont icon-add-button" style="font-size: 15px;"></i>
                        添加药物
                    </el-button>
                </el-col>
            </el-row>
            <!--表格-->
            <el-table :data="drugData" size="small" stripe style="width: 100%" border>
                <el-table-column label="编号" prop="drId"/>
                <el-table-column label="名称" prop="drName"/>
                <el-table-column label="剩余数量" prop="drNumber"/>
                <el-table-column label="单位" prop="drUnit"/>
                <el-table-column label="单价" prop="drPrice"/>
                <el-table-column label="出版商"prop="drPublisher"/>
                <el-table-column label="操作" width="200" fixed="right">
                    <template slot-scope="scope">
                        <el-button
                            size="small"
                            style="font-size: 14px"
                            type="success"
                            @click="modifyDialog(scope.row.drId)"
                        ><i class="iconfont icon-edit-button" style="font-size: 12px;"></i></el-button>
                        <el-button
                            size="small"
                            style="font-size: 14px"
                            type="danger"
                            @click="deleteDialog(scope.row.drId)"
                        ><i class="iconfont icon-delete-button" style="font-size: 12px;"></i></el-button>
                    </template>
                </el-table-column>
            </el-table>

            <!-- 分页控件 -->
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

        <!-- 增加药物对话框 -->
        <el-dialog title="增加药物" :visible.sync="addFormVisible">
            <el-form :model="addForm" :rules="rules" ref="ruleForm">
                <el-form-item label="编号" prop="drId" label-width="80px">
                    <el-input v-model.number="addForm.drId"></el-input>
                </el-form-item>
                <el-form-item label="名称" prop="drName" label-width="80px">
                    <el-input v-model="addForm.drName"></el-input>
                </el-form-item>
                <el-form-item label="数量" prop="drNumber" label-width="80px">
                    <el-input-number
                        v-model="addForm.drNumber"
                        :min="0"
                        :max="1000"
                    ></el-input-number>
                </el-form-item>
                <el-form-item label="单位" prop="drUnit" label-width="80px">
                    <el-radio v-model="addForm.drUnit" label="盒">盒</el-radio>
                    <el-radio v-model="addForm.drUnit" label="袋">袋</el-radio>
                    <el-radio v-model="addForm.drUnit" label="剂">剂</el-radio>
                </el-form-item>
                <el-form-item label="单价" prop="drPrice" label-width="80px">
                    <el-input v-model="addForm.drPrice"></el-input>
                </el-form-item>
                <el-form-item label="出版商"
                    prop="drPublisher"
                    label-width="80px"
                >
                    <el-input v-model="addForm.drPublisher"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button size="mini" @click="addFormVisible = false" style="font-size: 18px;">
                  <i class="iconfont icon-cancel-button" size="small" style="font-size: 20px;"></i> 取 消
                </el-button>
                <el-button size="mini" type="primary"@click="addDrug('ruleForm')"style="font-size: 18px;">
                  <i class="iconfont icon-sure-button"  style="font-size: 20px;"></i>确 定
                </el-button>
            </div>
        </el-dialog>

        <!-- 修改药物对话框 -->
        <el-dialog title="修改药物" :visible.sync="modifyFormVisible">
            <el-form :model="modifyForm" :rules="rules" ref="ruleForm">
                <el-form-item label="编号" prop="drId" label-width="80px">
                    <el-input
                        v-model.number="modifyForm.drId"
                        disabled
                    ></el-input>
                </el-form-item>
                <el-form-item label="名称" prop="drName" label-width="80px">
                    <el-input v-model="modifyForm.drName"></el-input>
                </el-form-item>
                <el-form-item label="数量" prop="drNumber" label-width="80px">
                    <el-input-number
                        v-model="modifyForm.drNumber"
                        :min="0"
                        :max="1000"
                    ></el-input-number>
                </el-form-item>
                <el-form-item label="单位" prop="drUnit" label-width="80px">
                    <el-radio v-model="modifyForm.drUnit" label="盒"
                        >盒</el-radio
                    >
                    <el-radio v-model="modifyForm.drUnit" label="袋"
                        >袋</el-radio
                    >
                    <el-radio v-model="modifyForm.drUnit" label="剂"
                        >剂</el-radio
                    >
                </el-form-item>
                <el-form-item label="单价" prop="drPrice" label-width="80px">
                    <el-input v-model="modifyForm.drPrice"></el-input>
                </el-form-item>
                <el-form-item
                    label="出版商"
                    prop="drPublisher"
                    label-width="80px"
                >
                    <el-input v-model="modifyForm.drPublisher"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button size="mini" @click="modifyFormVisible = false"  style="font-size: 18px;">
                  <i class="iconfont icon-cancel-button"  style="font-size: 20px;"></i> 取 消
                </el-button>
                <el-button size="mini" type="primary"  @click="modifyDrug('ruleForm')"style="font-size: 18px;">
                  <i class="iconfont icon-sure-button" style="font-size: 20px;"></i> 确 定
                </el-button>
            </div>
        </el-dialog>
    </div>
</template>
<script>
import request from "@/utils/request.js";
export default {
    name: "DrugList",
    data() {
        return {
            pageNumber: 1,
            size: 8,
            query: "",
            drugData: [],
            total: 3,
            addFormVisible: false,
            addForm: {},
            rules: {
                drId: [
                    { required: true, message: "请输入编号", trigger: "blur" },
                    {
                        type: "number",
                        message: "账号必须数字类型",
                        trigger: "blur",
                    },
                ],
                drName: [
                    { required: true, message: "请输入名称", trigger: "blur" },
                    {
                        min: 1,
                        max: 50,
                        message: "账号必须是1到50个字符",
                        trigger: "blur",
                    },
                ],
                drUnit: [
                    { required: true, message: "请选择单位", trigger: "blur" },
                ],
                drPrice: [
                    { required: true, message: "请输入单价", trigger: "blur" },
                ],
                drPublisher: [
                    {
                        required: true,
                        message: "请输入出版商",
                        trigger: "blur",
                    },
                    {
                        min: 1,
                        max: 50,
                        message: "账号必须是1到50个字符",
                        trigger: "blur",
                    },
                ],
            },
            modifyFormVisible: false,
            modifyForm: {},
        };
    },
    methods: {
        //点击修改药物信息
        modifyDrug(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request
                        .get("drug/modifyDrug", {
                            params: {
                                drId: this.modifyForm.drId,
                                drName: this.modifyForm.drName,
                                drNumber: this.modifyForm.drNumber,
                                drPrice: this.modifyForm.drPrice,
                                drUnit: this.modifyForm.drUnit,
                                drPublisher: this.modifyForm.drPublisher,
                            },
                        })
                        .then((res) => {
                            if (res.data.status !== 200)
                                return this.$message.error("修改信息失败！");
                            this.modifyFormVisible = false;
                            this.requestDrugs();
                            this.$message.success("修改药物信息成功！");
                        });
                } else {
                    return false;
                }
            });
        },
        //打开修改对话框
        modifyDialog(id) {
            request
                .get("drug/findDrug", {
                    params: {
                        drId: id,
                    },
                })
                .then((res) => {
                    if (res.data.status !== 200)
                        return this.$message.error("请求数据失败");
                    this.modifyForm = res.data.data;
                    this.modifyFormVisible = true;
                });
        },
        //删除药物操作
        deleteDrug(id) {
            request
                .get("drug/deleteDrug", {
                    params: {
                        drId: id,
                    },
                })
                .then((res) => {
                    this.requestDrugs();
                });
        },
        //删除对话框
        deleteDialog(id) {
            this.$confirm("此操作将删除该药物信息, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then(() => {
                    this.deleteDrug(id);
                    this.$message({
                        type: "success",
                        message: "删除成功!",
                    });
                })
                .catch(() => {
                    this.$message({
                        type: "info",
                        message: "已取消删除",
                    });
                });
        },
        //点击增加确认按钮
        addDrug(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request
                        .get("drug/addDrug", {
                            params: {
                                drId: this.addForm.drId,
                                drName: this.addForm.drName,
                                drNumber: this.addForm.drNumber,
                                drPrice: this.addForm.drPrice,
                                drUnit: this.addForm.drUnit,
                                drPublisher: this.addForm.drPublisher,
                            },
                        })
                        .then((res) => {
                            if (res.data.status !== 200)
                                return this.$message.error(
                                    "编号不合法或已被占用！"
                                );
                            this.addFormVisible = false;
                            this.requestDrugs();
                            this.$message.success("增加药物成功！");
                        });
                } else {
                    return false;
                }
            });
        },
        //页面大小改变时触发
        handleSizeChange(size) {
            this.size = size;
            this.requestDrugs();
        },
        //页码改变时触发
        handleCurrentChange(num) {
            this.pageNumber = num;
            this.requestDrugs();
        },
        //加载药物列表
        requestDrugs() {
            request
                .get("drug/findAllDrugs", {
                    params: {
                        pageNumber: this.pageNumber,
                        size: this.size,
                        query: this.query,
                    },
                })
                .then((res) => {
                    this.drugData = res.data.data.drugs;
                    this.total = res.data.data.total;
                });
        },
    },
    created() {
        this.requestDrugs();
    },
};
</script>
<style scoped lang="scss">
.el-table {
    margin-top: 20px;
    margin-bottom: 20px;
}
.el-form {
    margin-top: 0;
}
</style>
