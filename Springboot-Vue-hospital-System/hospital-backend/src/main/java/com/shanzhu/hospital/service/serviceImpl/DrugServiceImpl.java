package com.shanzhu.hospital.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.hospital.entity.po.Drug;
import com.shanzhu.hospital.entity.vo.DrugPageVo;
import com.shanzhu.hospital.mapper.DrugMapper;
import com.shanzhu.hospital.service.DrugService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 药物 服务层
 *
 * @author: ShanZhu
 * @date: 2023-11-17
 */
@Service("DrugService")
@RequiredArgsConstructor
public class DrugServiceImpl extends ServiceImpl<DrugMapper, Drug> implements DrugService {

    private final DrugMapper drugMapper;

    /**
     * 查询药物 - 分页
     *
     * @param pageNum  分页页面
     * @param pageSize 分页大小
     * @param drName   药物名
     * @return 药物
     */
    @Override
    public DrugPageVo findDrugsPage(Integer pageNum, Integer pageSize, String drName) {
        //分页条件
        Page<Drug> page = new Page<>(pageNum, pageSize);

        //查询条件
        LambdaQueryWrapper<Drug> lambdaQuery = Wrappers.<Drug>lambdaQuery()
                .like(Drug::getDrName, drName);

        //分页查询
        IPage<Drug> iPage = this.drugMapper.selectPage(page, lambdaQuery);

        //组装结果
        DrugPageVo pageVo = new DrugPageVo();
        pageVo.populatePage(iPage);

        return pageVo;
    }

    /**
     * 查询药物
     *
     * @param drId 药物id
     * @return 药物信息
     */
    @Override
    public Drug findDrug(Integer drId) {
        return this.getById(drId);
    }

    /**
     * 减少药物数量
     *
     * @param drId       药物id
     * @param usedNumber 数量
     * @return 结果
     */
    @Override
    public Boolean reduceDrugNumber(Integer drId, Integer usedNumber) {
        //查询药物
        Drug drug = this.getById(drId);

        //数量少于使用量
        if (drug.getDrNumber() < usedNumber) {
            return Boolean.FALSE;
        }

        //减少数量
        drug.setDrNumber(drug.getDrNumber() - usedNumber);

        //更新
        return this.updateById(drug);
    }

    /**
     * 添加药物
     *
     * @param drug 药物信息
     * @return 结果
     */
    public Boolean addDrug(Drug drug) {
        //查询药物是否已存在
        Drug existDrug = this.getById(drug.getDrId());

        //如果已存在则返回false
        if (existDrug != null) {
            return Boolean.FALSE;
        }

        //保存药物
        return this.save(drug);
    }

    /**
     * 删除药物
     *
     * @param drId 药物id
     * @return 结果
     */
    @Override
    public Boolean deleteDrug(Integer drId) {
        return this.removeById(drId);
    }

    /**
     * 修改药物
     *
     * @param drug 药物信息
     * @return 结果
     */
    @Override
    public Boolean modifyDrug(Drug drug) {
        return this.updateById(drug);
    }
}
