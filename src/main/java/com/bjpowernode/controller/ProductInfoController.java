package com.bjpowernode.controller;

import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.bjpowernode.service.ProductInfoService;
import com.bjpowernode.utils.FileNameUtil;
import com.github.pagehelper.PageInfo;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProductInfoController {
    private static final int PAGE_SIZE =5 ;
    @Autowired
   ProductInfoService productInfoService;
    String  saveFileName;
   @RequestMapping("/getall")
   public String getall(HttpServletRequest request){
       List<ProductInfo> list=productInfoService.getall();
       request.setAttribute("info",list);
      return "product";
   }
   @RequestMapping("/split")
    public String split(HttpServletRequest request){
       PageInfo<ProductInfo> info =productInfoService.splitpage(1,5);
       request.setAttribute("info",info);
       return "product";
   }
   @ResponseBody
   @RequestMapping("ajaxsplit")
    public void ajaxsplit(int page, HttpSession httpSession){
       PageInfo pageInfo=productInfoService.splitpage(page,5);
       httpSession.setAttribute("info",pageInfo);
       return ;
   }
    //异步ajax文件上传处理
    @ResponseBody
    @RequestMapping("/ajaxImg")
    public Object ajaxImg(
            MultipartFile pimage, HttpServletRequest request) {
        //提取生成文件名UUID+上传图片的后缀.jpg  .png
        saveFileName = FileNameUtil.getUUIDFileName() + FileNameUtil.getFileType(pimage.getOriginalFilename());
        //得到项目中图片存储的路径
        String path = request.getServletContext().getRealPath("/image_big");
        //转存  E:\idea_workspace\mimissm\image_big\23sdfasferafdafdadfasfdassf.jpg
        try {
            pimage.transferTo(new File(path + File.separator + saveFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回客户端JSON对象,封装图片的路径,为了在页面实现立即回显
        JSONObject object = new JSONObject();
        object.put("imgurl", saveFileName);
        return object.toString();
    }
   @RequestMapping("/save")
    public String save(ProductInfo productInfo,HttpServletRequest httpServletRequest){
       productInfo.setpImage(saveFileName);
       productInfo.setpDate(new Date());
       int num=-1;
       num= productInfoService.save(productInfo);;
       if(num>0){
           httpServletRequest.setAttribute("msg","插入成功");
       }else{
           httpServletRequest.setAttribute("msg","插入失败");
       }
       saveFileName="";
       return "forward:/prod/split.action";
   }
   @RequestMapping("/one")
    public String getone(int pid, Model model){
       ProductInfo productInfo=productInfoService.getone(pid);
       model.addAttribute("prod",productInfo);
       return "update";
   }
    @RequestMapping("/update")
    public String update(ProductInfo info, HttpServletRequest request) {
        //因为ajax的异步图片上传,如果有上传过,
        // 则saveFileName里有上传上来的图片的名称,
        // 如果没有使用异步ajax上传过图片,则saveFileNme="",
        // 实体类info使用隐藏表单域提供上来的pImage原始图片的名称;
        if (!saveFileName.equals("")) {
            info.setpImage(saveFileName);
        }
        //完成更新处理
        int num = -1;
        try {
            num = productInfoService.update(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            //此时说明更新成功
            request.setAttribute("msg", "更新成功!");
        } else {
            //更新失败
            request.setAttribute("msg", "更新失败!");
        }

        //处理完更新后,saveFileName里有可能有数据,
        // 而下一次更新时要使用这个变量做为判断的依据,就会出错,所以必须清空saveFileName.
        saveFileName = "";
        return "forward:/prod/split.action";
    }
    @RequestMapping("/delete")
    public String delete(int pid,ProductInfoVo vo, HttpServletRequest request) {
        int num = -1;
        try {
            num = productInfoService.delete(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (num > 0) {
            request.setAttribute("msg", "删除成功!");
            request.getSession().setAttribute("deleteProdVo",vo);
        } else {
            request.setAttribute("msg", "删除失败!");
        }

        //删除结束后跳到分页显示
        return "forward:/prod/deleteAjaxSplit.action";
    }

    @ResponseBody
    @RequestMapping(value = "/deleteAjaxSplit", produces = "text/html;charset=UTF-8")
    public Object deleteAjaxSplit(HttpServletRequest request) {
        //取得第1页的数据
        PageInfo info = null;
        Object vo = request.getSession().getAttribute("deleteProdVo");
        if(vo != null){
            info = productInfoService.splitPageVo((ProductInfoVo)vo,PAGE_SIZE);
        }else {
            info = productInfoService.splitpage(1,PAGE_SIZE);
        }
        request.getSession().setAttribute("info",info);
        return request.getAttribute("msg");
    }

    //批量删除商品
    @RequestMapping("/deleteBatch")
    //pids="1,4,5"  ps[1,4,5]
    public String deleteBatch(String pids,HttpServletRequest request){
        //将上传上来的字符串截开,形成商品id的字符数组
        String []ps = pids.split(",");
        try {
            int num = productInfoService.deleteBatch(ps);
            if(num > 0 ){
                request.setAttribute("msg","批量删除成功!");
            }else{
                request.setAttribute("msg","批量删除失败!");
            }
        } catch (Exception e) {
            request.setAttribute("msg","商品不可删除!");
        }
        return "forward:/prod/deleteAjaxSplit.action";
    }
}
