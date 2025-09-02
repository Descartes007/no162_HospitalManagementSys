package com.shanzhu.hospital.service.serviceImpl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.shanzhu.hospital.entity.po.Patient;
import com.shanzhu.hospital.entity.vo.PatientPageVo;
import com.shanzhu.hospital.entity.vo.user.PatientUserVo;
import com.shanzhu.hospital.mapper.PatientUserMapper;
import com.shanzhu.hospital.service.PatientUserService;
import com.shanzhu.hospital.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 患者相关 服务层
 *
 * @author: ShanZhu
 * @date: 2023-11-15
 */
@Slf4j
@Service("PatientService")
public class PatientUserServiceImpl extends ServiceImpl<PatientUserMapper, Patient> implements PatientUserService {

    @Resource
    private PatientUserMapper patientMapper;

    /**
     * 病患登录
     *
     * @param pId       病患id（账号）
     * @param pPassword 密码
     * @return 病患信息
     */
    @Override
    public PatientUserVo login(String pId, String pPassword) {
        // 支持字符串账号：尝试将 pId 转为 Integer（兼容原主键为 Integer 的设计）
        Integer id = null;
        try {
            id = Integer.valueOf(pId);
        } catch (NumberFormatException ex) {
            return null;
        }

        // 通过病患id（账号）查询病患
        Patient patient = this.getById(id);

        // 查找不到记录
        if (patient == null) {
            return null;
        }

        // 密码错误
        if (BooleanUtils.isFalse(
                (patient.getPPassword()).equals(DigestUtils.md5DigestAsHex(pPassword.getBytes())))) {
            return null;
        }

        // 组装接口数据
        PatientUserVo userVo = new PatientUserVo();
        userVo.setPId(patient.getPId());
        userVo.setPName(patient.getPName());
        userVo.setPCard(patient.getPCard());
        userVo.setToken(generateToken(patient));

        return userVo;
    }

    /**
     * 查询患者信息 - 分页
     *
     * @param pageNum  分页页码
     * @param pageSize 分页大小
     * @param query    查询条件
     * @return 患者数据
     */
    @Override
    public PatientPageVo findPatientPage(Integer pageNum, Integer pageSize, String query) {
        // 分页条件
        Page<Patient> page = new Page<>(pageNum, pageSize);

        // 查询条件
        LambdaQueryWrapper<Patient> lambdaQuery = Wrappers.<Patient>lambdaQuery()
                .like(Patient::getPName, query)
                .orderByDesc(Patient::getPState);

        // 分页查询
        IPage<Patient> iPage = this.page(page, lambdaQuery);

        // 组装分页结果
        PatientPageVo pageVo = new PatientPageVo();
        pageVo.populatePage(iPage);

        return pageVo;
    }

    /**
     * 删除患者
     *
     * @param pId 账号id
     * @return 结果
     */
    @Override
    public Boolean deletePatient(int pId) {
        Patient patient = new Patient();
        patient.setPId(pId);
        // 设置为删除状态
        patient.setPState(0);

        return this.updateById(patient);
    }

    /**
     * 查询患者信息
     *
     * @param pId 患者id
     * @return 患者信息
     */
    @Override
    public Patient findPatientById(Integer pId) {
        return lambdaQuery().eq(Patient::getPId, pId).one();
    }

    /**
     * 添加病患
     *
     * @param patient 病患信息
     * @return 结果
     */
    @Override
    public Boolean addPatient(Patient patient) {
        // 查询病患
        Patient existPatient = lambdaQuery().eq(Patient::getPId, patient.getPId()).one();

        // 存在已添加病患信息
        if (existPatient != null) {
            return null;
        }

        // 出生年份
        Integer birthYear = Integer.parseInt(
                StrUtil.sub(patient.getPBirthday(), 0, 4));
        // 当前年份
        Integer currentYear = DateUtil.year(new Date());

        // 病患信息
        patient.setPPassword(DigestUtils.md5DigestAsHex(patient.getPPassword().getBytes()));
        patient.setPAge(currentYear - birthYear);
        patient.setPState(1);

        // 保存病患信息
        return this.save(patient);
    }

    /**
     * 统计患者年龄分布
     *
     * @return 年龄分布
     */
    public List<Integer> patientAge() {
        List<Integer> ageList = new ArrayList<>();

        Integer age1 = this.patientMapper.patientAge(0, 9);
        Integer age2 = this.patientMapper.patientAge(10, 19);
        Integer age3 = this.patientMapper.patientAge(20, 29);
        Integer age4 = this.patientMapper.patientAge(30, 39);
        Integer age5 = this.patientMapper.patientAge(40, 49);
        Integer age6 = this.patientMapper.patientAge(50, 59);
        Integer age7 = this.patientMapper.patientAge(60, 69);
        Integer age8 = this.patientMapper.patientAge(70, 79);
        Integer age9 = this.patientMapper.patientAge(80, 89);
        Integer age10 = this.patientMapper.patientAge(90, 99);

        ageList.add(age1);
        ageList.add(age2);
        ageList.add(age3);
        ageList.add(age4);
        ageList.add(age5);
        ageList.add(age6);
        ageList.add(age7);
        ageList.add(age8);
        ageList.add(age9);
        ageList.add(age10);

        return ageList;
    }

    /**
     * 生成token
     *
     * @param patient 病患信息
     * @return token
     */
    private String generateToken(Patient patient) {
        Map<String, String> doctorMap = Maps.newHashMap();
        doctorMap.put("pId", String.valueOf(patient.getPId()));
        doctorMap.put("pName", patient.getPName());
        doctorMap.put("pCard", patient.getPCard());
        return JwtUtil.generateToken(doctorMap);
    }

}
