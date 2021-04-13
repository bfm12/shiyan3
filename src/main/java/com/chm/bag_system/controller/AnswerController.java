package com.chm.bag_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chm.bag_system.entity.Capacity;
import com.chm.bag_system.entity.Data;
import com.chm.bag_system.service.CapacityService;
import com.chm.bag_system.service.DataService;
import com.chm.bag_system.toolClass.HandleData;
import com.chm.bag_system.toolClass.Scatter;
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
public class AnswerController {

    @Resource
    DataService dataService;

    @Resource
    CapacityService capacityService;

    //    存放当前组价值的整型列表
    private static ArrayList<Integer> profitList=new ArrayList<>();

    //    存放当前组重量的整型列表
    private static ArrayList<Integer> weightList=new ArrayList<>();


    @PostMapping("/answer/query")
    public String queryData(Scatter scatter, Model model){
        long l;
        long l1;
        int answer;
        if(scatter.getType().equals("")||scatter.getFileName().equals("")||scatter.getGroup().equals("")){
            model.addAttribute("answer",null);
            model.addAttribute("time",null);
            return "answer";
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
        QueryWrapper<Capacity> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("file",scatter.getFileName());
        wrapper1.eq("team",scatter.getGroup());
        Capacity one = capacityService.getOne(wrapper1);

        if(scatter.getType().equals("动态规划算法")){
            l = System.currentTimeMillis();
            answer = dp(profitList, weightList, one.getSize());
            l1 = System.currentTimeMillis();
            model.addAttribute("answer",answer);
            model.addAttribute("time",(l1-l)+"ms");
        }
        if(scatter.getType().equals("回溯算法")){
            l = System.currentTimeMillis();
            answer = back(profitList, weightList, one.getSize());
            l1 = System.currentTimeMillis();
            model.addAttribute("answer",answer);
            model.addAttribute("time",(l1-l)+"ms");
        }

        return "answer";

    }

    /**
     * 动态规划算法
     */
    public int dp(ArrayList<Integer> profitList, ArrayList<Integer> weightList, Integer currentVolume) {
//        组数
        int N=profitList.size()/3;
//        每一个项集的元素个数
        int S=3;
        int[][] p = new int[N+1][S];        // 价值（每S个为一组）
        int[][] w = new int[N+1][S];        // 重量（每S个为一组）
//        将数据存为动态规划需要的格式
        int index=0;
        for (int i = 1; i <= N; i++) {
            List<Integer> pi = profitList.subList(index, index + 3);
            int[] pa = pi.stream().mapToInt(Integer::intValue).toArray();
            p[i]=pa;
            List<Integer> wi = weightList.subList(index, index + 3);
            int[] wa = wi.stream().mapToInt(Integer::intValue).toArray();
            w[i]=wa;
            index=index+3;
        }
//        算法
        int[] dp = new int[currentVolume+1];
        for (int i = 1; i <= N; i++) {
            for (int j = currentVolume; j >= 0; j--) {
                for (int k = 0; k < 3; k++) {
                    if(j>=w[i][k]) {
                        dp[j] = Math.max(dp[j], dp[j - w[i][k]] + p[i][k]);
                    }
                }
            }
        }
        return dp[currentVolume];

    }


    /**
     * 回溯处理数据
     * @param profitsList
     * @param weightsList
     * @param volume
     * @return
     */
    public int back(ArrayList<Integer> profitsList, ArrayList<Integer> weightsList, int volume){
        //        组数
        int N=profitsList.size()/3;
//        每一个项集的元素个数
        int S=3;
        int[][] p = new int[N+1][S];        // 价值（每S个为一组）
        int[][] w = new int[N+1][S];        // 重量（每S个为一组）
//        将数据存为动态规划需要的格式
        int index=0;
        for (int i = 0; i <= N; i++) {
            if(i==0){
                int[] head=new int[]{0,0,0};
                p[i]=head;
                w[i]=head;
            }else{
                List<Integer> pi = profitsList.subList(index, index + 3);
                int[] pa = pi.stream().mapToInt(Integer::intValue).toArray();
                p[i]=pa;
                List<Integer> wi = weightsList.subList(index, index + 3);
                int[] wa = wi.stream().mapToInt(Integer::intValue).toArray();
                w[i]=wa;
                index=index+3;
            }
        }
        ArrayList<Integer> ret=new ArrayList<>();
        int totalProfit=0;
        int totalWeight=0;
        recursion(ret,volume,p,w,totalProfit,totalWeight,0,0);
        Collections.sort(ret);
        return ret.get(ret.size()-1);
    }

    /**
     * 回溯算法
     * @param ret
     * @param volume
     * @param p
     * @param w
     * @param totalProfit
     * @param totalWeight
     * @param i
     * @param j
     */
    public void  recursion(ArrayList<Integer> ret,int volume,int[][] p,int[][] w,int totalProfit,int totalWeight,int i,int j){
        if(j!=3){
//            相当于不选当前的项集
            totalProfit=totalProfit+p[i][j];
            totalWeight=totalWeight+w[i][j];
        }


//        如果加上当前物品时总重量超过了背包容量则返回上一级
        if(totalWeight>volume){
            return;
        }

        if(i==p.length-1){
            ret.add(totalProfit);
            return;
        }

        for (int k = 0; k <4 ; k++) {
            recursion(ret,volume,p,w,totalProfit,totalWeight,i+1,k);
        }
    }

}
