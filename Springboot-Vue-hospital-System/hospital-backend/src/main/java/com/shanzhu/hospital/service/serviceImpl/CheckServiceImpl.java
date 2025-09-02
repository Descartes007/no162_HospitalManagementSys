package com.shanzhu.hospital.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.hospital.entity.po.Checks;
import com.shanzhu.hospital.entity.vo.ChecksPageVo;
import com.shanzhu.hospital.mapper.CheckMapper;
import com.shanzhu.hospital.service.CheckService;
import org.springframework.stereotype.Service;

/**
 * 检查项目 服务层
 *
 * @author: ShanZhu
 * @date: 2023-11-15
 */
@Service("CheckService")
public class CheckServiceImpl extends ServiceImpl<CheckMapper, Checks> implements CheckService {

    /**
     * 查询检查项目 分页
     *
     * @param pageNum  分页页面
     * @param pageSize 分页大小
     * @param query    查询条件
     * @return 检查项目
     */
    @Override
    public ChecksPageVo findChecksPage(Integer pageNum, Integer pageSize, String query) {
        //分页条件
        Page<Checks> page = new Page<>(pageNum, pageSize);

        //查询条件
        LambdaQueryWrapper<Checks> lambdaQuery = Wrappers.<Checks>lambdaQuery()
                .like(Checks::getChName, query);

        //分页查询
        IPage<Checks> iPage = this.page(page, lambdaQuery);

        //组装结果
        ChecksPageVo pageVo = new ChecksPageVo();
        pageVo.populatePage(iPage);

        return pageVo;
    }

    /**
     * 通过id查询检查项目
     *
     * @param chId 检查项目id
     * @return 检查项目
     */
    @Override
    public Checks findCheck(Integer chId) {
        return this.getById(chId);
    }

    /**
     * 添加检查项目
     *
     * @param checks 检查项目
     * @return 结果
     */
    @Override
    public Boolean addCheck(Checks checks) {
        //查询是否存在检测项目
        Checks existChecks = this.getById(checks.getChId());

        //已经存在检查项目，返回false
        if (existChecks != null) {
            return Boolean.FALSE;
        }

        return this.save(checks);
    }

    /**
     * 删除检查项目
     *
     * @param chId 检查项目id
     * @return 结果
     */
    @Override
    public Boolean deleteChecks(Integer chId) {
        return this.removeById(chId);
    }

    /**
     * 更新检查项目
     *
     * @param checks 检查项目信息
     * @return 结果
     */
    @Override
    public Boolean updateChecks(Checks checks) {
        return this.updateById(checks);
    }

}
