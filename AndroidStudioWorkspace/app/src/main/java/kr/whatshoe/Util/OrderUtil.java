package kr.whatshoe.Util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;

import kr.whatshoe.whatShoe.R;

/**
 * Created by jaewoo on 2015-10-25.
 */
public class OrderUtil {
    Context context;

    public OrderUtil(Context context) {
        this.context = context;
    }

    public ArrayList<String> getOrderName(String code) {
        ArrayList<String> array = new ArrayList<String>();

        int index = 3;
        while (true) {
            if (index == code.length() || code.length() < 3) {
                break;
            }
            String ordercode = code.substring(index, index + 3);
            int codeItem = Integer.parseInt(ordercode);
            Resources resource = context.getResources();
            if (resource == null) {
                return array;

            }
            if (codeItem < 200) {
                String[] ordernameForman = resource.getStringArray(R.array.order_man_title);
                array.add(ordernameForman[codeItem % 100]);
            } else if (codeItem < 300) {
                String[] ordernameForman = resource.getStringArray(R.array.order_woman_title);
                array.add(ordernameForman[codeItem % 100]);
            } else if (codeItem < 400) {
                String[] ordernameForman = resource.getStringArray(R.array.premium_man_title);
                array.add(ordernameForman[codeItem % 100]);
            } else {
                String[] ordernameForman = resource.getStringArray(R.array.premium_woman_title);
                array.add(ordernameForman[codeItem % 100]);
            }
            index += 3;
        }
        return array;
    }

    public ArrayList<String> getTitle(String code) {
        String ordercode = code.substring(0, 3);
        Log.i("!!!!!!!", ordercode);
        if (ordercode.equals("#01")) {
            return getOrderName(code);
        } else {
            return oldTitle(code);
        }
    }

    public int getPrice(String code) {
        String ordercode = code.substring(0, 3);
        if (ordercode.equals("#01")) {
            return OrderPrice(code);
        } else {
            return oldPrice(code);
        }
    }

    public int OrderPrice(String code) {
        int price = 0;
        int index = 3;
        while (true) {
            if (index == code.length() || code.length() < 3) {
                break;
            }
            String ordercode = code.substring(index, index + 3);
            int codeItem = Integer.parseInt(ordercode);
            Resources resource = context.getResources();
            if (resource == null) {
                return 0;
            }
            if (codeItem < 200) {
                int[] orderpriceForman = resource.getIntArray(R.array.order_man_price);
                price += orderpriceForman[codeItem % 100];
            } else if (codeItem < 300) {
                int[] orderpriceForman = resource.getIntArray(R.array.order_woman_price);
                price += orderpriceForman[codeItem % 100];
            } else if (codeItem < 400) {
                int[] orderpriceForman = resource.getIntArray(R.array.premium_man_price);
                price += orderpriceForman[codeItem % 100];
            } else {
                int[] orderpriceForman = resource.getIntArray(R.array.premium_woman_price);
                price += orderpriceForman[codeItem % 100];
            }
            index += 3;
        }
        return price;
    }

    public int oldPrice(String code) {
        int price = 0;
        int index = 0;
        while (true) {
            index++;
            int start = code.length() - 2 * index;
            if (code.length() < 2 || start < 0) {
                break;
            }
            String ordercode = code.substring(start, start + 2);
            int codeItem = Integer.parseInt(ordercode);
            switch (codeItem) {
                case 10:
                    price += 5000;
                    break;
                case 11:
                    price += 18000;
                    break;
                case 12:
                    price += 15000;
                    break;
                case 13:
                    price += 15000;
                    break;
                case 14:
                    price += 5000;
                    break;
                case 15:
                    price += 5000;
                    break;
                case 16:
                    price += 6000;
                    break;
                case 17:
                    price += 7000;
                    break;
                case 50:
                    price += 25000;
                    break;
                case 51:
                    price += 30000;
                    break;
                case 52:
                    price += 30000;
                    break;
                case 53:
                    price += 35000;
                    break;
                case 54:
                    price += 50000;
                    break;
                case 55:
                    price += 30000;
                    break;
                case 56:
                    price += 40000;
                    break;
                case 57:
                    price += 128000;
                    break;
                case 58:
                    price += 138000;
                    break;
                case 59:
                    price += 158000;
                    break;
                case 60:
                    price += 30000;
                    break;
                case 61:
                    price += 110000;
                    break;
                case 62:
                    price += 35000;
                    break;
                case 63:
                    price += 0;
                    break;
                case 70:
                    price += 25000;
                    break;
                case 71:
                    price += 30000;
                    break;
                case 72:
                    price += 10000;
                    break;
                case 73:
                    price += 15000;
                    break;
                case 74:
                    price += 20000;
                    break;
                case 75:
                    price += 30000;
                    break;
                case 76:
                    price += 40000;
                    break;
                case 77:
                    price += 128000;
                    break;
                case 78:
                    price += 40000;
                    break;
                case 79:
                    price += 30000;
                    break;
                case 80:
                    price += 30000;
                    break;
                case 81:
                    price += 110000;
                    break;
                case 82:
                    price += 35000;
                    break;
                case 83:
                    price += 0;
                    break;
                case 90:
                    price += 2000;
                    break;
                case 91:
                    price += 4000;
                    break;
                case 92:
                    price += 0;
                    break;
            }
        }
        return price;
    }

