package com.shanzhu.hospital.service;

import com.shanzhu.hospital.entity.po.Doctor;
import com.shanzhu.hospital.entity.vo.DoctorListVo;
import com.shanzhu.hospital.entity.vo.DoctorPageVo;
import com.shanzhu.hospital.entity.vo.user.DoctorUserVo;

/**
 * 医生相关 服务层
 *
 * 
 * @date: 2023-11-15
 */
public interface DoctorUserService {

    /**
     * 医生登录
     *
     * @param dId       医生id (账号)
     * @param dPassword 密码
     * @return 医生用户信息
     */
    DoctorUserVo login(String dId, String dPassword);

    /**
     * 查询所有医护人员信息 - 分页
     *
     * @param pageNum  分页页码
     * @param pageSize 分页大小
     * @param query    查询条件
     * @return 医护人员信息
     */
    DoctorPageVo findDoctorPage(Integer pageNum, Integer pageSize, String query);

    /**
     * 通过id查找医生
     *
     * @param dId 医生id（账号）
     * @return 医生信息
     */
    Doctor findDoctor(Integer dId);

    /**
     * 添加医生
     *
     * @param doctor 医生信息
     * @return 结果
     */
    Boolean addDoctor(Doctor doctor);

    /**
     * 删除医生
     *
     * @param dId 医生账号
     * @return 结果
     */
    Boolean deleteDoctor(int dId);

    /**
     * 更新医生信息
     *
     * @param doctor 医生信息
     * @return 结果
     */
    Boolean updateDoctor(Doctor doctor);

    /**
     * 通过科室查询医生
     *
     * @param dSection 科室
     * @return 医生列表
     */
    DoctorListVo findDoctorBySection(String dSection);

    /**
     * 通过科室查询所有医生信息 - 分页
     *
     * @param pageNum     分页页面
     * @param pageSize    分页大小
     * @param dName       医生名字
     * @param arrangeDate 排班时间
     * @param dSection    科室
     * @return 医生列表
     */
    DoctorPageVo findDoctorBySectionPage(
            Integer pageNum, Integer pageSize, String dName, String arrangeDate, String dSection);

    /**
     * 更新评价
     *
     * @param dId   医生id
     * @param dStar 评分
     * @return 结果
     */
    Boolean updateStar(Integer dId, Double dStar);

}
