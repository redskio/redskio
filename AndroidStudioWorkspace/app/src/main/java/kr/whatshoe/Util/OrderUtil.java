package kr.whatshoe.Util;

import java.util.ArrayList;

/**
 * Created by jaewoo on 2015-10-25.
 */
public class OrderUtil {
    public ArrayList<String> getOrderName(int code){
        ArrayList<String> array = new ArrayList<String>();
        while(code%100!=0){
            int codeItem = code%100;
            code= code/100;
            switch(codeItem){
                case 10:
                    array.add("남자 구두닦이");
                    break;
                case 11:
                    array.add("남자 통굽갈이");
                    break;
                case 12:
                    array.add("남자 반창 덧댐");
                    break;
                case 13:
                    array.add("남자 보조굽");
                    break;
                case 14 :
                    array.add("여자 구두닦이");
                    break;
                case 15:
                    array.add("여자 보조굽");
                    break;
                case 16:
                    array.add("여자 보조굽(중)");
                    break;
                case 17:
                    array.add("여자 보조굽(대)");
                    break;

            }
        }
        return array;
    }
    public int getOrderPrice(int code){
        int price = 0;
        while(code%100!=0){
            int codeItem = code%100;
            code= code/100;
            switch(codeItem){
                case 10:
                 price+=5000;
                    break;
                case 11:
                    price+=18000;
                    break;
                case 12:
                    price+=15000;
                    break;
                case 13:
                    price+=15000;
                    break;
                case 14 :
                    price+=5000;
                    break;
                case 15:
                    price+=5000;
                    break;
                case 16:
                    price+=6000;
                    break;
                case 17:
                    price+=7000;
                    break;

            }
        }
        return price;
    }
}
