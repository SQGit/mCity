package mcity.com.mcity;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by TekCampuz on 9/18/2015.
 */
public class HttpUtils {

    public static final String TAG = "tagHttpUtils";
    String s1 = "";

    public static String makeRequest(String url, String json) {
        Log.e(TAG, "URL-->" + url);
        Log.e(TAG, "input-->" + json);
        JSONObject jss = new JSONObject();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Content-Type", "application/json");
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);


            InputStream inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                String result = convertInputStreamToString(inputStream);
                Log.e(TAG, "output-->" + result);
                return result;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String makeRequest1(String url, String json, String id,String token) {
        Log.e("tag", "URL-->" + url);
        Log.e("tag", "input-->" + json);
        Log.e("tag","id"+id);

        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("id", id);

            httpPost.setHeader("x-access-token",token);

            httpPost.setHeader("Content-type", "application/json");
            //text/html
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);


            InputStream inputStream = httpResponse.getEntity().getContent();

            if (inputStream != null) {
                String result = convertInputStreamToString(inputStream);
                Log.e(TAG, "output-->" + result);
                return result;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Header adding multiple parameter>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public static String makeRequest2(String url, String AuthToken) {
        Log.v(TAG, "URL-->" + url);


        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("sessionToken", AuthToken);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);


            InputStream inputStream = httpResponse.getEntity().getContent();
            String result = convertInputStreamToString(inputStream);
            Log.v(TAG, "output-->" + result);
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        System.out.println(" OUTPUT -->" + result);

        return result;

    }
}
