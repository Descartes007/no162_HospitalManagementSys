package com.shanzhu.hospital.service.serviceImpl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.hospital.entity.po.Bed;
import com.shanzhu.hospital.entity.vo.BedPageVo;
import com.shanzhu.hospital.mapper.BedMapper;
import com.shanzhu.hospital.service.BedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 病床 服务层
 *
 * @author: ShanZhu
 * @date: 2023-11-15
 */
@Service("BedService")
@RequiredArgsConstructor
public class BedServiceImpl extends ServiceImpl<BedMapper, Bed> implements BedService {

    /**
     * 查询空床位
     *
     * @return 床位
     */
    @Override
    public List<Bed> findEmptyBed() {
        return lambdaQuery().eq(Bed::getBState, 0).list();
    }

    /**
     * 根据病患id查询床位
     *
     * @param pId 病患id
     * @return 床位
     */
    public List<Bed> findBedByPid(Integer pId) {
        return lambdaQuery().eq(Bed::getPId, pId).list();
    }

    /**
     * 查询床位 - 分页
     *
     * @param pageNum  分页页数
     * @param pageSize 分页大小
     * @param query    查询条件
     * @return 床位数据
     */
    @Override
    public BedPageVo findAllBeds(Integer pageNum, Integer pageSize, String query) {
        //分页条件
        Page<Bed> page = new Page<>(pageNum, pageSize);

        //查询条件
        LambdaQueryWrapper<Bed> lambdaQuery = Wrappers.<Bed>lambdaQuery().like(Bed::getPId, query);

        //分页查询
        IPage<Bed> iPage = this.page(page, lambdaQuery);

        //组装结果
        BedPageVo pageVo = new BedPageVo();
        pageVo.populatePage(iPage);

        return pageVo;
    }

    /**
     * 通过床位id 查询床位
     *
     * @param bId 床位id
     * @return 床位信息
     */
    @Override
    public Bed findBed(Integer bId) {
        return this.getById(bId);
    }

    /**
     * 添加床位
     *
     * @param bed 床位信息
     * @return 结果
     */
    @Override
    public Boolean addBed(Bed bed) {
        //查询床位是否存在
        Bed existBed = this.getById(bed.getBId());

        //床位存在，返回false
        if (existBed != null) {
            return Boolean.FALSE;
        }

        bed.setBState(0);

        //保存床位
        return this.save(bed);
    }

    /**
     * 更新床位
     *
     * @param bed 床位信息
     * @return 结果
     */
    @Override
    public Boolean updateBed(Bed bed) {
        //查询已存在床位
        Bed existBed = this.getById(bed.getBId());

        //添加的床位已存在
        if (existBed != null && existBed.getBState().equals(1)) {
            return false;
        }

        //更新床位信息
        bed.setBStart(DateUtil.today());
        bed.setBState(1);
        bed.setVersion(existBed.getVersion());

        //更新
        return this.updateById(bed);
    }

    /**
     * 删除床位
     *
     * @param bId 床位id
     * @return 结果
     */
    @Override
    public Boolean deleteBed(Integer bId) {
        return this.removeById(bId);
    }

    /**
     * 清空床位
     *
     * @param bId 床位id
     * @return 结果
     */
    public Boolean clearBed(int bId) {
        return lambdaUpdate()
                .set(Bed::getPId, -1)
                .set(Bed::getDId, -1)
                .set(Bed::getBReason, null)
                .set(Bed::getBStart, null)
                .set(Bed::getBState, 0)
                .eq(Bed::getBId, bId)
                .update();
    }

    /**
     * 统计今天挂号人数
     *
     * @return 挂号人数
     */
    @Override
    public Integer countBed(String bStart) {
        return lambdaQuery().eq(Bed::getBStart, bStart).count();
    }

}
