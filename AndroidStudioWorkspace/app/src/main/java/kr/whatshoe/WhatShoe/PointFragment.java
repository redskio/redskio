package kr.whatshoe.whatShoe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import kr.whatshoe.Util.HttpClient;

public class PointFragment extends Fragment {
    TextView pointText;

    public PointFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_point, container, false);
        pointText = (TextView)rootView.findViewById(R.id.pointText);
        SharedPreferences preference = getActivity().getSharedPreferences("login_pref", 0);
        String id = preference.getString("id", "왓슈");
        RequestParams params = new RequestParams();
        params.put("id", id);
        HttpClient.post("member/android_getpoint.php", params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2,
                                  Throwable arg3) {
                Toast.makeText(getActivity(), "서버에 일시적인 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2) {
                pointText.setText( "0 Point");
            }
        });
        return rootView;
    }


}
