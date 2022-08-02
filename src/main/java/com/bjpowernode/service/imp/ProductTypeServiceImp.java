package com.bjpowernode.service.imp;
import com.bjpowernode.mapper.ProductTypeMapper;
import com.bjpowernode.pojo.ProductType;
import com.bjpowernode.pojo.ProductTypeExample;
import com.bjpowernode.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ProductTypeServiceImp")
public class ProductTypeServiceImp implements ProductTypeService {
    @Autowired
    ProductTypeMapper productTypeMapper;
    @Override
    public List<ProductType> getall() {
        List<ProductType> list=productTypeMapper.selectByExample(new ProductTypeExample());
        return list;
    }
}
