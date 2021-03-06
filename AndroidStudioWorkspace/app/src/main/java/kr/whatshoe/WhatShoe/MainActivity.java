package kr.whatshoe.whatShoe;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.Calendar;

import kr.whatshoe.Util.GPSListener;
import kr.whatshoe.Util.HttpClient;
import kr.whatshoe.Util.QuickStartPreference;
import kr.whatshoe.Util.RegistrationIntentService;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static final int BACKKEY_TIMEOUT = 2;
    private static final int MILLIS_IN_SEC = 1000;
    private static final int MSG_TIMER_EXPIRED = 1;
    public static final int FRAGMENT_FLAG_CONTENT = 0;
    public static final int FRAGMENT_FLAG_SERVICEORDER = 1;
    public static final int FRAGMENT_FLAG_MAP = 2;
    public static final int FRAGMENT_FLAG_SEARCH = 3;
    public static int currentFragment = FRAGMENT_FLAG_SERVICEORDER;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean mIsBackKeyPressed = false;
    public static boolean needLogin = true;
    private long mCurrTimeInMillis = 0;
    private String provider;
    private static Location location;
    private static String currentCity;
    private static String currentLocality;
    private LocationManager locationManager;
    private GPSListener gpsListener;
    private static String locationInfo = "";
    private kr.whatshoe.whatShoe.NavigationDrawerFragment mNavigationDrawerFragment;
    private SharedPreferences loginPreferences;
    private SharedPreferences orderPreferences;

    public static final boolean IS_JBMR2 = Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2;

    @Override
    protected void onResume() {
        super.onResume();
        if (currentFragment == FRAGMENT_FLAG_SERVICEORDER) {
            getInstanceIdToken();
            getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.whatShoe.R.id.container, new ServiceFragment()).commitAllowingStateLoss();
        } else if (currentFragment == FRAGMENT_FLAG_CONTENT) {
            getInstanceIdToken();
            getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.whatShoe.R.id.container, new ContentFragment()).commitAllowingStateLoss();
        }  else if (currentFragment == FRAGMENT_FLAG_MAP) {
            getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.whatShoe.R.id.container, new ServiceFragment()).commitAllowingStateLoss();
        }else {
        getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.whatShoe.R.id.container, new ContentFragment()).commitAllowingStateLoss();
        }
        actionBarSetting();
        setLocationSetting();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickStartPreference.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickStartPreference.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickStartPreference.REGISTRATION_COMPLETE));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(kr.whatshoe.whatShoe.R.layout.activity_main);

        registBroadcastReceiver();
        // After enter the activity, call Loading Activity. and transaction the ContentFragment
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoadingActivity.class);
            startActivity(intent);
            mNavigationDrawerFragment = (NavigationDrawerFragment)
                    getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
            mNavigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));
            getSupportFragmentManager().beginTransaction()
                    .add(kr.whatshoe.whatShoe.R.id.container, new ServiceFragment()).commit();
    }


    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//위치설정 엑티비티 종료 후

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setSpeedRequired(false);
                criteria.setCostAllowed(true);
                provider = locationManager.getBestProvider(criteria, true);

                if (provider == null) {//사용자가 위치설정동의 안했을때 종료
                    Toast.makeText(this, "현재 위치를 가져올 수 없습니다.", Toast.LENGTH_LONG).show();

                } else {//사용자가 위치설정 동의 했을때
                    gpsListener = new GPSListener(this, locationManager, provider);
                    String currentLoc = gpsListener.getCurrentLocation();
                    if (currentLoc != null) {
                        locationInfo = currentLoc;
                        currentCity = gpsListener.getCurrentCity();
                        currentLocality = gpsListener.getCurrentLocality();
                        location = gpsListener.getCurrentLocationData();
                        if (location == null) {
                            Toast.makeText(this, "현재 위치를 가져올 수 없습니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                break;

        }

    }

    @Override
    public void onBackPressed() {
        if (currentFragment == FRAGMENT_FLAG_SERVICEORDER) {
            if (mIsBackKeyPressed == false) {
                mIsBackKeyPressed = true;
                mCurrTimeInMillis = Calendar.getInstance().getTimeInMillis();

                Toast.makeText(this, "한번 더 Back 키를 눌러 주시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
                startTimer();

            } else {
                mIsBackKeyPressed = false;
                if (Calendar.getInstance().getTimeInMillis() <= (mCurrTimeInMillis + (BACKKEY_TIMEOUT * MILLIS_IN_SEC))) {
                    System.exit(0);
                }
            }
        }
        else if (currentFragment == FRAGMENT_FLAG_MAP){
            getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.whatShoe.R.id.container, new ServiceFragment()).commit();
        }
    }

    public static String getLocationInfo() {
        return locationInfo;
    }

    public static Location getLocation() {
        return location;
    }

    public static String getCurrentCity() {
        return currentCity;
    }

    public static String getCurrentLocality() {
        return currentLocality;
    }

    private void startTimer() {
        mTimerHandler.sendEmptyMessageDelayed(MSG_TIMER_EXPIRED, BACKKEY_TIMEOUT * MILLIS_IN_SEC);
    }

    private android.os.Handler mTimerHandler = new android.os.Handler() {


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIMER_EXPIRED: {
                    mIsBackKeyPressed = false;

                }
                break;
            }
        }
    };

    private void actionBarSetting() {
        orderPreferences = getSharedPreferences("order_pref", 0);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setElevation(0);
        View mCustomView = LayoutInflater.from(this).inflate(
                kr.whatshoe.whatShoe.R.layout.action_bar, null);

        final TextView pushIcon = (TextView) mCustomView.findViewById(R.id.push_text);
        refreshPushIcon(pushIcon);
        ImageButton actionBtn = (ImageButton) mCustomView.findViewById(R.id.action_imageButton1);
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyShoeDialog dialog = new MyShoeDialog(MainActivity.this);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        refreshPushIcon(pushIcon);
                    }
                });

                if (orderPreferences.contains("orderCode")) {
                    orderPreferences.edit().putBoolean("isRead", true).apply();
                } else {

                }
                dialog.show();
            }
        });
        actionBar.setCustomView(mCustomView);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(255, 35,
                23, 21)));
    }

    private void setLocationSetting() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        provider = locationManager.getBestProvider(criteria, true);

        if (provider == null) {  //위치정보 설정이 안되어 있으면 설정하는 엑티비티로 이동합니다
            makeAlert();
        } else if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            makeAlert();
        } else {   //get current location if is agreed
            gpsListener = new GPSListener(this, locationManager, provider);
            String currentLoc = gpsListener.getCurrentLocation();
            if (currentLoc != null) {
                locationInfo = currentLoc;
                location = gpsListener.getCurrentLocationData();
                currentCity = gpsListener.getCurrentCity();
                currentLocality = gpsListener.getCurrentLocality();
                if (location == null) {
                    makeAlert();
                }
            }
        }
    }

    private void makeAlert() {
        new AlertDialog.Builder(this)

                .setTitle("위치서비스 동의")
                .setMessage("현재 위치 확인을 위해 위치 서비스에 동의 해주세요.")
                .setNeutralButton("이동", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {

                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                    }

                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override

            public void onCancel(DialogInterface dialog) {

                finish();

            }

        }).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null && gpsListener != null) {
            locationManager.removeUpdates(gpsListener);
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }

    private void refreshPushIcon(TextView view) {
          if (orderPreferences.contains("orderCode")) {
                        if (!orderPreferences.getBoolean("isRead", true)) {
                view.setVisibility(View.VISIBLE);
                        } else {
                            view.setVisibility(View.INVISIBLE);
                        }
        }
    }
    /**
     * Instance ID를 이용하여 디바이스 토큰을 가져오는 RegistrationIntentService를 실행한다.
     */
    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    /**
     * LocalBroadcast 리시버를 정의한다. 토큰을 획득하기 위한 READY, GENERATING, COMPLETE 액션에 따라 UI에 변화를 준다.
     */
    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();


                if(action.equals(QuickStartPreference.REGISTRATION_READY)){
                    Log.i("whatshoeGCM", "ready.");
                    // 액션이 READY일 경우

                } else if(action.equals(QuickStartPreference.REGISTRATION_COMPLETE)){
                    // 액션이 COMPLETE일 경우
                    String token = intent.getStringExtra("token");
                    addToken(token);
                }else if(action.equals(QuickStartPreference.REGISTRATION_GENERATING)){
                    // 액션이 GENERATING일 경우
                    Log.i("whatshoeGCM", "generating.");

                }

            }
        };
    }
    private void addToken(final String token){
        loginPreferences = getSharedPreferences("login_pref", 0);
        String id= loginPreferences.getString("id","whatshoe");
            if(id.equals("whatshoe")){
                return;
            } else if(loginPreferences.contains("token")){
                return;
            }
            RequestParams params = new RequestParams();
            params.put("id", id );
            params.put("token", token );

            HttpClient.post("member/android_token.php", params, new TextHttpResponseHandler() {

                @Override
                public void onFailure(int arg0, Header[] arg1, String arg2,
                                      Throwable arg3) {
                    Log.i("whatshoeGCM", "token_failed_to_register");
                }

                @Override
                public void onSuccess(int arg0, Header[] arg1, String arg2) {
                    if (arg2.trim().equals("\uFEFFsuccess")) {
                        Log.i("whatshoeGCM","token_registered");
                        loginPreferences.edit().putString("token",token).commit();
                    }
                }
            });

    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Log.i("whatshoeGCM", "can use playservice.");
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("whatshoeGCM", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
