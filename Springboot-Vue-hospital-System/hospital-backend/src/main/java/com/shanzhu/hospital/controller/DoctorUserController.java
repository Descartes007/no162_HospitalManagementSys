package com.shanzhu.hospital.controller;

import com.shanzhu.hospital.common.R;
import com.shanzhu.hospital.entity.po.Orders;
import com.shanzhu.hospital.entity.po.Patient;
import com.shanzhu.hospital.entity.vo.DoctorPageVo;
import com.shanzhu.hospital.entity.vo.user.DoctorUserVo;
import com.shanzhu.hospital.service.DoctorUserService;
import com.shanzhu.hospital.service.OrderService;
import com.shanzhu.hospital.service.PatientUserService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 医生相关 控制层
 *
 * 
 * @date: 2023-11-15
 */
@RestController
@RequestMapping("doctor")
@RequiredArgsConstructor
public class DoctorUserController {

    private final DoctorUserService doctorUserService;

    private final OrderService orderService;

    private final PatientUserService patientUserService;

    /**
     * 医生登录
     *
     * @param dId       医生id (账号)
     * @param dPassword 密码
     * @return 医生用户信息
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public R<DoctorUserVo> login(@RequestParam(value = "dId") String dId,
            @RequestParam(value = "dPassword") String dPassword) {
        // 登录医生（Service 层将负责字符串到 Integer 的兼容处理）
        DoctorUserVo doctorVo = doctorUserService.login(dId, dPassword);

        if (doctorVo == null) {
            return R.error("登录失败，密码或账号错误");
        }

        return R.ok(doctorVo);
    }

    /**
     * 查看医生当天挂号
     *
     * @param dId    医生id
     * @param oStart 日期时间
     * @return 挂号数据
     */
    @RequestMapping("findOrderByNull")
    public R<List<Orders>> findOrderByNull(
            @Param(value = "dId") Integer dId,
            @RequestParam(value = "oStart") String oStart) {

        return R.ok(orderService.findOrderByNull(dId, oStart));
    }

    /**
     * 查询患者信息
     *
     * @param pId 患者id
     * @return 患者信息
     */
    @RequestMapping("findPatientById")
    public R<Patient> findPatientById(int pId) {
        return R.ok(patientUserService.findPatientById(pId));
    }

    /**
     * 通过科室查询所有医生信息 - 分页
     *
     * @param pageNum     分页页面
     * @param pageSize    分页大小
     * @param dName       医生名字
     * @param arrangeDate 排班时间
     * @param dSection    科室
     * @return 医生信息
     */
    @RequestMapping("findDoctorBySectionPage")
    public R<DoctorPageVo> findDoctorBySectionPage(
            @RequestParam(value = "pageNumber") Integer pageNum,
            @RequestParam(value = "size") Integer pageSize,
            @RequestParam(value = "query") String dName,
            @RequestParam(value = "arrangeDate") String arrangeDate,
            @RequestParam(value = "dSection") String dSection) {
        return R.ok(doctorUserService.findDoctorBySectionPage(pageNum, pageSize, dName, arrangeDate, dSection));
    }

    /**
     * 更新评价
     *
     * @param dId   医生id
     * @param dStar 评分
     * @return 结果
     */
    @RequestMapping("updateStar")
    public R<Boolean> updateStar(Integer dId, Double dStar) {
        if (doctorUserService.updateStar(dId, dStar)) {
            return R.ok("评价成功");
        }

        return R.error("评价失败");
    }

}
