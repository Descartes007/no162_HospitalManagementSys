package com.shanzhu.hospital.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.shanzhu.hospital.entity.po.Admin;
import com.shanzhu.hospital.entity.vo.user.AdminUserVo;
import com.shanzhu.hospital.mapper.AdminUserMapper;
import com.shanzhu.hospital.service.AdminUserService;
import com.shanzhu.hospital.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 管理员相关 服务层
 *
 * @author: ShanZhu
 * @date: 2023-11-15
 */
@Service("AdminService")
@RequiredArgsConstructor
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, Admin> implements AdminUserService {

    /**
     * 管理员登录
     *
     * @param aId       管理员id （账号）
     * @param aPassword 管理员密码
     * @return 返回管理员登录信息
     */
    @Override
    public AdminUserVo login(String aId, String aPassword) {
        // 支持字符串账号：尝试将 aId 转为 Integer（兼容原主键为 Integer 的设计）
        Integer id = null;
        try {
            id = Integer.valueOf(aId);
        } catch (NumberFormatException ex) {
            // 无法转换为数字时，暂不支持按用户名查找，则认为不存在
            return null;
        }

        //通过id（账号）查询管理员记录
        Admin admin = this.getById(id);

        //通过账号查询不到记录
        if (admin == null) {
            return null;
        }

        //密码错误
        if (BooleanUtils.isFalse(admin.getAPassword().equals(aPassword))) {
            return null;
        }

        //组装接口数据结果
        AdminUserVo vo = new AdminUserVo();
        vo.setAId(admin.getAId());
        vo.setAName(admin.getAName());
        vo.setToken(generateToken(admin));

        return vo;
    }

    /**
     * 生成token
     *
     * @param admin 管理员信息
     * @return token
     */
    private String generateToken(Admin admin) {
        Map<String, String> adminMap = Maps.newHashMap();
        adminMap.put("aName", admin.getAName());
        adminMap.put("aId", String.valueOf(admin.getAId()));
        return JwtUtil.generateToken(adminMap);
    }

}
