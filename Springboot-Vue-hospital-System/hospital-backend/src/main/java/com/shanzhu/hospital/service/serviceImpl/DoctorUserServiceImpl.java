package com.shanzhu.hospital.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.shanzhu.hospital.entity.po.Arrange;
import com.shanzhu.hospital.entity.po.Doctor;
import com.shanzhu.hospital.entity.vo.DoctorListVo;
import com.shanzhu.hospital.entity.vo.DoctorPageVo;
import com.shanzhu.hospital.entity.vo.user.DoctorUserVo;
import com.shanzhu.hospital.mapper.ArrangeMapper;
import com.shanzhu.hospital.mapper.DoctorUserMapper;
import com.shanzhu.hospital.service.DoctorUserService;
import com.shanzhu.hospital.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Map;

/**
 * 医生相关 服务层
 *
 * @author: ShanZhu
 * @date: 2023-11-15
 */
@Slf4j
@Service("DoctorService")
@RequiredArgsConstructor
public class DoctorUserServiceImpl extends ServiceImpl<DoctorUserMapper, Doctor> implements DoctorUserService {

    private final DoctorUserMapper doctorUserMapper;

    private final ArrangeMapper arrangeMapper;

    /**
     * 医生登录
     *
     * @param dId       医生id (账号)
     * @param dPassword 密码
     * @return 医生用户信息
     */
    @Override
    public DoctorUserVo login(String dId, String dPassword) {
        // 支持字符串账号：尝试将 dId 转为 Integer（兼容原主键为 Integer 的设计）
        Integer id = null;
        try {
            id = Integer.valueOf(dId);
        } catch (NumberFormatException ex) {
            // 无法转换为数字时，暂不支持按用户名查找，则认为不存在
            return null;
        }

        // 通过医生id（账号）查询医生
        Doctor doctor = this.getById(id);

        // 查询不到账号记录
        if (doctor == null) {
            return null;
        }

        // 密码错误
        if (BooleanUtils.isFalse(
                doctor.getDPassword().equals(DigestUtils.md5DigestAsHex(dPassword.getBytes())))) {
            return null;
        }

        // 组装接口数据结果
        DoctorUserVo vo = new DoctorUserVo();
        vo.setDId(doctor.getDId());
        vo.setAName(doctor.getDName());
        vo.setToken(generateToken(doctor));

        return vo;
    }

    /**
     * 查询所有医护人员信息 - 分页
     *
     * @param pageNum  分页页码
     * @param pageSize 分页大小
     * @param query    查询条件
     * @return 医护人员信息
     */
    @Override
    public DoctorPageVo findDoctorPage(Integer pageNum, Integer pageSize, String query) {
        // 分页条件
        Page<Doctor> page = new Page<>(pageNum, pageSize);

        // 查询条件
        LambdaQueryWrapper<Doctor> lambadaQuery = Wrappers.<Doctor>lambdaQuery()
                // 模糊匹配 姓名
                .like(Doctor::getDName, query)
                // 是否在职倒序（优先展示在职的）
                .orderByDesc(Doctor::getDState);

        // 分页查询
        IPage<Doctor> iPage = this.page(page, lambadaQuery);

        // 组装分页返回对象
        DoctorPageVo doctorVo = new DoctorPageVo();
        doctorVo.populatePage(iPage);

        return doctorVo;
    }

    /**
     * 通过id查找医生
     *
     * @param dId 医生id（账号）
     * @return 医生信息
     */
    @Override
    public Doctor findDoctor(Integer dId) {
        return this.getById(dId);
    }

    /**
     * 添加医生
     *
     * @param doctor 医生信息
     * @return 结果
     */
    @Override
    public Boolean addDoctor(Doctor doctor) {
        // 通过 dId（医生账号）查询数据库是否存在记录
        Doctor existDoctor = lambdaQuery()
                // dId 相等的医生数据
                .eq(Doctor::getDId, doctor.getDId())
                .one();

        // 新增的医生账号已经存在
        if (BooleanUtils.isFalse(existDoctor == null)) {
            return Boolean.FALSE;
        }

        // 密码加密
        String password = DigestUtils.md5DigestAsHex(doctor.getDPassword().getBytes());

        // 设置医生数据
        doctor.setDPassword(password);
        doctor.setDState(1);
        doctor.setDStar(0.00);
        doctor.setDPeople(0);

        // 保存
        return this.save(doctor);
    }

