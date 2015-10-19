package kr.whatshoe.Util;
import com.loopj.android.http.*;
/**
 * Created by jaewoo on 2015-09-28.
 */
public class HttpClient {
    private static final String BASE_URL = "http://whatshoe.co.kr/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static AsyncHttpClient getInstance() {
        return HttpClient.client;
    }

    public static void get(String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params,
                            TextHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void postJson(String url, RequestParams params,
                                JsonHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}