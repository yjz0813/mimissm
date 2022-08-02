package com.bjpowernode.service;

import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductInfoService {
    //获取连接得到全部商品
    List<ProductInfo> getall();
    //分页功能的设置
    PageInfo<ProductInfo> splitpage(int pagenum,int pagesize);
    //保存数据
    int save(ProductInfo productInfo);
    //查找主键
    ProductInfo getone(int pid);
    //更新
    int update(ProductInfo productInfo);
    //单个商品删除
    int delete(int pid);
    //批量删除商品
    int deleteBatch(String []ids);

    //多条件商品查询
    List<ProductInfo> selectCondition(ProductInfoVo vo);

    //多条件查询分页
    public PageInfo splitPageVo(ProductInfoVo vo, int pageSize);
}
