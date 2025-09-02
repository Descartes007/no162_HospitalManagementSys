package com.shanzhu.hospital.controller;

import com.shanzhu.hospital.common.R;
import com.shanzhu.hospital.entity.po.Arrange;
import com.shanzhu.hospital.service.ArrangeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 排班相关 控制层
 *
 
 * @date: 2023-11-17
 */
@RestController
@RequestMapping("/arrange")
@RequiredArgsConstructor
public class ArrangeController {

    private final ArrangeService arrangeService;

    /**
     * 通过日期查询排班
     *
     * @param arTime   排班时间
     * @param dSection 科室
     * @return 排班信息
     */
    @RequestMapping("findByTime")
    public R<List<Arrange>> findArrange(
            @RequestParam(value = "arTime") String arTime,
            @RequestParam(value = "dSection") String dSection
    ) {
        return R.ok(arrangeService.findArrange(arTime, dSection));
    }

    /**
     * 添加排班
     *
     * @param arrange 排班信息
     * @return 结果
     */
    @RequestMapping("addArrange")
    public R<Boolean> addArrange(Arrange arrange) {
        if (BooleanUtils.isTrue(arrangeService.addArrange(arrange))) {
            return R.ok("添加排班成功");
        }

        return R.error("该医生该日已排班");
    }

    /**
     * 删除排班
     *
     * @param arId 排班id
     * @return 结果
     */
    @RequestMapping("deleteArrange")
    public R<Boolean> deleteArrange(String arId) {
        if (BooleanUtils.isTrue(arrangeService.deleteArrange(arId))){
            return R.ok("删除排班成功");
        }

        return R.error("排班信息不存在");
    }

}
