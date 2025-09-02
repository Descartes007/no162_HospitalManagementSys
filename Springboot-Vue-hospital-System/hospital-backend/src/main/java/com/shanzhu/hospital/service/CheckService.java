package com.shanzhu.hospital.service;

import com.shanzhu.hospital.entity.po.Checks;
import com.shanzhu.hospital.entity.vo.ChecksPageVo;

/**
 * 检查项目 服务层
 *
 
 * @date: 2023-11-15
 */
public interface CheckService {

    /**
     * 查询检查项目 分页
     *
     * @param pageNum  分页页面
     * @param pageSize 分页大小
     * @param query    查询条件
     * @return 检查项目
     */
    ChecksPageVo findChecksPage(Integer pageNum, Integer pageSize, String query);

    /**
     * 通过id查询检查项目
     *
     * @param chId 检查项目id
     * @return 检查项目
     */
    Checks findCheck(Integer chId);

    /**
     * 添加检查项目
     *
     * @param checks 检查项目
     * @return 结果
     */
    Boolean addCheck(Checks checks);

    /**
     * 删除检查项目
     *
     * @param chId 检查项目id
     * @return 结果
     */
    Boolean deleteChecks(Integer chId);

    /**
     * 更新检查项目
     *
     * @param checks 检查项目信息
     * @return 结果
     */
    Boolean updateChecks(Checks checks);
}
