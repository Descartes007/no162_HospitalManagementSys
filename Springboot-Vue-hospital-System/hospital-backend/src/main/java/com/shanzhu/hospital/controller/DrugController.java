package com.shanzhu.hospital.controller;

import com.shanzhu.hospital.common.R;
import com.shanzhu.hospital.entity.po.Drug;
import com.shanzhu.hospital.entity.vo.DrugPageVo;
import com.shanzhu.hospital.service.DrugService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 药物 控制层
 *
 
 * @date: 2023-11-17
 */
@RestController
@RequestMapping("drug")
@RequiredArgsConstructor
public class DrugController {

    private final DrugService drugService;

    /**
     * 查询药物 - 分页
     *
     * @param pageNum  分页页面
     * @param pageSize 分页大小
     * @param drName   药物名
     * @return 药物
     */
    @RequestMapping("findAllDrugs")
    public R<DrugPageVo> findDrugsPage(
            @RequestParam(value = "pageNumber") Integer pageNum,
            @RequestParam(value = "size") Integer pageSize,
            @RequestParam(value = "query") String drName
    ) {
        return R.ok(drugService.findDrugsPage(pageNum, pageSize, drName));
    }

    /**
     * 查询药物
     *
     * @param drId 药物id
     * @return 药物信息
     */
    @RequestMapping("findDrug")
    public R<Drug> findDrug(int drId) {
        return R.ok(drugService.findDrug(drId));
    }

    /**
     * 减少药物数量
     *
     * @param drId       药物id
     * @param usedNumber 数量
     * @return 结果
     */
    @RequestMapping("reduceDrugNumber")
    public R<Boolean> reduceDrugNumber(Integer drId, Integer usedNumber) {
        if (BooleanUtils.isTrue(drugService.reduceDrugNumber(drId, usedNumber))) {
            return R.ok("删除药物数量成功");
        }

        return R.error("删除药物数量失败");
    }

    /**
     * 添加药物
     *
     * @param drug 药物信息
     * @return 结果
     */
    @RequestMapping("addDrug")
    public R<Boolean> addDrug(Drug drug) {
        if (BooleanUtils.isTrue(drugService.addDrug(drug))) {
            return R.ok("增加药物成功");
        }

        return R.error("增加药物失败！账号或已被占用");
    }

    /**
     * 删除药物
     *
     * @param drId 药物id
     * @return 结果
     */
    @RequestMapping("deleteDrug")
    public R<Boolean> deleteDrug(@RequestParam(value = "drId") Integer drId) {
        if (BooleanUtils.isTrue(drugService.deleteDrug(drId))) {
            return R.ok("删除药物成功");
        }

        return R.error("删除药物失败");
    }

    /**
     * 修改药物
     *
     * @param drug 药物信息
     * @return 结果
     */
    @RequestMapping("modifyDrug")
    public R<Boolean> modifyDrug(Drug drug) {
        if (BooleanUtils.isTrue(drugService.modifyDrug(drug))) {
            return R.ok("修改药物成功");
        }

        return R.ok("修改药物失败");
    }
}
