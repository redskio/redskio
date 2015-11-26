package kr.whatshoe.whatShoe;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import kr.whatshoe.Util.HttpClient;
import kr.whatshoe.Util.UnderlineText;

public class GiftDetail2Fragment extends Fragment implements View.OnClickListener {
    private ArrayList<GiftPerson> personArray;
    private GiftPersonAdapter adapter;
    private ListView listView;
    private int sendType = 0;
    private int SMS = 1;
    private int KAKAO = 2;
    private int couponType = 0;
    private UnderlineText messageText;
    public static final long HOUR = 3600*1000;
    String id = "";
    Cursor cursor;
    String couponCode;
    private BroadcastReceiver receiver;
    public GiftDetail2Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((DrawerActivity)getActivity()).type = DrawerActivity.GIFT_DETAIL_FRAGMENT2_TYPE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_gift_detail2, container, false);

        ImageView couponImage = (ImageView) rootView.findViewById(R.id.coupon_image_detail2);
        couponType = getArguments().getInt("type");
        switch (couponType) {
            case 1:
                couponImage.setImageResource(R.drawable.gift_detail);
                break;
            case 2:
                couponImage.setImageResource(R.drawable.gift_detail1);
                break;
            case 3:
                couponImage.setImageResource(R.drawable.gift_detail2);
                break;
            case 4:
                couponImage.setImageResource(R.drawable.gift_detail3);
                break;
        }


        messageText = (UnderlineText) rootView.findViewById(R.id.messageText);
        TextView fromIDText = (TextView) rootView.findViewById(R.id.fromIDText);
        SharedPreferences idPreferences = getActivity().getSharedPreferences("login_pref", 0);
        id = idPreferences.getString("id", "WhatShoe");
        fromIDText.setText("From. " + id);
        //End set FromID through Preferences saved ID

        //start giftPerson
        listView = (ListView) rootView.findViewById(R.id.giftPersonListView);
        Button addGiftPerson = (Button) rootView.findViewById(R.id.addGiftPerson);
        Button contactsBtn = (Button) rootView.findViewById(R.id.contactsBtn);

        personArray = new ArrayList<GiftPerson>();
        personArray.add(new GiftPerson("", "", 1));
        adapter = new GiftPersonAdapter(getActivity(), R.layout.gift_person_item, personArray);
        listView.setAdapter(adapter);

        addGiftPerson.setOnClickListener(this);
        contactsBtn.setOnClickListener(this);
        //end giftPerson

        Button cardPay = (Button) rootView.findViewById(R.id.cardPay);
        Button pointPay = (Button) rootView.findViewById(R.id.pointGift);
        cardPay.setOnClickListener(this);
        pointPay.setOnClickListener(this);

        final CheckBox smsCheck = (CheckBox)rootView.findViewById(R.id.smsCheck);
        final CheckBox kakaoCheck = (CheckBox)rootView.findViewById(R.id.kakaoCheck);
        smsCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    smsCheck.setChecked(true);
                    kakaoCheck.setChecked(false);
                    RelativeLayout smsLayout = (RelativeLayout) rootView.findViewById(R.id.smsLayout);
                    smsLayout.setVisibility(View.VISIBLE);
                    LinearLayout kakaoLayout = (LinearLayout) rootView.findViewById(R.id.kakaoLayout);
                    kakaoLayout.setVisibility(View.GONE);
                    sendType=SMS;
                }
            }
        });

        kakaoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    smsCheck.setChecked(false);
                    kakaoCheck.setChecked(true);
                    RelativeLayout smsLayout = (RelativeLayout) rootView.findViewById(R.id.smsLayout);
                    smsLayout.setVisibility(View.GONE);
                    LinearLayout kakaoLayout = (LinearLayout) rootView.findViewById(R.id.kakaoLayout);
                    kakaoLayout.setVisibility(View.VISIBLE);
                    sendType=KAKAO;
                }
            }
        });
        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addGiftPerson:
                personArray.add(new GiftPerson("", "", 1));
                adapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(listView);
                break;
            case R.id.contactsBtn:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivityForResult(intent, 0);
                break;
            case R.id.cardPay:
                for (int i = 0; i < personArray.size(); i++) {
                    GiftPerson person = personArray.get(i);
                    if (person.getPhoneNum().length() > 9 && person.getPhoneNum().length() < 14) {
                        sendSMS(person.getPhoneNum(), messageText.getText().toString());
                    } else {
                        Toast.makeText(getActivity(), "전화번호를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                break;
            case R.id.pointGift:
                for (int i = 0; i < personArray.size(); i++) {
                    GiftPerson person = personArray.get(i);
                    if (person.getPhoneNum().length() > 9 && person.getPhoneNum().length() < 14) {
                        sendSMS(person.getPhoneNum(), messageText.getText().toString());
                    } else {
                        Toast.makeText(getActivity(), "전화번호를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String people_Name = "";
            String people_Number = "";
            Uri dataUri = data.getData();
            cursor = getActivity().managedQuery(dataUri, null, null, null, null);
            while (cursor.moveToNext()) {
                int getcolumnId = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                String id = cursor.getString(getcolumnId);
                people_Name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                String hasPhoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (hasPhoneNumber.equalsIgnoreCase("1")) {
                    hasPhoneNumber = "true";
                } else {
                    hasPhoneNumber = "false";
                }
                if (Boolean.parseBoolean(hasPhoneNumber)) {
                    Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                    while (phones.moveToNext()) {
                        people_Number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phones.close();    //	End
                }


            }
            addContactsPersonAfterQuery(people_Name, people_Number);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addContactsPersonAfterQuery(String people_Name, String people_Number) {
        for (int i = 0; i < personArray.size(); i++) {
            GiftPerson person = personArray.get(i);
            if (person.getName().equals("") && person.getPhoneNum().equals("")) {
                personArray.set(i, new GiftPerson(people_Name, people_Number, 1));
                adapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(listView);
                return;
            }
        }
        personArray.add(new GiftPerson(people_Name, people_Number, 1));
        adapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(listView);
    }


    public void setListViewHeightBasedOnChildren(ListView listView) {
        GiftPersonAdapter listAdapter = (GiftPersonAdapter) listView.getAdapter();
        if (listAdapter == null) {
            //pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private void sendSMS(String phoneNumber, String message) {
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
                        addCoupon(couponCode);
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        getActivity().registerReceiver(receiver, new IntentFilter(SENT));
        // SmsManager를 가져온다.
        SmsManager sms = SmsManager.getDefault();
        // sms를 보낸다.
        ArrayList<String> texts = new ArrayList<String>();
        if (message.length() > 30 && message.length() < 60) {
            texts.add(message.substring(0, 30));
            texts.add(message.substring(31));
        } else if (message.length() > 60) {

        } else {
            texts.add(message);
        }
        Random random = new Random();
        int couponNum = random.nextInt(100000);
        couponCode = id.substring(0, 2) + couponNum;
        texts.add("마음을 담은 왓슈! 쿠폰받고 빛나는 하루와 구두 되세요~\n");
        texts.add("상품명 : 왓슈 불광 쿠폰 5000\n");
        texts.add("상품수량: 1개\n쿠폰번호: " + couponCode);
        ArrayList<PendingIntent> pi = new ArrayList<PendingIntent>();
        for (int i = 0; i < texts.size(); i++) {
            pi.add(sentPI);
        }
        sms.sendMultipartTextMessage(phoneNumber, null, texts, pi, null);

    }

    private void addCoupon(String CouponCode) {
        RequestParams params = new RequestParams();
//        params.put("id", idText.getText());
        params.put("desc", couponType);
        params.put("code", CouponCode);
        params.put("date", getEventTime());
        params.put("price", 5000);
        params.put("owner", "whatshoe");

        HttpClient.post("member/android_coupon_add.php", params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2,
                                  Throwable arg3) {
                Toast.makeText(getActivity(), "인터넷 접속상태를 확인해 주세요.\n 문제가 계속 발생할 경우 관리자에게 문의해 주세요.",
                        Toast.LENGTH_SHORT).show();
                Log.i("PostingFailed", arg2);
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2) {
                if (arg2.trim().equals("\uFEFFsuccess")) {
                    Toast.makeText(getActivity(), "성공적으로 쿠폰을 발송했습니다.", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else {
                    getActivity().finish();
                }
            }
        });

    }
    private String getEventTime(){
        SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy.MM.dd HH:mm:ss", Locale.KOREAN );
        Date currentTime = new Date();
        String dTime = formatter.format ( currentTime );
        return dTime;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(receiver!=null) {
            getActivity().unregisterReceiver(receiver);
        }
    }
}
