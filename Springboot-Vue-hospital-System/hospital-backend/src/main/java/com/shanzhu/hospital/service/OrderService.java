package com.shanzhu.hospital.service;

import com.shanzhu.hospital.entity.po.Orders;
import com.shanzhu.hospital.entity.vo.OrderArrangeVo;
import com.shanzhu.hospital.entity.vo.OrdersPageVo;

import java.util.List;

/**
 * 挂号相关 服务层
 *
 
 * @date: 2023-11-15
 */
public interface OrderService {

    /**
     * 查询挂号信息 - 分页
     *
     * @param pageNum  分页页数
     * @param pageSize 分页大小
     * @param query    查询条件
     * @return 挂号列表
     */
    OrdersPageVo findOrdersPages(Integer pageNum, Integer pageSize, String query);

    /**
     * 删除挂号单
     *
     * @param oId 挂号单id
     * @return 结果
     */
    Boolean deleteOrder(Integer oId);

    /**
     * 添加挂号单
     *
     * @param order 挂号单信息
     * @param arId  排班id
     * @return 结果
     */
    Boolean addOrder(Orders order, String arId);

    /**
     * 查询病患挂号
     *
     * @param pId 病患id
     * @return 挂号信息
     */
    List<Orders> findOrderByPid(Integer pId);

    /**
     * 查询挂号
     *
     * @param oId 挂号单id
     * @return 挂号信息
     */
    Orders findOrderByOid(Integer oId);

    /**
     * 查看当天挂号
     *
     * @param dId    医生id
     * @param oStart 日期时间
     * @return 挂号数据
     */
    List<Orders> findOrderByNull(Integer dId, String oStart);

    /**
     * 更新挂号单
     *
     * @param orders 挂号单信息
     * @return 结果
     */
    Boolean updateOrder(Orders orders);

    /**
     * 缴费
     *
     * @param oId 挂号单id
     * @return 结果
     */
    Boolean updatePrice(Integer oId);

    /**
     * 查询已完结挂号单
     *
     * @param pageNum  分页页数
     * @param pageSize 分页大小
     * @param pid      病患id
     * @param dId      医生id
     * @return 挂号单
     */
    OrdersPageVo findOrderFinish(
            Integer pageNum, Integer pageSize, String pid, Integer dId
    );

    /**
     * 根据dId查询挂号
     *
     * @param pageNum  分页页数
     * @param pageSize 分页大小
     * @param pId      病患id
     * @param dId      医生id
     * @return 挂号单
     */
    OrdersPageVo findOrderByDid(Integer pageNum, Integer pageSize, String pId, Integer dId);

    /**
     * 统计挂号人数
     *
     * @Param oStart 时间
     * @return 人数
     */
    Integer countOrderPeople(String oStart);

    /**
     * 统计今天某个医生挂号人数
     *
     * @param oStart 时间
     * @param dId 医生id
     * @return 人数
     */
    Integer countOrderPeopleByDid(String oStart, Integer dId);

    /**
     * 统计挂号男女人数
     *
     * @return 人数
     */
    List<String> countOrderGender();

    /**
     * 增加诊断及医生意见
     *
     * @param order 挂号单信息
     * @return 结果
     */
    Boolean updateOrderByAdd(Orders order);

    /**
     * 判断诊断后购买药物是否已经缴费
     *
     * @param oId 挂号单id
     * @return 结果
     */
    Boolean findTotalPrice(int oId);

    /**
     * 获取挂号时间段
     *
     * @param arId 排班id
     * @return 时间段
     */
    OrderArrangeVo findOrderTime(String arId);

    /**
     * 统计近20天科室人数
     *
     * @return 人数
     */
    List<String> countOrderSection();

}
