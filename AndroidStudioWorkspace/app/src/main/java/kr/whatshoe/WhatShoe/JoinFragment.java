package kr.whatshoe.whatShoe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import kr.whatshoe.Util.HttpClient;

public class JoinFragment extends Fragment implements View.OnClickListener{
    EditText idText;
    EditText pwText;
    EditText pwCheck;
    EditText nameText;
    EditText birthText;
    EditText mailText;
    TextView useLawText;
    TextView personalLawText;
    CheckBox useLawCheckbox;
    CheckBox personalLawCheckbox;
    boolean isDuplicated = false;

    String sextype = NOCHOICE_TYPE;
    public static final String NOCHOICE_TYPE = "선택 안함";
    public static final String MAN_TYPE = "남";
    public static final String WOMAN_TYPE = "여";
    SharedPreferences preference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_join,container,false);
        idText=(EditText)rootView.findViewById(R.id.idText);
        pwText=(EditText)rootView.findViewById(R.id.passText);
        pwCheck = (EditText)rootView.findViewById(R.id.passconfirm);
        nameText=(EditText)rootView.findViewById(R.id.nameText);
        birthText=(EditText)rootView.findViewById(R.id.birthText);
        mailText=(EditText)rootView.findViewById(R.id.mailText);
        final CheckBox manCheck = (CheckBox)rootView.findViewById(R.id.join_man);
        final CheckBox womanCheck = (CheckBox)rootView.findViewById(R.id.join_woman);
        final CheckBox noCheck = (CheckBox)rootView.findViewById(R.id.join_nochoice);
        manCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sextype = MAN_TYPE;
                womanCheck.setChecked(false);
                noCheck.setChecked(false);
            }
        });
        womanCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sextype = WOMAN_TYPE;
                manCheck.setChecked(false);
                noCheck.setChecked(false);
            }
        });
        noCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sextype = NOCHOICE_TYPE;
                womanCheck.setChecked(false);
                manCheck.setChecked(false);

            }
        });

        Button joinBtn = (Button)rootView.findViewById(R.id.join_Btn);
        joinBtn.setOnClickListener(this);
        preference = getActivity().getSharedPreferences("login_pref", 0);
        useLawText=(TextView)rootView.findViewById(R.id.use_law_text);
        useLawText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),LawActivity.class);
                intent.putExtra("law",1);
                startActivity(intent);
                return false;
            }
        });
        personalLawText=(TextView)rootView.findViewById(R.id.personal_law_text);
        personalLawText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),LawActivity.class);
                intent.putExtra("law",2);
                startActivity(intent);
                return false;
            }
        });
        useLawCheckbox = (CheckBox)rootView.findViewById(R.id.use_law_checkbox);

        personalLawCheckbox = (CheckBox)rootView.findViewById(R.id.personal_law_checkbox);

        return rootView;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.join_Btn:

                if(!checkData())
                {
                    return;
                }
                join(idText.getText().toString());
                if(isDuplicated){
                    return;
                }
                PhoneFragment fragment = new PhoneFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("type",LoginFragment.normal);
                bundle.putString("id", idText.getText().toString());
                bundle.putString("pass",pwText.getText().toString());
                bundle.putString("name",nameText.getText().toString());
                bundle.putString("gender", sextype);
                bundle.putString("birthday", birthText.getText().toString());
                bundle.putString("email",mailText.getText().toString());
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.whatShoe.R.id.container, fragment).commit();
                break;
            default:
                break;
        }
    }
    private boolean checkData(){
        idText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String id =  idText.getText().toString();
        String password = pwText.getText().toString();
        String passwordCk = pwCheck.getText().toString();
        String name = nameText.getText().toString();
        String sex = sextype;
        String birth = birthText.getText().toString();
        String mail = mailText.getText().toString();
        if (id.length() == 0) {
            Toast.makeText(getActivity(), "아이디를 입력해야 합니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (id.length() < 4) {
            Toast.makeText(getActivity(), "아이디는 4자 이상 입력해야 합니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() == 0) {
            Toast.makeText(getActivity(), "비밀번호를 입력해야 합니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 4 || password.length() > 9) {
            Toast.makeText(getActivity(), "비밀번호는 4자 이상 10자 미만으로 입력해야 합니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.equals(passwordCk)) {
            Toast.makeText(getActivity(), "동일한 비밀번호를 두번 입력해야 합니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (!useLawCheckbox.isChecked()||!personalLawCheckbox.isChecked()){
            Toast.makeText(getActivity(), "이용 약관과 개인정보 취급방침에 동의하셔야 합니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }else{

            return true;
        }


    }
    private void join(String id ) {

        RequestParams params = new RequestParams();
        params.put("id", id);

        HttpClient.post("member/android_isused.php", params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2,
                                  Throwable arg3) {
                Toast.makeText(getActivity(), "가입에 실패했습니다. 인터넷 연결상태를 확인해 주세요.",
                        Toast.LENGTH_SHORT).show();
                Log.i("PostingFailed", arg2);
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2) {
                if (arg2.trim().equals("\uFEFFok")) {

                } else {
                    Toast.makeText(getActivity(), "이미 사용중인 아이디입니다.",
                            Toast.LENGTH_SHORT).show();
                    isDuplicated = true;
                }
            }
        });
    }
}
