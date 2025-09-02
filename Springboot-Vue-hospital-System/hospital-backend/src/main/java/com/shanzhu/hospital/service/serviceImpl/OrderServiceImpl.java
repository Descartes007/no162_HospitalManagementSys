package com.shanzhu.hospital.service.serviceImpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.hospital.entity.po.Arrange;
import com.shanzhu.hospital.entity.po.Orders;
import com.shanzhu.hospital.entity.vo.OrderArrangeVo;
import com.shanzhu.hospital.entity.vo.OrdersPageVo;
import com.shanzhu.hospital.mapper.ArrangeMapper;
import com.shanzhu.hospital.mapper.OrderMapper;
import com.shanzhu.hospital.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 挂号相关 服务层
 *
 * @author: ShanZhu
 * @date: 2023-11-15
 */
@Service("OrderService")
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    private final OrderMapper orderMapper;

    private final ArrangeMapper arrangeMapper;

    /**
     * 查询挂号信息 - 分页
     *
     * @param pageNum  分页页数
     * @param pageSize 分页大小
     * @param query    查询条件
     * @return 挂号列表
     */
    @Override
    public OrdersPageVo findOrdersPages(Integer pageNum, Integer pageSize, String query) {
        //分页条件
        Page<Orders> page = new Page<>(pageNum, pageSize);

        //查询条件
        LambdaQueryWrapper<Orders> lambdaQuery = Wrappers.<Orders>lambdaQuery()
                .like(Orders::getPId, query);

        //分页查询
        IPage<Orders> iPage = this.page(page, lambdaQuery);

        //组装分页结果
        OrdersPageVo pageVo = new OrdersPageVo();
        pageVo.populatePage(iPage);

        return pageVo;
    }

    /**
     * 删除挂号单
     *
     * @param oId 挂号单id
     * @return 结果
     */
    @Override
    public Boolean deleteOrder(Integer oId) {
        return this.removeById(oId);
    }

    /**
     * 添加挂号单
     *
     * @param order 挂号单信息
     * @param arId  排班id
     * @return 结果
     */
    @Override
    public Boolean addOrder(Orders order, String arId) {
        String oStart = order.getOStart().substring(0, 22);
        //查询患者当前时间段是否有未诊断的挂号单
        List<Orders> existOrders = lambdaQuery()
                //患者id
                .eq(Orders::getPId, order.getPId())
                //挂号时间段
                .eq(Orders::getOStart, oStart)
                //未诊断
                .eq(Orders::getOState, 0)
                .list();

        //存在未诊断的挂号单
        if(CollUtil.isNotEmpty(existOrders)){
            return Boolean.FALSE;
        }

        //挂号单信息
        order.setOId(RandomUtil.randomInt(1000, 9999));
        order.setOState(0);
        order.setOPriceState(0);
        order.setOStart(oStart);

        //保存挂号单
        return this.save(order);

    }

    /**
     * 查询病患挂号
     *
     * @param pId 病患id
     * @return 挂号信息
     */
    @Override
    public List<Orders> findOrderByPid(Integer pId) {
        return orderMapper.findOrderByPid(pId);
    }

    /**
     * 查询挂号
     *
     * @param oId 挂号单id
     * @return 挂号信息
     */
    @Override
    public Orders findOrderByOid(Integer oId) {
        return orderMapper.findOrderByOid(oId);
    }

    /**
     * 查看当天挂号
     *
     * @param dId    医生id
     * @param oStart 日期时间
     * @return 挂号数据
     */
    @Override
    public List<Orders> findOrderByNull(Integer dId, String oStart) {
        return orderMapper.findOrderByNull(dId, oStart);
    }

    /**
     * 更新挂号单
     *
     * @param orders 挂号单信息
     * @return 结果
     */
    @Override
    public Boolean updateOrder(Orders orders) {
        //设置挂号单状态
        orders.setOState(1);

        //设置挂号单结束时间
        orders.setOEnd(DateUtil.now());

        //更新
        return this.updateById(orders);
    }

    /**
     * 缴费
     *
     * @param oId 挂号单id
     * @return 结果
     */
    @Override
    public Boolean updatePrice(Integer oId) {
        return lambdaUpdate()
                //更新缴费状态1
                .set(Orders::getOPriceState, 1)
                //更新费用0
                .set(Orders::getOTotalPrice, 0.00)
                .update();
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
    @Override
    public OrdersPageVo findOrderFinish(
            Integer pageNum, Integer pageSize, String pid, Integer dId
    ) {
        //分页条件
        Page<Orders> page = new Page<>(pageNum, pageSize);

        //查询条件
        LambdaQueryWrapper<Orders> lambdaQuery = Wrappers.<Orders>lambdaQuery()
                //模糊匹配病患id
                .like(Orders::getPId, pid)
                //医生id
                .eq(Orders::getDId, dId)
                //状态1
                .eq(Orders::getOState, 1)
                //按状态降序
                .orderByDesc(Orders::getOState);

        //分页查询
        IPage<Orders> iPage = this.page(page, lambdaQuery);

        //组装结果
        OrdersPageVo pageVo = new OrdersPageVo();
        pageVo.populatePage(iPage);

        return pageVo;
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
    public OrdersPageVo findOrderByDid(
            Integer pageNum, Integer pageSize, String pId, Integer dId
    ) {
        //分页条件
        Page<Orders> page = new Page<>(pageNum, pageSize);

        //查询条件
        LambdaQueryWrapper<Orders> lambdaQuery = Wrappers.<Orders>lambdaQuery()
                //模糊匹配病患id
                .like(Orders::getPId, pId)
                //医生id
                .eq(Orders::getDId, dId)
                //按状态降序
                .orderByDesc(Orders::getOState);

        //分页查询
        IPage<Orders> iPage = this.page(page, lambdaQuery);

        //组装结果
        OrdersPageVo pageVo = new OrdersPageVo();
        pageVo.populatePage(iPage);

        return pageVo;
    }

    /**
     * 统计挂号人数
     *
     * @return 人数
     * @Param oStart 时间
     */
    @Override
    public Integer countOrderPeople(String oStart) {
        return orderMapper.orderPeople(oStart);
    }

    /**
     * 统计今天某个医生挂号人数
     *
     * @param oStart 时间
     * @param dId    医生id
     * @return 人数
     */
    @Override
    public Integer countOrderPeopleByDid(String oStart, Integer dId) {
        return orderMapper.orderPeopleByDid(oStart, dId);
    }

    /**
     * 统计挂号男女人数
     *
     * @return 人数
     */
    public List<String> countOrderGender() {
        return orderMapper.orderGender();
    }

    /**
     * 增加诊断及医生意见
     *
     * @param order 挂号单信息
     * @return 结果
     */
    public Boolean updateOrderByAdd(Orders order) {
        if (orderMapper.updateOrderByAdd(order) == 0) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    /**
     * 判断诊断后购买药物是否已经缴费
     *
     * @param oId 挂号单id
     * @return 结果
     */
    public Boolean findTotalPrice(int oId) {
        Orders order = this.getById(oId);
        if (order.getOTotalPrice() != 0.00) {
            order.setOPriceState(0);

            this.updateById(order);
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    /**
     * 获取挂号时间段
     *
     * @param arId 排班id
     * @return 时间段
     */
    @Override
    public OrderArrangeVo findOrderTime(String arId) {
        //查询排班信息
        Arrange arrange = arrangeMapper.selectById(arId);

        OrderArrangeVo orderArrangeVo = new OrderArrangeVo();
        orderArrangeVo.setOrderDate(arrange.getArTime());

        return orderArrangeVo;
    }

    /**
     * 统计近20天科室人数
     *
     * @return 人数
     */
    @Override
    public List<String> countOrderSection() {
        //过去20天开始时间
        LocalDate beginDate = LocalDate.now().minusDays(20);
        String startTime = beginDate.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
        String endTime = DateUtil.now();

        return this.orderMapper.orderSection(startTime, endTime);
    }
}
