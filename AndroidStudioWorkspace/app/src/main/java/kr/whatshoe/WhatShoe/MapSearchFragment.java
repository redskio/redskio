package kr.whatshoe.WhatShoe;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import kr.whatshoe.Util.OnTaskCompleted;


/**
 * A placeholder fragment containing a simple view.
 */
public class MapSearchFragment extends Fragment implements View.OnClickListener, OnTaskCompleted {

    private MapView mapView;
    private ViewGroup mapViewContainer;
    private EditText searchText;
    private AsyncTask geoPointTask;
    private static Double lon = new Double(0);
    private static Double lat = new Double(0);

    public MapSearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.hide();

        View rootView = inflater.inflate(R.layout.fragment_mapsearch, container, false);


        ImageButton cancelBtn = (ImageButton) rootView.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(this);




        Resources resources = getActivity().getResources();
        String key = resources.getString(R.string.mapKey);
        mapView = new MapView(getActivity());
        mapView.setDaumMapApiKey(key);
        if (MainActivity.getLocation() != null) {
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(MainActivity.getLocation().getLatitude(), MainActivity.getLocation().getLongitude());
            mapView.setMapCenterPointAndZoomLevel(mapPoint, -10, true);
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName("여기 있슈!");
            marker.setTag(0);
            marker.setMapPoint(mapPoint);
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            mapView.addPOIItem(marker);
            mapView.setMapTilePersistentCacheEnabled(true);
        }
        mapViewContainer = (ViewGroup) rootView.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        searchText = (EditText)rootView.findViewById(R.id.searchText);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



//                if(s.length()>2){
//
//                    geoPointTask.execute(s.toString());
//
//                    if(lat==0){
//                        return;
//                    }
//
//
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        MainActivity.currentFragment = MainActivity.FRAGMENT_FLAG_SEARCH;
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.cancel_btn:
                closeKeyboad(searchText);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainActivityFragment()).commit();
                break;
            default:
                break;
        }
    }

    private void closeKeyboad(EditText edittext) {

        InputMethodManager inputMethodManager =

                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(edittext.getWindowToken(), 0);

    }

    @Override
    public void onTaskCompleted() {
        mapView.removeAllPOIItems();
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(lat, lon);
        mapView.setMapCenterPointAndZoomLevel(mapPoint, -10, true);
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("여기 있슈!");
        marker.setTag(0);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(marker);
    }

    private class GeoPointTask extends AsyncTask<String, Void, Void> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }




        @Override

        protected Void doInBackground(String... params) {

            getGeoPoint(getLocationInfo(params[0].replace("\n", " ")

                    .replace(" ", "%20")));  //주소를 넘겨준다(공백이나 엔터는 제거합니다)




            return null;

        }




        @Override

        protected void onPostExecute(Void result) {



        }

    }




    public static JSONObject getLocationInfo(String address) {




        HttpGet httpGet = new HttpGet(

                "http://maps.google.com/maps/api/geocode/json?address="

                        + address + "&ka&sensor=false");

//해당 url을 인터넷창에 쳐보면 다양한 위도 경도 정보를 얻을수있다(크롬 으로실행하세요)

        HttpClient client = new DefaultHttpClient();

        HttpResponse response;

        StringBuilder stringBuilder = new StringBuilder();




        try {

            response = client.execute(httpGet);

            HttpEntity entity = response.getEntity();

            InputStream stream = entity.getContent();

            int b;

            while ((b = stream.read()) != -1) {

                stringBuilder.append((char) b);

            }

        } catch (ClientProtocolException e) {

        } catch (IOException e) {

        }




        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject = new JSONObject(stringBuilder.toString());

        } catch (JSONException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }




        return jsonObject;

    }






    public static void getGeoPoint(JSONObject jsonObject) {






        Double lon = new Double(0);

        Double lat = new Double(0);




        try {

            lon = ((JSONArray) jsonObject.get("results")).getJSONObject(0)

                    .getJSONObject("geometry").getJSONObject("location")

                    .getDouble("lng");




            lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)

                    .getJSONObject("geometry").getJSONObject("location")

                    .getDouble("lat");




        } catch (JSONException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }






        Log.d("myLog", "경도:" + lon ); //위도/경도 결과 출력

        Log.d("myLog", "위도:" + lat  );








    }


}
