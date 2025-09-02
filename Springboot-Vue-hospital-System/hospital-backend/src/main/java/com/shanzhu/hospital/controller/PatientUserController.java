package com.shanzhu.hospital.controller;

import com.shanzhu.hospital.common.R;
import com.shanzhu.hospital.entity.po.Orders;
import com.shanzhu.hospital.entity.po.Patient;
import com.shanzhu.hospital.entity.vo.DoctorListVo;
import com.shanzhu.hospital.entity.vo.user.PatientUserVo;
import com.shanzhu.hospital.service.DoctorUserService;
import com.shanzhu.hospital.service.OrderService;
import com.shanzhu.hospital.service.PatientUserService;
import com.shanzhu.hospital.utils.PdfUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 病患相关 控制层
 *
 * 
 * @date: 2023-11-17
 */
@RestController
@RequestMapping("patient")
@RequiredArgsConstructor
public class PatientUserController {

    private final DoctorUserService doctorUserService;

    private final PatientUserService patientUserService;

    private final OrderService orderService;

    /**
     * 病患登录
     *
     * @param pId       病患id（账号）
     * @param pPassword 密码
     * @return 病患信息
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public R<PatientUserVo> login(@RequestParam(value = "pId") String pId,
            @RequestParam(value = "pPassword") String pPassword) {
        // 登录病患（Service 层将负责字符串到 Integer 的兼容处理）
        PatientUserVo patientVo = patientUserService.login(pId, pPassword);

        if (patientVo == null) {
            return R.error("登录失败，密码或账号错误");
        }

        return R.ok(patientVo);
    }

    /**
     * 通过科室查询医生
     *
     * @param dSection 科室
     * @return 医生列表
     */
    @RequestMapping("findDoctorBySection")
    public R<DoctorListVo> findDoctorBySection(@RequestParam(value = "dSection") String dSection) {
        return R.ok(doctorUserService.findDoctorBySection(dSection));
    }

    /**
     * 添加挂号单
     *
     * @param order 挂号单信息
     * @param arId  排班id
     * @return 结果
     */
    @RequestMapping("addOrder")
    public R<Boolean> addOrder(Orders order, String arId) {
        if (BooleanUtils.isTrue(orderService.addOrder(order, arId))) {
            return R.ok("挂号成功");
        }

        return R.error("挂号失败, 当前时间段存在还未诊断的挂号单");
    }

    /**
     * 查询病患挂号
     *
     * @param pId 病患id
     * @return 挂号信息
     */
    @RequestMapping("findOrderByPid")
    public R<List<Orders>> findOrderByPid(@RequestParam(value = "pId") Integer pId) {
        return R.ok(orderService.findOrderByPid(pId));
    }

    /**
     * 添加病患
     *
     * @param patient 病患信息
     * @return 结果
     */
    @RequestMapping("addPatient")
    public R<Boolean> addPatient(Patient patient) {
        if (BooleanUtils.isTrue(patientUserService.addPatient(patient))) {
            return R.ok("添加成功");
        }

        return R.error("添加失败");
    }

    /**
     * 导出挂号单pdf
     *
     * @param response httpResponse
     * @param oId      挂号单id
     * @throws Exception 异常
     */
    @GetMapping("/pdf")
    public void exportPDF(HttpServletResponse response, Integer oId) throws Exception {
        Orders order = orderService.findOrderByOid(oId);
        // 导出pdf
        PdfUtil.exportPatientOrder(response, order);
    }

    /**
     * 统计患者年龄分布
     *
     * @return 年龄分布
     */
    @RequestMapping("patientAge")
    public R<List<Integer>> patientAge() {
        return R.ok(patientUserService.patientAge());
    }
}
