package com.bjpowernode.service.imp;

import com.bjpowernode.mapper.ProductInfoMapper;
import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.ProductInfoExample;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.bjpowernode.service.ProductInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductInfoServiceimp implements ProductInfoService {
    @Autowired
    ProductInfoMapper productInfoMapper;
    @Override
    public List<ProductInfo> getall() {
       return productInfoMapper.selectByExample(new ProductInfoExample());
    }
    @Override
    public PageInfo<ProductInfo> splitpage(int page, int pageSize) {
        //商品分页一定会借助于PageHelper类，还要借助于ProductInfoExample
        ProductInfoExample example=new ProductInfoExample();
        //设置的字符串是字段名称和排序规则
        example.setOrderByClause("p_id desc");
        //切记切记：在取集合之前，使用分页工具设置当前页和每页的记录数
        PageHelper.startPage(page,pageSize);
        //取集合
        List<ProductInfo> list=productInfoMapper.selectByExample(example);

        //将查到的集合封装进pageInfo
        PageInfo<ProductInfo> pageInfo=new PageInfo<>(list);

        return pageInfo;
    }

    @Override
    public int save(ProductInfo productInfo) {
        return productInfoMapper.insert(productInfo);
    }

    @Override
    public ProductInfo getone(int pid) {
        return productInfoMapper.selectByPrimaryKey(pid);
    }

    @Override
    public int update(ProductInfo productInfo) {
        return productInfoMapper.updateByPrimaryKey(productInfo);
    }

    @Override
    public int deleteBatch(String[] ids) {
        return productInfoMapper.deleteBatch(ids);
    }

    @Override
    public List<ProductInfo> selectCondition(ProductInfoVo vo) {
        return productInfoMapper.selectCondition(vo);
    }

    @Override
    public PageInfo<ProductInfo> splitPageVo(ProductInfoVo vo, int pageSize) {
        //取出集合之前,先要设置PageHelper.startPage()属性
        PageHelper.startPage(vo.getPage(),pageSize);
        List<ProductInfo> list = productInfoMapper.selectCondition(vo);
        return new PageInfo<>(list);
    }
    @Override
    public int delete(int pid) {
        return productInfoMapper.deleteByPrimaryKey(pid);
    }
}
