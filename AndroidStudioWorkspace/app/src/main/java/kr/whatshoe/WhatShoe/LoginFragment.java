package kr.whatshoe.whatShoe;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

import static com.kakao.usermgmt.response.model.UserProfile.loadFromCache;


/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {

    private CallbackManager callbackManager;
    public final static int kakao =0;
    public final static int facebook = 1;
    public final static int normal = 2;
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
        View rootView = inflater.inflate(R.layout.login_fragment,container,false);

        ViewPager pager = (ViewPager)rootView.findViewById(R.id.pager);
        Tutorial_adapter adapter = new Tutorial_adapter(inflater);
        pager.setAdapter(adapter);
        View loginView = inflater.inflate(kr.whatshoe.whatShoe.R.layout.fragment_layout, null);
        Button joinBtn = (Button) loginView.findViewById(R.id.join_ws_button);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(kr.whatshoe.whatShoe.R.id.container, new JoinFragment()).commit();
            }
        });
        loginText = (EditText) loginView.findViewById(R.id.login_id_text);
        passwdText = (EditText) loginView.findViewById(R.id.login_pw_text);
        LoginButton loginButton = (LoginButton) loginView.findViewById(kr.whatshoe.whatShoe.R.id.login_button);
        loginWsButton = (Button) loginView.findViewById(kr.whatshoe.whatShoe.R.id.login_ws_button);
        loginWsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginType = SIMPLELOGIN;
                id = loginText.getText().toString();
                String password = passwdText.getText().toString();
                if (id.equals("")) {
                    Toast.makeText(getActivity(), "아이디를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                doLogin(passwdText.getText().toString());
            }
        });
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);
        loginButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.login_facebook_btn, 0, 0, 0);
        adapter.addView(loginView);
        adapter.notifyDataSetChanged();
        return rootView;

    }
    private void doLogin(String pw){ //normal login
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
    protected void showSignup() { // for kakao Login
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
    private void onClickSignup() { // for kakao Login
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
                String id = null;
                if (loadFromCache() == null || loadFromCache().getNickname() == null) {
                    id = "";
                } else {
                    id = loadFromCache().getNickname();
                }
                PhoneFragment fragment = new PhoneFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("type", kakao);
                bundle.putString("id", id);
                bundle.putString("pass", "");
                bundle.putString("name", "");
                bundle.putString("gender", "");
                bundle.putString("birthday", "");
                bundle.putString("email", "");
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(kr.whatshoe.whatShoe.R.id.container, fragment).commit();

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

    //for facebook
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
    private void startWithFacebook(){
        String name = "";
        String email = "";
        String gender = "";
        String birthday = "";

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
            PhoneFragment fragment = new PhoneFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type",facebook);
            bundle.putString("id",email);
            bundle.putString("pass","whatshoe"+email);
            bundle.putString("name",name);
            bundle.putString("gender", gender);
            bundle.putString("birthday",birthday);
            bundle.putString("email",email);
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(kr.whatshoe.whatShoe.R.id.container, fragment).commit();
        }

    }


}
