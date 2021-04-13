package com.chm.bag_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chm.bag_system.entity.Data;
import com.chm.bag_system.service.DataService;
import com.chm.bag_system.toolClass.HandleData;
import com.chm.bag_system.toolClass.OrderUtil;
import com.chm.bag_system.toolClass.Scatter;
import com.chm.bag_system.toolClass.ScatterUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @program: bag_system
 * @description
 * @author: BeiKe
 * @create: 2021-04-13 19:22
 **/
@Controller
public class OrderController {

    @Resource
    DataService dataService;

    //    存放当前组价值的整型列表
    private static ArrayList<Integer> profitList=new ArrayList<>();

    //    存放当前组重量的整型列表
    private static ArrayList<Integer> weightList=new ArrayList<>();


    @PostMapping("/order/query")
    public String queryData(Scatter scatter, Model model){
        if(scatter.getFileName().equals("")||scatter.getGroup().equals("")){
            model.addAttribute("data",null);
            return "order";
        }
        profitList.clear();
        weightList.clear();
        QueryWrapper<Data> wrapper = new QueryWrapper<Data>();
        wrapper.eq("file",scatter.getFileName());
        wrapper.eq("team",scatter.getGroup());
        List<Data> list = dataService.list(wrapper);
        HandleData handleData = new HandleData();
        for (Data data : list) {
            if (data.getType().equals("价值")) {
                handleData.splitDataIntoInteger(profitList,data.getContent());
            }
            if (data.getType().equals("重量")) {
                handleData.splitDataIntoInteger(weightList,data.getContent());
            }
        }
        ArrayList<ScatterUtil> scatterUtils = new ArrayList<>();
        handleData.splitDataTwoGroup(profitList,weightList,scatterUtils);
//        返回前端的数据列表
        ArrayList<OrderUtil> orderUtils = new ArrayList<>();
        splitThreeGroup(scatterUtils, orderUtils);
        model.addAttribute("data",orderUtils);
        return "order";
    }

    /**
     * 将数据分为三三一组，并排序
     * @param scatterUtils
     * @param orderUtils
     */
    private void splitThreeGroup(ArrayList<ScatterUtil> scatterUtils, ArrayList<OrderUtil> orderUtils) {
        for (int i = 0; i < scatterUtils.size(); i=i+3) {
//            封装数据
            OrderUtil orderUtil = new OrderUtil();
            List<ScatterUtil> item = scatterUtils.subList(i, i + 3);
            orderUtil.setItem(item);
            float rate = (float) item.get(2).getProfit() / item.get(2).getWeight();
            orderUtil.setRate(rate);
            orderUtils.add(orderUtil);
        }
        Collections.sort(orderUtils);
    }
}
