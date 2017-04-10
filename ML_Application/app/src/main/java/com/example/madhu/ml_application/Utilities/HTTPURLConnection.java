package com.example.madhu.ml_application.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Madhu on 01-04-2017.
 */
public class HTTPURLConnection {
   public String response="";
    public URL url;



    public String ServerCall(String path, HashMap<String,String> DataParams)
    {
        response="";
        try
        {
            url=new URL(path);
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                   "application/json");

           // conn.setRequestProperty("Content-Length", "" +
             //       Integer.toString(getParamsData(DataParams).getBytes().length));
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    conn.getOutputStream ());
            wr.writeBytes (convertMapToJson(DataParams).toString());
            wr.flush();
            wr.close();

            int responseCode=conn.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            if(responseCode== HttpsURLConnection.HTTP_OK)
            {
                String line="";

                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine())!=null)
                {
                    response+=line;
                }
            }
            else
            {
                response="error";
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return response;

    }


    public JSONObject convertMapToJson (HashMap<String,String> map)
    {
        JSONObject json=new JSONObject(map);
        return json;
    }
}