    public ArrayList<String> oldTitle(String code) {
        ArrayList<String> array = new ArrayList<String>();
        int index = 0;
        while (true) {
            index++;

            int start = code.length() - 2 * index;
            if (code.length() < 2 || start < 0) {
                break;
            }
            String ordercode = code.substring(start, start + 2);
            int codeItem = Integer.parseInt(ordercode);
            switch (codeItem) {
                case 10:
                    array.add("구두 닦이");
                    break;
                case 11:
                    array.add("남자 통굽갈이");
                    break;
                case 12:
                    array.add("남자 보조굽");
                    break;
                case 13:
                    array.add("남자 반창덧댐");
                    break;
                case 14:
                    array.add("구두 닦이");
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
                case 50:
                    array.add("프리미엄 슈 풀케어");
                    break;
                case 51:
                    array.add("프리미엄 슈 딥케어");
                    break;
                case 52:
                    array.add("프리미엄 뒷굽갈이(중)");
                    break;
                case 53:
                    array.add("프리미엄 뒷굽갈이(대)");
                    break;
                case 54:
                    array.add("Vibram 뒷굽갈이");
                    break;
                case 55:
                    array.add("프리미엄 반창갈이(고무)");
                    break;
                case 56:
                    array.add("Vibram 반창갈이(고무)");
                    break;
                case 57:
                    array.add("전창갈이(고무)");
                    break;
                case 58:
                    array.add("이태리 전창갈이(가죽)");
                    break;
                case 59:
                    array.add("영국 전창갈이(가죽)");
                    break;
                case 60:
                    array.add("밑창 접착 수선");
                    break;
                case 61:
                    array.add("염색");
                    break;
                case 62:
                    array.add("바느질 수선");
                    break;
                case 63:
                    array.add("기타 및 특수주문");
                    break;
                case 70:
                    array.add("프리미엄 슈 풀케어");
                    break;
                case 71:
                    array.add("프리미엄 슈 딥케어");
                    break;
                case 72:
                    array.add("프리미엄 뒷굽갈이(소)");
                    break;
                case 73:
                    array.add("프리미엄 뒷굽갈이(중)");
                    break;
                case 74:
                    array.add("프리미엄 뒷굽갈이(대)");
                    break;
                case 75:
                    array.add("프리미엄 반창갈이(고무)");
                    break;
                case 76:
                    array.add("Vibram 반창갈이(고무)");
                    break;
                case 77:
                    array.add("전창갈이(고무)");
                    break;
                case 78:
                    array.add("프리미엄 힐 수선");
                    break;
                case 79:
                    array.add("프리미엄 안창갈이");
                    break;
                case 80:
                    array.add("밑창 접착 수선");
                    break;
                case 81:
                    array.add("염색");
                    break;
                case 82:
                    array.add("바느질 수선");
                    break;
                case 83:
                    array.add("기타 및 특수주문");
                    break;
                case 90:
                    array.add("남자 양말");
                    break;
                case 91:
                    array.add("스타킹 무료배달");
                    break;
                case 92:
                    array.add("대일밴드 무료배달");
                    break;
            }
        }
        return array;
    }
}