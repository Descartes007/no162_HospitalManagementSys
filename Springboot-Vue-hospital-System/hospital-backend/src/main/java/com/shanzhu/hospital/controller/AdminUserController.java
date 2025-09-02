package com.shanzhu.hospital.controller;

import com.shanzhu.hospital.common.R;
import com.shanzhu.hospital.entity.po.Doctor;
import com.shanzhu.hospital.entity.vo.DoctorPageVo;
import com.shanzhu.hospital.entity.vo.OrdersPageVo;
import com.shanzhu.hospital.entity.vo.PatientPageVo;
import com.shanzhu.hospital.entity.vo.user.AdminUserVo;
import com.shanzhu.hospital.service.AdminUserService;
import com.shanzhu.hospital.service.DoctorUserService;
import com.shanzhu.hospital.service.OrderService;
import com.shanzhu.hospital.service.PatientUserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员相关 控制层
 *
 * 
 * @date: 2023-11-13
 */
@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminService;

    private final DoctorUserService doctorUserService;

    private final PatientUserService patientUserService;

    private final OrderService orderService;

    /**
     * 管理员登录
     *
     * @param aId       管理员id （账号）
     * @param aPassword 管理员密码
     * @return 返回管理员登录信息
     */
    @PostMapping("/login")
    public R<AdminUserVo> login(@RequestParam("aId") String aId, @RequestParam("aPassword") String aPassword) {
        // 登录管理员
        AdminUserVo adminVo = adminService.login(aId, aPassword);

        if (adminVo == null) {
            return R.error("登录失败，密码或账号错误");
        }

        return R.ok(adminVo);
    }

    /**
     * 查询所有医护人员信息 - 分页
     *
     * @param pageNum  分页页码
     * @param pageSize 分页大小
     * @param query    查询条件
     * @return 医护人员信息
     */
    @RequestMapping("findAllDoctors")
    public R<DoctorPageVo> findDoctorPage(
            @RequestParam(value = "pageNumber") Integer pageNum,
            @RequestParam(value = "size") Integer pageSize,
            @RequestParam(value = "query") String query) {
        return R.ok(doctorUserService.findDoctorPage(pageNum, pageSize, query));
    }

    /**
     * 通过id查找医生
     *
     * @param dId 医生id（账号）
     * @return 医生信息
     */
    @RequestMapping("findDoctor")
    public R<Doctor> findDoctor(@RequestParam(value = "dId") int dId) {
        return R.ok(doctorUserService.findDoctor(dId));
    }

    /**
     * 添加医生
     *
     * @param doctor 医生信息
     * @return 结果
     */
    @RequestMapping("addDoctor")
    public R addDoctor(Doctor doctor) {
        if (BooleanUtils.isTrue(doctorUserService.addDoctor(doctor))) {
            return R.ok("添加医生成功");
        }

        return R.error("添加医生失败");
    }

    /**
     * 删除医生
     *
     * @param dId 医生账号
     * @return 结果
     */
    @RequestMapping("deleteDoctor")
    public R deleteDoctor(@RequestParam(value = "dId") int dId) {
        if (BooleanUtils.isTrue(doctorUserService.deleteDoctor(dId))) {
            return R.ok("删除医生成功");
        }

        return R.error("删除医生失败");
    }

    /**
     * 修改医生信息
     *
     * @param doctor 医生信息
     * @return 结果
     */
    @RequestMapping("modifyDoctor")
    public R updateDoctor(Doctor doctor) {
        if (BooleanUtils.isTrue(doctorUserService.updateDoctor(doctor))) {
            return R.ok("修改医生成功");
        }

        return R.ok("修改医生失败");
    }

    /**
     * 查询患者信息 - 分页
     *
     * @param pageNum  分页页码
     * @param pageSize 分页大小
     * @param query    查询条件
     * @return 患者数据
     */
    @RequestMapping("findAllPatients")
    public R<PatientPageVo> findPatientPage(
            @RequestParam(value = "pageNumber") Integer pageNum,
            @RequestParam(value = "size") Integer pageSize,
            @RequestParam(value = "query") String query) {
        return R.ok(patientUserService.findPatientPage(pageNum, pageSize, query));
    }

    /**
     * 删除患者
     *
     * @param pId 账号id
     * @return 结果
     */
    @RequestMapping("deletePatient")
    public R deletePatient(@RequestParam(value = "pId") int pId) {
        if (BooleanUtils.isTrue(patientUserService.deletePatient(pId))) {
            return R.ok("删除患者成功");
        }

        return R.error("删除患者失败");
    }

    /**
     * 查询挂号信息 - 分页
     *
     * @param pageNum  分页页数
     * @param pageSize 分页大小
     * @param query    查询条件
     * @return 挂号列表
     */
    @RequestMapping("findAllOrders")
    public R<OrdersPageVo> findOrdersPages(
            @RequestParam(value = "pageNumber") Integer pageNum,
            @RequestParam(value = "size") Integer pageSize,
            @RequestParam(value = "query") String query) {
        return R.ok(orderService.findOrdersPages(pageNum, pageSize, query));
    }

    /**
     * 删除挂号单
     *
     * @param oId 挂号单id
     * @return 结果
     */
    @RequestMapping("deleteOrder")
    public R deleteOrder(@RequestParam(value = "oId") Integer oId) {
        if (BooleanUtils.isTrue(orderService.deleteOrder(oId))) {
            return R.ok("删除挂号单成功");
        }

        return R.error("删除挂号单失败");
    }

}
