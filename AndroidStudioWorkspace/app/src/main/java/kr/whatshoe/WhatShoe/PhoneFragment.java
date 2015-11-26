package kr.whatshoe.whatShoe;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.Random;

import kr.whatshoe.Util.HttpClient;


/**
 * A placeholder fragment containing a simple view.
 */
public class PhoneFragment extends Fragment implements View.OnClickListener {
    private String id = "";
    private String pass = "";
    private String name = "";
    private String gender = "";
    private String birthday = "";
    private String email = "";
    private String phone = "";
    private int checkNum = 0;
    private int type = 0;
    private EditText checkText;
    private BroadcastReceiver receiver;
    EditText phoneText;
    SharedPreferences preference;

    public PhoneFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt("type");
        id = getArguments().getString("id");
        pass = getArguments().getString("pass");
        name = getArguments().getString("name");
        gender = getArguments().getString("gender");
        birthday = getArguments().getString("birthday");
        email = getArguments().getString("email");
        preference = getActivity().getSharedPreferences("login_pref", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_phone, container, false);
        TelephonyManager telephony
                = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
//        phone = telephony.getLine1Number();
        phoneText = (EditText) rootView.findViewById(R.id.phoneText);
//        phoneText.setText(phone);
        checkText = (EditText) rootView.findViewById(R.id.checkText);
        Button checkBtn = (Button) rootView.findViewById(R.id.checkBtn);
        checkBtn.setOnClickListener(this);
        Button sendBtn = (Button) rootView.findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(this);

        Random random = new Random();
        checkNum = random.nextInt(10000);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendBtn:
                phone = phoneText.getText().toString();
                if(phone.length()<8){
                    Toast.makeText(getActivity(), "휴대폰 번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                sendSMS(phone);
                break;
            case R.id.checkBtn:
                if (checkText.getText().toString().equals(Integer.toString(checkNum))) {
                    Toast.makeText(getActivity(), "휴대폰 번호가 인증되었습니다.", Toast.LENGTH_SHORT).show();
                    if (type == LoginFragment.kakao) {
                        registerPhone();
                    } else {
                        join();
                    }
                } else {
                    Toast.makeText(getActivity(), "인증번호를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void sendSMS(String phoneNumber) {
        String SENT = "SMS_SENT";
        // 문자 보내는 상태를 감지하는 PendingIntent

        PendingIntent sentPI = PendingIntent.getBroadcast(getActivity(), 0, new Intent(SENT), 0);
        // 문자 보내는 상태를 감지하는 BroadcastReceiver를 등록한다.
        receiver = new BroadcastReceiver() {


            // 문자를 수신하면, 발생.


            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:

                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "문자발송 오류로 인해 휴대폰 인증을 진행할 수 없습니다. 고객센터로 문의 바랍니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No Service : 휴대폰 인증을 진행할 수 없습니다. 고객센터로 문의 바랍니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "NULL PDU : 휴대폰 인증을 진행할 수 없습니다. 고객센터로 문의 바랍니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Error radio off : 휴대폰 인증을 진행할 수 없습니다. 고객센터로 문의 바랍니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        getActivity().registerReceiver(receiver, new IntentFilter(SENT));

        // SmsManager를 가져온다.
        SmsManager sms = SmsManager.getDefault();
        // sms를 보낸다.

        Random random = new Random();
        checkNum = random.nextInt(10000);
        String texts = "왓슈 인증번호 : " + checkNum;

        sms.sendTextMessage(phoneNumber, null, texts, sentPI, null);

    }

    private void join() {
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("pass1", pass);
        params.put("name", name);
        params.put("sex", gender);
        params.put("birth", birthday);
        params.put("mail", email);
        params.put("phone", phone);
        params.put("pass2", pass);

        HttpClient.post("member/android_join.php", params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2,
                                  Throwable arg3) {
                Toast.makeText(getActivity(), "가입에 실패했습니다.",
                        Toast.LENGTH_SHORT).show();
                Log.i("PostingFailed", arg2);
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2) {
                if (arg2.trim().equals("\uFEFFsuccess")) {
                    registerPhone();
                } else {
                    Toast.makeText(getActivity(), "로그인 정보를 확인해 주세요. 문제가 지속될 경우 고객센터로 문의해 주세요.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerPhone() {
        Toast.makeText(getActivity(), "가입되었습니다.",
                Toast.LENGTH_SHORT).show();
        preference
                .edit()
                .putString(
                        "id", id)
                .apply();
        preference.edit().putString("phone", phone);
        getActivity().finish();
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
