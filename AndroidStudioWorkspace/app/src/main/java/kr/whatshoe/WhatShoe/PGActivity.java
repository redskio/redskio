package kr.whatshoe.whatShoe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;



public class PGActivity extends AppCompatActivity {
    final private String URI_ENCODE="UTF-8";
    final private String URL_RESULT = "http://api.payapp.kr/oapi/apiLoad.html";
    private String cmd="payrequest";
    private String userid="whatshoe";
    private String goodname="Shoecare";
    private String price = "0";
    private String recvphone = "0";
    private String openpaytype ="card";
    private int code = 0;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pg);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bundle bundle = getIntent().getExtras();
        if(bundle==null || bundle.isEmpty()){
            finish();
        }
        price = bundle.getString("price");
        openpaytype = bundle.getString("openpaytype");
        recvphone = bundle.getString("recvphone");
        code = bundle.getInt("code");


        postData(URL_RESULT, cmd, userid,goodname,price,recvphone,openpaytype);
    }

    public void postData(String url, String _cmd, String _userid, String _goodname, String _price, String _recvphone, String _openpaytype) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("cmd", URLEncoder.encode(_cmd,URI_ENCODE)));
            nameValuePairs.add(new BasicNameValuePair("userid", URLEncoder.encode(_userid,URI_ENCODE)));
            nameValuePairs.add(new BasicNameValuePair("goodname", URLEncoder.encode(_goodname,URI_ENCODE)));
            nameValuePairs.add(new BasicNameValuePair("price", URLEncoder.encode(_price,URI_ENCODE)));
            nameValuePairs.add(new BasicNameValuePair("recvphone", URLEncoder.encode(_recvphone,URI_ENCODE)));
            nameValuePairs.add(new BasicNameValuePair("openpaytype", URLEncoder.encode(_openpaytype,URI_ENCODE)));
            nameValuePairs.add(new BasicNameValuePair("smsuse", URLEncoder.encode("n",URI_ENCODE)));
            nameValuePairs.add(new BasicNameValuePair("var1", URLEncoder.encode(Integer.toString(code),URI_ENCODE)));
            nameValuePairs.add(new BasicNameValuePair("feedbackurl","http://whatshoe.co.kr/member/android_feedback.php"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if(entity != null){
                final InputStream inputStream = entity.getContent();
//                Toast.makeText(PGActivity.this, " " + convertStreamToString(inputStream), Toast.LENGTH_LONG).show();

                String path = URLDecoder.decode(convertStreamToString(inputStream), "UTF-8");
                final WebView webV = (WebView)findViewById(R.id.webView);
                webV.setBackgroundColor(0x00000000);
                webV.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
                webV.getSettings().setJavaScriptEnabled(true);
                webV.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                webV.loadUrl(path);
                webV.setWebViewClient(new WebViewClient(){ @Override
                                                           public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url != null && url.startsWith("intent://")) {
                        try {
                            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                            Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                            if (existPackage != null) {
                                startActivity(intent);
                            } else {
                                Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                                marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                                startActivity(marketIntent);
                            }
                            return true;
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (url != null && url.startsWith("market://")) {
                        try {
                            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                            if (intent != null) {
                                startActivity(intent);
                            }
                            return true;
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                    view.loadUrl(url);
                    return false;
                }
                });

            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        String result="";
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String sb =stringBuilder.toString();
       int start = sb.indexOf("payurl=")+7;
       int end = sb.indexOf("errno")-1;
       sb = sb.substring(start,end);

        return sb;
    }
}

