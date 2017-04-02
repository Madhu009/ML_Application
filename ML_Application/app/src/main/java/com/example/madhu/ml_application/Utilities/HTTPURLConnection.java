package com.example.madhu.ml_application.Utilities;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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



    public String ServerCall(String path, HashMap<String,String> DataParams,String requestMethod)
    {
        try
        {
            url=new URL(path);
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //conn.setRequestProperty("Content-Type",
              //      "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", "" +
                    Integer.toString(getParamsData(DataParams).getBytes().length));
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setDoOutput(true);


           // OutputStream os=conn.getOutputStream();
           // BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            DataOutputStream wr = new DataOutputStream(
                    conn.getOutputStream ());
            wr.writeBytes (getParamsData(DataParams));
            //bw.write(getParamsData(DataParams));
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

                response="not done";
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return response;

    }


    private String getParamsData(HashMap<String,String> dataParams)throws UnsupportedEncodingException
    {
        StringBuilder stb=new StringBuilder();
        boolean first=true;
        for (Map.Entry<String,String> entry:dataParams.entrySet())
        {
            if(first)
                first=false;
            else
                stb.append("&");

            stb.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
            stb.append("=");
            stb.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
        }
        return stb.toString();
    }
}
