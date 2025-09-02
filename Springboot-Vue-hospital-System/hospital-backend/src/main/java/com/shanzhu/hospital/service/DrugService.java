package com.shanzhu.hospital.service;

import com.shanzhu.hospital.entity.po.Drug;
import com.shanzhu.hospital.entity.vo.DrugPageVo;

/**
 * 药物 服务层
 *
 
 * @date: 2023-11-17
 */
public interface DrugService {

    /**
     * 查询药物 - 分页
     *
     * @param pageNum  分页页面
     * @param pageSize 分页大小
     * @param drName   药物名
     * @return 药物
     */
    DrugPageVo findDrugsPage(Integer pageNum, Integer pageSize, String drName);

    /**
     * 查询药物
     *
     * @param drId 药物id
     * @return 药物信息
     */
    Drug findDrug(Integer drId);

    /**
     * 减少药物数量
     *
     * @param drId 药物id
     * @param usedNumber 数量
     * @return 结果
     */
    Boolean reduceDrugNumber(Integer drId, Integer usedNumber);

    /**
     * 添加药物
     *
     * @param drug 药物信息
     * @return 结果
     */
    Boolean addDrug(Drug drug);

    /**
     * 删除药物
     *
     * @param drId 药物id
     * @return 结果
     */
    Boolean deleteDrug(Integer drId);

    /**
     * 修改药物
     *
     * @param drug 药物信息
     * @return 结果
     */
    Boolean modifyDrug(Drug drug);

}
