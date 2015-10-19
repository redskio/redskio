package kr.whatshoe.WhatShoe;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ErrorResult;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import kr.whatshoe.Util.HttpClient;
import kr.whatshoe.widget.DialogBuilder;
import kr.whatshoe.widget.KakaoToast;


/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {

    private CallbackManager callbackManager;

    private LoginButton loginButton;
    private Button loginWsButton;
    SharedPreferences preference;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private EditText loginText;
    private EditText passwdText;
    private GraphResponse response;
    private String id = "";
    private int loginType = 0;
    private static final int SIMPLELOGIN = 1;
    private static final int FACEBOOKLOGIN = 2;
    private SessionCallback kakao_callback;
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            LoginFragment.this.response = response;
                            if(response.getJSONObject()==null){
                                Toast.makeText(getActivity(),"네트워크 연결에 문제가 있습니다.",Toast.LENGTH_SHORT).show();
                            }

                            try {
                                id = (String) response.getJSONObject().get("email");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            loginType= FACEBOOKLOGIN;
                            doLogin("whatshoe" + id);
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields","id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    public LoginFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.needLogin=false;
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        callbackManager = CallbackManager.Factory.create();


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        preference = getActivity().getSharedPreferences("login_pref", 0);

        //kakao login
        kakao_callback = new SessionCallback();
        Session.getCurrentSession().addCallback(kakao_callback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(kr.whatshoe.WhatShoe.R.layout.fragment_layout, container, false);
        Button joinBtn = (Button) rootView.findViewById(R.id.join_ws_button);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), JoinActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return rootView;

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginText = (EditText) view.findViewById(R.id.login_id_text);
        passwdText = (EditText) view.findViewById(R.id.login_pw_text);
        LoginButton loginButton = (LoginButton) view.findViewById(kr.whatshoe.WhatShoe.R.id.login_button);
        loginWsButton = (Button) view.findViewById(kr.whatshoe.WhatShoe.R.id.login_ws_button);
        loginWsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginType = SIMPLELOGIN;
                id = loginText.getText().toString();
                String password = passwdText.getText().toString();
                if(id.equals("")){
                    Toast.makeText(getActivity(),"아이디를 확인해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                doLogin(passwdText.getText().toString());
            }
        });
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);


    }

    protected void showSignup() {
        Log.d("kakao", "not registered user");
//        redirectLoginActivity();
        String message = "not registered user.\nYou should signup at UserManagememt menu.";
        Dialog dialog = new DialogBuilder(getActivity())
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onClickSignup();
                        dialog.dismiss();
                    }
                }).create();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                getActivity().finish();
            }
        });
        dialog.show();
    }
    private void onClickSignup() {
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put("nickname", "leo");

        UserManagement.requestSignup(new ApiResponseCallback<Long>() {

            @Override
            public void onSessionClosed(final ErrorResult errorResult) {
                requestMe();
            }

            @Override
            public void onNotSignedUp() {

            }

            @Override
            public void onFailure(final ErrorResult errorResult) {
                Log.e("kakao", "failed to sign up. msg = " + errorResult);
            }

            @Override
            public void onSuccess(Long aLong) {
                Toast.makeText(getActivity(), "로그인되었습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }, properties);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onDestroy(){

        super.onDestroy();
        Session.getCurrentSession().removeCallback(kakao_callback);
    }
    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        Profile profile = Profile.getCurrentProfile();
        if (profile != null && preference.contains("id")) {
            getActivity().finish();
        }

        super.onResume();
    }
    private void doLogin(String pw){
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("pass", pw);
        HttpClient.post("member/android_login.php", params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2,
                                  Throwable arg3) {
                Toast.makeText(getActivity(), "로그인에 실패했습니다.",
                        Toast.LENGTH_SHORT).show();
                Log.i("PostingFailed", arg2);
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2) {
                if (arg2.trim().equals("\uFEFFsuccess")) {
                    Toast.makeText(getActivity(), "로그인되었습니다.",
                            Toast.LENGTH_SHORT).show();
                    preference
                            .edit()
                            .putString(
                                    "id", id)
                            .apply();
                    getActivity().finish();
                } else {
                    if (loginType == SIMPLELOGIN)
                        Toast.makeText(getActivity(), "아이디와 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    else {
                        Log.i("FACEBOOK", "NEED_JOIN");
                        startWithFacebook();
                        return;
                    }
                }
            }
        });
    }
    private void startWithFacebook(){
        String name = "";
        String email = "";
        String gender = "";
        String birthday = "";
        RequestParams params = new RequestParams();
        try {
            id = (String) response.getJSONObject().get("email");
            name = (String) response.getJSONObject().get("name");
            email = (String) response.getJSONObject().get("email");
            gender = (String) response.getJSONObject().get("gender");
            birthday = (String) response.getJSONObject().get("birthday");

        } catch (JSONException e) {
            e.printStackTrace();
                if(e.getMessage().toString().equals("No value for birthday")){
                    birthday = "2015-10-01";
                }
        } finally {
            params.put("id", id);
            params.put("pass1", "whatshoe"+id);
            params.put("name", name);
            params.put("sex", gender);
            params.put("birth", birthday);
            params.put("mail", email);
            params.put("phone", "");
            params.put("pass2", "whatshoe" + id);

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
                        Toast.makeText(getActivity(), "가입되었습니다.",
                                Toast.LENGTH_SHORT).show();
                        preference
                                .edit()
                                .putString(
                                        "id", id)
                                .apply();
                        getActivity().finish();
                    }
                }
            });
        }

    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            Log.v("kakao", "session opened at Session callback");
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Log.e("kakao",exception.toString());
            }
        }
    }

    protected void requestMe() {
        Log.v("kakao", "session access at request me");
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Log.d("kakao", message);

                if (errorResult.getErrorCode() == ErrorCode.CLIENT_ERROR_CODE) {
                    KakaoToast.makeToast(getActivity().getApplicationContext(), "서비스를 이용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else {
                    kakao_callback = new SessionCallback();
                    Session.getCurrentSession().addCallback(kakao_callback);
                    Session.getCurrentSession().checkAndImplicitOpen();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.v("kakao", "session closed at request me");
                kakao_callback = new SessionCallback();
                Session.getCurrentSession().addCallback(kakao_callback);
                Session.getCurrentSession().checkAndImplicitOpen();
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Log.d("kakao", "UserProfile : " + userProfile);
//                Toast.makeText(getActivity(),userProfile.getNickname()+ " 님 환영합니다.",Toast.LENGTH_SHORT).show();
                preference
                        .edit()
                        .putString(
                                "id", userProfile.getNickname())
                        .apply();
                redirectMainActivity();
            }

            @Override
            public void onNotSignedUp() {
                Log.v("kakao", "Not signed up");
                showSignup();
            }
        });

    }

    private void redirectMainActivity() {
        Log.v("kakao", "redirect Main Activity");

        getActivity().finish();
    }
}
