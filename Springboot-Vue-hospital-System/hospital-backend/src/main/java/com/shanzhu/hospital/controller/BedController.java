package com.shanzhu.hospital.controller;

import cn.hutool.core.date.DateUtil;
import com.shanzhu.hospital.common.R;
import com.shanzhu.hospital.entity.po.Bed;
import com.shanzhu.hospital.entity.vo.BedPageVo;
import com.shanzhu.hospital.service.BedService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 病床 控制层
 *
 
 * @date: 2023-11-15
 */
@RestController
@RequestMapping("bed")
@RequiredArgsConstructor
public class BedController {

    private final BedService bedService;

    /**
     * 查询空床位
     *
     * @return 床位
     */
    @RequestMapping("findNullBed")
    public R<List<Bed>> findEmptyBed() {
        return R.ok(bedService.findEmptyBed());
    }


    /**
     * 根据病患id查询床位
     *
     * @param pId 病患id
     * @return 床位
     */
    @RequestMapping("findBedByPid")
    public R<Bed> findBedByPid(@RequestParam(value = "pId") int pId) {
        return R.ok(bedService.findBedByPid(pId));
    }

    /**
     * 查询床位 - 分页
     *
     * @param pageNum  分页页数
     * @param pageSize 分页大小
     * @param query    查询条件
     * @return 床位数据
     */
    @RequestMapping("findAllBeds")
    public R<BedPageVo> findAllBeds(
            @RequestParam(value = "pageNumber") Integer pageNum,
            @RequestParam(value = "size") Integer pageSize,
            @RequestParam(value = "query") String query

    ) {
        return R.ok(this.bedService.findAllBeds(pageNum, pageSize, query));
    }

    /**
     * 通过床位id 查询床位
     *
     * @param bId 床位id
     * @return 床位信息
     */
    @RequestMapping("findBed")
    public R<Bed> findBed(Integer bId) {
        return R.ok(bedService.findBed(bId));
    }

    /**
     * 添加床位
     *
     * @param bed 床位信息
     * @return 结果
     */
    @RequestMapping("addBed")
    public R<Boolean> addBed(Bed bed) {
        if (BooleanUtils.isTrue(bedService.addBed(bed))) {
            return R.ok("增加床位成功");
        }

        return R.error("增加床位失败！床号或已被占用");
    }

    /**
     * 更新床位
     *
     * @param bed 床位信息
     * @return 结果
     */
    @RequestMapping("updateBed")
    public R<Boolean> updateBed(Bed bed) {
        if (BooleanUtils.isTrue(bedService.updateBed(bed))) {
            return R.ok("增加床位成功");
        }

        return R.error("增加床位失败");
    }

    /**
     * 删除床位
     *
     * @param bId 床位id
     * @return 结果
     */
    @RequestMapping("deleteBed")
    public R<Boolean> deleteBed(@RequestParam(value = "bId") int bId) {
        if (BooleanUtils.isTrue(bedService.deleteBed(bId))) {
            return R.ok("删除床位成功");
        }

        return R.error("删除床位失败");
    }

    /**
     * 清空床位
     *
     * @param bId 床位id
     * @return 结果
     */
    @RequestMapping("emptyBed")
    public R<Boolean> clearBed(Integer bId) {
        if (BooleanUtils.isTrue(bedService.clearBed(bId))) {
            return R.ok("清空床位成功");
        }

        return R.error("清空床位失败");
    }

    /**
     * 统计今天挂号人数
     *
     * @return 挂号人数
     */
    @RequestMapping("bedPeople")
    public R<Integer> countBed() {
        return R.ok(bedService.countBed(DateUtil.today()));
    }
}
