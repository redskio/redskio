package kr.whatshoe.WhatShoe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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

public class JoinActivity extends AppCompatActivity implements View.OnClickListener{
    EditText idText;
    EditText pwText;
    EditText pwCheck;
    EditText nameText;
    EditText birthText;
    EditText mailText;
    EditText numText;
    TextView useLawText;
    TextView personalLawText;
    CheckBox useLawCheckbox;
    CheckBox personalLawCheckbox;

    String sextype = NOCHOICE_TYPE;
    public static final String NOCHOICE_TYPE = "선택 안함";
    public static final String MAN_TYPE = "남";
    public static final String WOMAN_TYPE = "여";
    SharedPreferences preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        idText=(EditText)findViewById(R.id.idText);
        pwText=(EditText)findViewById(R.id.passText);
        pwCheck = (EditText)findViewById(R.id.passconfirm);
        nameText=(EditText)findViewById(R.id.nameText);
        birthText=(EditText)findViewById(R.id.birthText);
        mailText=(EditText)findViewById(R.id.mailText);
        numText=(EditText)findViewById(R.id.numText);


        final CheckBox manCheck = (CheckBox)findViewById(R.id.join_man);
        final CheckBox womanCheck = (CheckBox)findViewById(R.id.join_woman);
        final CheckBox noCheck = (CheckBox)findViewById(R.id.join_nochoice);
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

        Button joinBtn = (Button)findViewById(R.id.join_Btn);
        joinBtn.setOnClickListener(this);
        preference = getSharedPreferences("login_pref", 0);
        useLawText=(TextView)findViewById(R.id.use_law_text);
        useLawText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Intent intent = new Intent();
//                intent.setClass(JoinActivity.this,LawActivity.class);
//                startActivity(intent);
                return false;
            }
        });
        personalLawText=(TextView)findViewById(R.id.personal_law_text);
        personalLawText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Intent intent = new Intent();
//                intent.setClass(JoinActivity.this,LawActivity.class);
//                startActivity(intent);
                return false;
            }
        });
        useLawCheckbox = (CheckBox)findViewById(R.id.use_law_checkbox);

        personalLawCheckbox = (CheckBox)findViewById(R.id.personal_law_checkbox);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_join, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.join_Btn:
                RequestParams params = new RequestParams();
                if(!checkData())
                {
                    return;
                }
                params.put("id", idText.getText());
                params.put("pass1", pwText.getText());
                params.put("name", nameText.getText());
                params.put("sex", sextype);
                params.put("birth", birthText.getText());
                params.put("mail", mailText.getText());
                params.put("phone", numText.getText());
                params.put("pass2", pwText.getText());
                HttpClient.post("member/android_join.php", params, new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int arg0, Header[] arg1, String arg2,
                                          Throwable arg3) {
                        Toast.makeText(JoinActivity.this, "가입에 실패했습니다.",
                                Toast.LENGTH_SHORT).show();
                        Log.i("PostingFailed", arg2);
                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, String arg2) {
                        Log.i("!!!!!!!!!!!!!!!!!",arg2);
                        if(arg2.trim().equals("\uFEFFsuccess")) {
                            Toast.makeText(JoinActivity.this, "가입되었습니다..",
                                    Toast.LENGTH_SHORT).show();
                            preference
                                    .edit()
                                    .putString(
                                            "id",
                                            idText.getText()
                                                    .toString())
                                    .apply();
                            finish();
                        } else {
                            Toast.makeText(JoinActivity.this, "이미 가입된 회원입니다..",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
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
        String phone = numText.getText().toString();
        if (id.length() == 0) {
            Toast.makeText(this, "아이디를 입력해야 합니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (id.length() < 4) {
            Toast.makeText(this, "아이디는 4자 이상 입력해야 합니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() == 0) {
            Toast.makeText(this, "비밀번호를 입력해야 합니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 4 || password.length() > 9) {
            Toast.makeText(this, "비밀번호는 4자 이상 10자 미만으로 입력해야 합니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.equals(passwordCk)) {
            Toast.makeText(this, "동일한 비밀번호를 두번 입력해야 합니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (!useLawCheckbox.isChecked()||!personalLawCheckbox.isChecked()){
            Toast.makeText(this, "이용 약관과 개인정보 취급방침에 동의하셔야 합니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }


    }

}
