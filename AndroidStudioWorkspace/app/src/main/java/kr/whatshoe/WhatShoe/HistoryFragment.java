package kr.whatshoe.whatShoe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.whatshoe.Order.FixOrder;
import kr.whatshoe.Util.HttpClient;
import kr.whatshoe.Util.OrderUtil;


/**
 * A placeholder fragment containing a simple view.
 */
public class HistoryFragment extends Fragment implements View.OnClickListener {
    ArrayList<FixOrder> orderArray = new ArrayList<FixOrder>();
    private ListView listView;
    private LinearLayout empty;
    public HistoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView4);
        empty = (LinearLayout) rootView.findViewById(R.id.empty);
        RequestParams params = new RequestParams();
        SharedPreferences idPreferences = getActivity().getSharedPreferences("login_pref", 0);
        params.put("id", idPreferences.getString("id", "WhatShoe"));
        HttpClient.postJson("member/android_get_history.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                JSONArray array = response;

                for(int i = 0 ; i <array.length();i++) {
                    try {
                        JSONObject object = (JSONObject)array.get(i);
                        String code = object.getString("order_code");
                        String time = object.getString("order_time");
                        OrderUtil util = new OrderUtil(getActivity().getApplicationContext());
                        ArrayList<String> nameList = util.getTitle(code);
                        String title = "";
                        if(nameList==null||nameList.size()==0){
                            return;
                        }
                        else if(nameList.size()>1) {
                            title = nameList.get(0) + "외 " + (nameList.size() - 1) + "건";
                        }
                        else {
                            title = nameList.get(0);
                        }
                        orderArray.add(new FixOrder(title , 0, util.getPrice(code), time,i,0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(orderArray.isEmpty()){
                    empty.setVisibility(View.VISIBLE);
                }else {
                    empty.setVisibility(View.GONE);
                }
                listView.setAdapter(new HistoryAdapter(getActivity(),R.layout.history_item,orderArray));
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getActivity(), "주문정보를 가져올 수 없습니다. 고객센터에 문의하세요.", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

    }
}
