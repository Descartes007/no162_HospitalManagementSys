package com.shanzhu.hospital.service;

import com.shanzhu.hospital.entity.po.Bed;
import com.shanzhu.hospital.entity.vo.BedPageVo;

import java.util.List;

/**
 * 病床 服务层
 *
 
 * @date: 2023-11-15
 */
public interface BedService {

    /**
     * 查询空床位
     *
     * @return 床位
     */
    List<Bed> findEmptyBed();


    /**
     * 根据病患id查询床位
     *
     * @param pId 病患id
     * @return 床位
     */
    List<Bed> findBedByPid(Integer pId);

    /**
     * 查询床位 - 分页
     *
     * @param pageNum 分页页数
     * @param pageSize 分页大小
     * @param query 查询条件
     * @return 床位数据
     */
    BedPageVo findAllBeds(Integer pageNum, Integer pageSize, String query);

    /**
     * 通过床位id 查询床位
     *
     * @param bId 床位id
     * @return 床位信息
     */
    Bed findBed(Integer bId);

    /**
     * 添加床位
     *
     * @param bed 床位信息
     * @return 结果
     */
    Boolean addBed(Bed bed);

    /**
     * 更新 床位
     *
     * @param bed 床位信息
     * @return 结果
     */
    Boolean updateBed(Bed bed);

    /**
     * 删除床位
     *
     * @param bId 床位id
     * @return 结果
     */
    Boolean deleteBed(Integer bId);

    /**
     * 清空床位
     *
     * @param bId 床位id
     * @return 结果
     */
    Boolean clearBed(int bId);

    /**
     * 统计今天挂号人数
     *
     * @return 挂号人数
     */
    Integer countBed(String bStart);

}
