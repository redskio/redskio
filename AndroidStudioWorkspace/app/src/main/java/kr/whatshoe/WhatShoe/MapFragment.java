package kr.whatshoe.whatShoe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MapFragment extends Fragment implements View.OnClickListener {
    private EditText currentDetail;
    private EditText locationDetail;
    private MapView mapView;
    private ViewGroup mapViewContainer;
    ArrayList<FixOrder> arraylist;
    public MapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.hide();

        arraylist=getArguments().getParcelableArrayList("order");
        if(arraylist== null){
            getActivity().finish();
        }
        View rootView = inflater.inflate(kr.whatshoe.whatShoe.R.layout.fragment_main, container, false);
        TextView locationText = (TextView) rootView.findViewById(kr.whatshoe.whatShoe.R.id.currentId);
        locationText.setText(MainActivity.getLocationInfo());


        currentDetail = (EditText) rootView.findViewById(R.id.currentIdDetail);
        locationDetail = (EditText) rootView.findViewById(R.id.locationDetail);

        Button locationBtn = (Button) rootView.findViewById(kr.whatshoe.whatShoe.R.id.locationBtn);
        locationBtn.setOnClickListener(this);

        Button resetBtn = (Button) rootView.findViewById(R.id.reset_location);
        resetBtn.setOnClickListener(this);


        ImageButton cancelBtn = (ImageButton) rootView.findViewById(kr.whatshoe.whatShoe.R.id.cancel_btn);
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

            currentDetail.setText(MainActivity.getCurrentLocality() + " " +
                    MainActivity.getCurrentCity() + " " + MainActivity.getLocationInfo());
        } else {
            Toast.makeText(getActivity(), "현재 위치를 가져올 수 없습니다. 직접 주소를 입력해 주세요.", Toast.LENGTH_LONG).show();
        }
        mapViewContainer = (ViewGroup) rootView.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);


        ImageButton textCancle = (ImageButton)rootView.findViewById(R.id.textCancleBtn);
        textCancle.setOnClickListener(this);

        CheckBox setOnDefaultButton = (CheckBox)rootView.findViewById(R.id.buttonSetDefault);
        final SharedPreferences preference = getActivity().getSharedPreferences("login_pref", 0);
        if(preference.getBoolean("isDefaultLocation",false)) {
            setOnDefaultButton.setChecked(true);
            locationDetail.setText(preference.getString("defaultLocation", ""));
            currentDetail.setText(preference.getString("cityLocation",""));
        }
        setOnDefaultButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(locationDetail!=null && !locationDetail.getText().toString().equals("")){
                        Toast.makeText(getActivity(),"현재 주소가 기본 주소로 저장되었습니다.",Toast.LENGTH_SHORT).show();
                        preference.edit().putBoolean("isDefaultLocation",true).apply();
                        preference.edit().putString("defaultLocation",locationDetail.getText().toString()).apply();
                        preference.edit().putString("cityLocation",currentDetail.getText().toString()).apply();
                    }
                    else {
                        Toast.makeText(getActivity(),"현재 주소가 입력되지 않았습니다.",Toast.LENGTH_SHORT).show();
                        buttonView.setChecked(false);
                    }
                } else{
                    preference.edit().remove("isDefaultLocation").apply();
                    preference.edit().remove("defaultLocation").apply();
                    preference.edit().remove("cityLocation").apply();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        MainActivity.currentFragment = MainActivity.FRAGMENT_FLAG_SERVICE;
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.textCancleBtn:
                locationDetail.setText("");
                break;
            case kr.whatshoe.whatShoe.R.id.locationBtn:
                if(this.locationDetail.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"상세 주소가 입력되지 않았습니다. 상세주소를 입력해 주세요",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (MainActivity.getCurrentCity() == null || MainActivity.getCurrentCity().equals("")) {
                    makeAlert("현재 강남구 및 서초구에서만 서비스 가능한 서비스 입니다.\n현재 위치 정보를 확인 할 수 없으니 입력하신 주소를 다시 한번 확인해 주세요.");
                } else if (!MainActivity.getCurrentCity().equals("강남구")&& !MainActivity.getCurrentCity().equals("서초구")) {
                    makeAlert("현재 강남구 및 서초구만 서비스 가능합니다. 곧 좋은 서비스로 찾아뵙겠습니다.");
                } else {
                    MainActivity.currentFragment = MainActivity.FRAGMENT_FLAG_CONTENT;
                    String locationResult = currentDetail.getText().toString();
                    String locationResultDetail = locationDetail.getText().toString();
                    intent = new Intent();
                    intent.setClass(getActivity(), PayActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("locationResult", locationResult);
                    bundle.putString("locationResultDetail", locationResultDetail);
                    bundle.putParcelableArrayList("order",arraylist);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                break;
            case R.id.reset_location:
                getActivity().getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.whatShoe.R.id.container, new MapSearchFragment()).commit();
                break;
            case kr.whatshoe.whatShoe.R.id.cancel_btn:
                closeKeyboad(currentDetail);
                getActivity().getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.whatShoe.R.id.container, new ServiceFragment()).commit();
                break;
            default:
                break;
        }
    }

    private void makeAlert(String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle("서비스 지역을 알립니다!")
                .setMessage(message)
                .setNeutralButton("확인", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.currentFragment = MainActivity.FRAGMENT_FLAG_CONTENT;
                        String locationResult = currentDetail.getText().toString();
                        String locationResultDetail = locationDetail.getText().toString();
                        Intent intent = new Intent();
                        intent.setClass(getActivity(),PayActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("locationResult", locationResult);
                        bundle.putString("locationResultDetail", locationResultDetail);
                        bundle.putParcelableArrayList("order",arraylist);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                }).setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
            }

        }).show();
    }

    private void closeKeyboad(EditText edittext) {

        InputMethodManager inputMethodManager =

                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(edittext.getWindowToken(), 0);

    }

}
