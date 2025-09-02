package com.shanzhu.hospital.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.hospital.entity.po.Arrange;
import com.shanzhu.hospital.mapper.ArrangeMapper;
import com.shanzhu.hospital.service.ArrangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 排班 服务层
 *
 * @author: ShanZhu
 * @date: 2023-11-17
 */
@Slf4j
@Service("ArrangeService")
@RequiredArgsConstructor
public class ArrangeServiceImpl extends ServiceImpl<ArrangeMapper, Arrange> implements ArrangeService {

    private final ArrangeMapper arrangeMapper;

    /**
     * 通过日期查询排班
     *
     * @param arTime   排班时间
     * @param dSection 科室
     * @return 排班信息
     */
    @Override
    public List<Arrange> findArrange(String arTime, String dSection) {

        return arrangeMapper.findByTime(arTime, dSection);
    }

    /**
     * 添加排班
     *
     * @param arrange 排班信息
     * @return 结果
     */
    public Boolean addArrange(Arrange arrange) {
        //查询是否已存在排班
        Arrange existArrange = this.getById(arrange.getArId());

        //已存在排班
        if (existArrange != null) {
            return Boolean.FALSE;
        }

        //入库排班
        return this.save(arrange);
    }

    /**
     * 删除排班
     *
     * @param arId 排班id
     * @return 结果
     */
    public Boolean deleteArrange(String arId) {
        //查询已存在排班
        Arrange existArrange = this.getById(arId);

        //排班不存在
        if (existArrange == null) {
            return Boolean.FALSE;
        }

        //删除排班
        return this.removeById(arId);
    }

}
