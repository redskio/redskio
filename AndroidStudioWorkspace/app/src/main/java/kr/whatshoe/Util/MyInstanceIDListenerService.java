package kr.whatshoe.Util;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by jaewoo on 2015-10-26.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = "MyInstanceIDLS";

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