    /**
     * 删除医生
     *
     * @param dId 医生账号
     * @return 结果
     */
    @Override
    public Boolean deleteDoctor(int dId) {
        // 更新为不在职状态
        Doctor doctor = new Doctor();
        doctor.setDId(dId);
        doctor.setDState(0);

        return this.updateById(doctor);
    }

    /**
     * 更新医生信息
     *
     * @param doctor 医生信息
     * @return 结果
     */
    @Override
    public Boolean updateDoctor(Doctor doctor) {
        // 更新
        return this.updateById(doctor);
    }

    /**
     * 通过科室查询医生
     *
     * @param dSection 科室
     * @return 医生列表
     */
    @Override
    public DoctorListVo findDoctorBySection(String dSection) {
        // 查询医生
        List<Doctor> doctors = lambdaQuery()
                // 科室
                .eq(Doctor::getDSection, dSection)
                // 在职状态
                .eq(Doctor::getDState, 1)
                .list();

        // 组装数据
        DoctorListVo doctorListVo = new DoctorListVo();
        doctorListVo.setDoctors(doctors);

        return doctorListVo;

    }

    /**
     * 通过科室查询所有医生信息 - 分页
     *
     * @param pageNum     分页页面
     * @param pageSize    分页大小
     * @param dName       查询条件（医生名字）
     * @param arrangeDate 排班时间
     * @param dSection    科室
     * @return 医生信息
     */
    @Override
    public DoctorPageVo findDoctorBySectionPage(
            Integer pageNum, Integer pageSize, String dName, String arrangeDate, String dSection) {
        // 分页条件
        Page<Doctor> page = new Page<>(pageNum, pageSize);

        // 查询条件
        LambdaQueryWrapper<Doctor> lambdaQuery = Wrappers.<Doctor>lambdaQuery()
                // 模糊匹配医生名字
                .like(Doctor::getDName, dName)
                // 科室
                .eq(Doctor::getDSection, dSection)
                // 按在职状态排序
                .orderByDesc(Doctor::getDState);

        // 分页查询
        IPage<Doctor> iPage = this.page(page, lambdaQuery);

        // 组装分页结果
        DoctorPageVo pageVo = new DoctorPageVo();
        pageVo.populatePage(iPage);

        // 查询医生是否已排班
        List<Doctor> records = iPage.getRecords();
        for (Doctor doctor : records) {
            // 查询条件
            LambdaQueryWrapper<Arrange> arrangeQuery = Wrappers.<Arrange>lambdaQuery()
                    // 排班时间
                    .eq(Arrange::getArTime, arrangeDate)
                    // 医生id
                    .eq(Arrange::getDId, doctor.getDId());

            // 查询医生排班
            Arrange arrange = arrangeMapper.selectOne(arrangeQuery);

            if (arrange != null) {
                // 设置医生的排班信息
                doctor.setArrangeId(arrange.getArId());
            }
        }

        return pageVo;
    }

    /**
     * 更新评价
     *
     * @param dId   医生id
     * @param dStar 评分
     * @return 结果
     */
    public Boolean updateStar(Integer dId, Double dStar) {
        return doctorUserMapper.updateStar(dId, dStar);
    }

    /**
     * 生成token
     *
     * @param doctor 医生信息
     * @return token
     */
    private String generateToken(Doctor doctor) {
        Map<String, String> doctorMap = Maps.newHashMap();
        doctorMap.put("dId", String.valueOf(doctor.getDId()));
        doctorMap.put("dName", doctor.getDName());
        return JwtUtil.generateToken(doctorMap);
    }

}
