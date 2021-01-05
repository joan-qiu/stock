package com.qqy.stockWealth.service.shareHold.impl;

import com.qqy.stockWealth.HttpUtil;
import com.qqy.stockWealth.dal.entity.ShareHoldInfo;
import com.qqy.stockWealth.dal.entity.ShareHoldInfoExample;
import com.qqy.stockWealth.dal.mapper.ShareHoldInfoMapper;
import com.qqy.stockWealth.service.shareHold.IShareHoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShareHoldServiceImpl implements IShareHoldService {

    @Autowired
    ShareHoldInfoMapper shareHoldInfoMapper;

    @Override
    public void runStock() {

        System.out.println("\033[30;0m" + "0 好好学习" + "\033[0m");
        System.out.println("\033[31;0m" + "1 好好学习" + "\033[0m");
        System.out.println("\033[32;0m" + "2 好好学习" + "\033[0m");
        System.out.println("\033[33;0m" + "3 好好学习" + "\033[0m");
        System.out.println("\033[34;0m" + "4 好好学习" + "\033[0m");
        System.out.println("\033[35;0m" + "5 好好学习" + "\033[0m");
        System.out.println("\033[36;0m" + "6 好好学习" + "\033[0m");
        System.out.println("\033[37;0m" + "7 好好学习" + "\033[0m");
        System.out.println("\033[37;0m" + "7 好好学习" + "\033[0m");

        //30-37有颜色
        System.out.println("\033[38;0m" + "8 好好学习" + "\033[0m");

        List<String> valuesList = new ArrayList<String>();

        int ZERO=0;
        while (ZERO<100){
            try {

                System.out.println("当前时间：" + new Date());

                List<ShareHoldInfo> shareHoldInfos = getShareHoldInfoList();
                ShareHoldInfo shareHoldInfo = null;
                for (int j = 0; j < shareHoldInfos.size(); j++) {


                    shareHoldInfo = shareHoldInfos.get(j);

                    String url = "http://hq.sinajs.cn/list=" + shareHoldInfo.getStockId();

                    String aaa = HttpUtil.httpRequest(url, "GET", "", "GBK");

                    String[] splist = aaa.split("=");

                    String str[] = splist[1].replace("\"", "").replace(";", "").split(",");
                    valuesList = Arrays.asList(str);

                    String zhangfu = getPercent(Float.valueOf(String.valueOf(shareHoldInfo.getStockBaseValue())), Float.valueOf(Float.valueOf(valuesList.get(3))));
                    String zhuande = getZhuan(getChajia(Float.valueOf(String.valueOf(shareHoldInfo.getStockBaseValue())), Float.valueOf(Float.valueOf(valuesList.get(3)))), Float.valueOf(shareHoldInfo.getStockNo()));
                    System.out.println(valuesList.get(0) + ",当前价格：" + valuesList.get(3) + " 今天最高价格：" + valuesList.get(4) + " 昨日收盘价:" + valuesList.get(2) + " 成本价：" + String.valueOf(shareHoldInfo.getStockBaseValue()) +
                            "  涨幅是：" + String.valueOf(zhangfu) + "   赚了：" + zhuande);
                }

                System.out.println("\n");

                Thread.sleep(5000);

            } catch (Exception e) {

            }
        }


    }


    @Override
    public List<ShareHoldInfo> getShareHoldInfoList() {
        ShareHoldInfoExample example = new ShareHoldInfoExample();
        ShareHoldInfoExample.Criteria criteria = example.createCriteria();
        return shareHoldInfoMapper.selectByExample(example);
    }

    //算差价
    public static Float getChajia(float cost, float now) {
        return now - cost;
    }

    //算赚的
    public static String getZhuan(Float chage, Float no) {
        return String.valueOf(chage * no);
    }

    //算涨幅
    public static String getPercent(float cost, float now) {
        return String.valueOf((now - cost) / cost * 100) + "%";
    }
}

