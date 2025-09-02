package com.shanzhu.hospital.controller;

import cn.hutool.core.date.DateUtil;
import com.shanzhu.hospital.common.R;
import com.shanzhu.hospital.entity.po.Orders;
import com.shanzhu.hospital.entity.vo.OrderArrangeVo;
import com.shanzhu.hospital.entity.vo.OrdersPageVo;
import com.shanzhu.hospital.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 挂号 控制层
 *
 
 * @date: 2023-11-17
 */
@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 更新挂号单
     *
     * @param orders 挂号单信息
     * @return 结果
     */
    @PostMapping("updateOrder")
    public R<Boolean> updateOrder(@RequestBody Orders orders) {
        if (BooleanUtils.isTrue(orderService.updateOrder(orders))) {
            return R.ok("更新挂号成功");
        }

        return R.error("更新挂号失败！");
    }

    /**
     * 缴费
     *
     * @param oId 挂号单id
     * @return 结果
     */
    @RequestMapping("updatePrice")
    public R<Boolean> updatePrice(Integer oId) {
        if (BooleanUtils.isTrue(orderService.updatePrice(oId))) {
            return R.ok("缴费成功");
        }

        return R.ok("缴费失败");
    }

    /**
     * 查询已完结挂号单
     *
     * @param pageNum  分页页数
     * @param pageSize 分页大小
     * @param pid      病患id
     * @param dId      医生id
     * @return 挂号单
     */
    @RequestMapping("findOrderFinish")
    public R<OrdersPageVo> findOrderFinish(
            @RequestParam(value = "pageNumber") Integer pageNum,
            @RequestParam(value = "size") Integer pageSize,
            @RequestParam(value = "query") String pid,
            @RequestParam(value = "dId") Integer dId
    ) {
        return R.ok(orderService.findOrderFinish(pageNum, pageSize, pid, dId));
    }

    /**
     * 根据dId查询挂号
     *
     * @param pageNum  分页页数
     * @param pageSize 分页大小
     * @param pId      病患id
     * @param dId      医生id
     * @return 挂号单
     */
    @RequestMapping("findOrderByDid")
    public R<OrdersPageVo> findOrderByDid(
            @RequestParam(value = "pageNumber") Integer pageNum,
            @RequestParam(value = "size") Integer pageSize,
            @RequestParam(value = "query") String pId,
            @RequestParam(value = "dId") Integer dId
    ) {
        return R.ok(orderService.findOrderByDid(pageNum, pageSize, pId, dId));
    }

    /**
     * 统计今天挂号人数
     *
     * @return 人数
     */
    @RequestMapping("orderPeople")
    public R<Integer> countOrderPeople() {
        return R.ok(orderService.countOrderPeople(DateUtil.today()));
    }

    /**
     * 统计今天某个医生挂号人数
     *
     * @param dId 医生id
     * @return 人数
     */
    @RequestMapping("orderPeopleByDid")
    public R<Integer> countOrderPeopleByDid(Integer dId) {
        return R.ok(orderService.countOrderPeopleByDid(DateUtil.today(), dId));
    }

    /**
     * 统计挂号男女人数
     *
     * @return 人数
     */
    @RequestMapping("orderGender")
    public R<List<String>> countOrderGender() {
        return R.ok(orderService.countOrderGender());
    }

    /**
     * 增加诊断及医生意见
     *
     * @param order 挂号单信息
     * @return 结果
     */
    @PostMapping("updateOrderByAdd")
    public R<Boolean> updateOrderByAdd(@RequestBody Orders order) {
        if (orderService.updateOrderByAdd(order)) {
            return R.ok("增加诊断及医生意见成功");
        }

        return R.error("增加诊断及医生意见失败");
    }

    /**
     * 判断诊断后购买药物是否已经缴费
     *
     * @param oId 挂号单id
     * @return 结果
     */
    @RequestMapping("findTotalPrice")
    public R<Boolean> findTotalPrice(Integer oId) {
        if (BooleanUtils.isTrue(orderService.findTotalPrice(oId))) {
            return R.ok("未缴费");
        }

        return R.error("无需缴费");
    }

    /**
     * 获取挂号时间段
     *
     * @param arId 排班id
     * @return 时间段
     */
    @RequestMapping("findOrderTime")
    public R<OrderArrangeVo> findOrderTime(String arId) {
        return R.ok(orderService.findOrderTime(arId));
    }

    /**
     * 统计近20天科室人数
     *
     * @return 人数
     */
    @RequestMapping("orderSection")
    public R<List<String>> countOrderSection() {
        return R.ok(orderService.countOrderSection());
    }
}
