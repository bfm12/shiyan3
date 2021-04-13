package com.chm.bag_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chm.bag_system.entity.Data;
import com.chm.bag_system.service.DataService;
import com.chm.bag_system.toolClass.HandleData;
import com.chm.bag_system.toolClass.Scatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: bag_system
 * @description
 * @author: BeiKe
 * @create: 2021-04-13 19:22
 **/
@Controller
public class ScatterController {

    @Resource
    DataService dataService;

    //    存放当前组价值的整型列表
    private static ArrayList<Integer> profitList=new ArrayList<>();

    //    存放当前组重量的整型列表
    private static ArrayList<Integer> weightList=new ArrayList<>();

//    存放当前散点图数据的列表
    private static ArrayList<ArrayList<Integer>> scatterData=new ArrayList<ArrayList<Integer>>();

    @PostMapping("/scatter/query")
    public String queryData(Scatter scatter, Model model){
        if(scatter.getFileName().equals("")||scatter.getGroup().equals("")){
            model.addAttribute("data",null);
            return "scatter";
        }
        profitList.clear();
        weightList.clear();
        scatterData.clear();
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
        for (int i = 0; i < weightList.size(); i++) {
            ArrayList<Integer> integers = new ArrayList<>();
            integers.add(weightList.get(i));
            integers.add(profitList.get(i));
            scatterData.add(integers);
        }
        model.addAttribute("data",scatterData);
        return "scatter";

    }
}
