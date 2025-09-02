package com.shanzhu.hospital.service;

import com.shanzhu.hospital.entity.vo.user.AdminUserVo;

/**
 * 管理员相关 服务层
 *
 * 
 * @date: 2023-11-15
 */
public interface AdminUserService {

    /**
     * 管理员登录
     *
     * @param aId       管理员id （账号）
     * @param aPassword 管理员密码
     * @return 返回管理员登录信息
     */
    AdminUserVo login(String aId, String aPassword);

}
