package com.shanzhu.hospital.controller;

import com.shanzhu.hospital.common.R;
import com.shanzhu.hospital.entity.po.Checks;
import com.shanzhu.hospital.entity.vo.ChecksPageVo;
import com.shanzhu.hospital.service.CheckService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 检查项目 控制层
 *
 
 * @date: 2023-11-15
 */
@RestController
@RequestMapping("check")
@RequiredArgsConstructor
public class CheckController {

    private final CheckService checkService;

    /**
     * 查询检查项目 分页
     *
     * @param pageNum  分页页面
     * @param pageSize 分页大小
     * @param query    查询条件
     * @return 检查项目
     */
    @RequestMapping("findAllChecks")
    public R<ChecksPageVo> findChecksPage(
            @RequestParam(value = "pageNumber") Integer pageNum,
            @RequestParam(value = "size") Integer pageSize,
            @RequestParam(value = "query") String query

    ) {
        return R.ok(checkService.findChecksPage(pageNum, pageSize, query));
    }

    /**
     * 通过id查询检查项目
     *
     * @param chId 检查项目id
     * @return 检查项目
     */
    @RequestMapping("findCheck")
    public R<Checks> findCheck(Integer chId) {
        return R.ok(checkService.findCheck(chId));
    }

    /**
     * 添加检查项目
     *
     * @param checks 检查项目
     * @return 结果
     */
    @RequestMapping("addCheck")
    public R<Boolean> addCheck(Checks checks) {
        if (BooleanUtils.isTrue(checkService.addCheck(checks))) {
            return R.ok("增加检查项成功");
        }

        return R.error("增加检查项失败！账号或已被占用");
    }

    /**
     * 更新检查项目
     *
     * @param checks 检查项目信息
     * @return 结果
     */
    @RequestMapping("modifyCheck")
    public R<Boolean> updateCheck(Checks checks) {
        if (BooleanUtils.isTrue(checkService.updateChecks(checks))) {
            return R.ok("修改检查项目成功");
        }

        return R.error("修改检查项目失败");
    }

    /**
     * 删除检查项目
     *
     * @param chId 检查项目id
     * @return 结果
     */
    @RequestMapping("deleteCheck")
    public R<Boolean> deleteCheck(@RequestParam(value = "chId") Integer chId) {
        if (BooleanUtils.isTrue(checkService.deleteChecks(chId))) {
            return R.ok("删除检查项目成功");
        }

        return R.error("删除检查项目失败");
    }

}
