package com.chm.bag_system.controller;

import com.chm.bag_system.entity.Capacity;
import com.chm.bag_system.entity.Data;
import com.chm.bag_system.service.CapacityService;
import com.chm.bag_system.service.DataService;
import com.chm.bag_system.toolClass.ReadFile;
import com.chm.bag_system.toolClass.Result;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @program: bag_system
 * @description
 * @author: BeiKe
 * @create: 2021-04-13 17:03
 **/
@Controller
public class DatabaseController {
    
    @Resource
    DataService dataService;
    
    @Resource
    CapacityService capacityService;

    @GetMapping("/save/data/database")
    public String insert(){
        int count = capacityService.count(null);
        if (count>0){
            return "redirect:/database";
        }
        //    存储所有文件名的列表
        ArrayList<String> files=new ArrayList<>();
        files.add("idkp1-10.txt");
        files.add("sdkp1-10.txt");
        files.add("udkp1-10.txt");
        files.add("wdkp1-10.txt");
        for (String file : files) {
            ReadFile rf = new ReadFile();
            try {
                rf.clearData(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            rf.replaceCharacter();
            rf.separateVolume();
            Result result = rf.resultData();
            insertDataBase(result);
        }
        return "database";
    }

    /**
     * 将数据插入到数据库
     * @param result
     */
    private void insertDataBase(Result result) {
        for (String profit : result.getProfits()) {
            Data fileData = new Data();
            fileData.setContent(profit);
            fileData.setType("价值");
            fileData.setTeam(result.getProfits().indexOf(profit)+1);
            fileData.setFile(result.getFileName());
            dataService.save(fileData);
        }
        for (String weight : result.getWeights()) {
            Data fileData = new Data();
            fileData.setContent(weight);
            fileData.setType("重量");
            fileData.setTeam(result.getWeights().indexOf(weight)+1);
            fileData.setFile(result.getFileName());
            dataService.save(fileData);
        }
        for (String volume : result.getVolumes()) {
            Capacity volume1 = new Capacity();
            volume1.setSize(Integer.parseInt(volume));
            volume1.setTeam(result.getVolumes().indexOf(volume)+1);
            volume1.setFile(result.getFileName());
            capacityService.save(volume1);
        }
    }
}
