package com.example.madhu.ml_application.Utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
    String response="";
    URL url;



    public String ServerCall(String path, HashMap<String,String> DataParams,String requestMethod)
    {
        try
        {
            url=new URL(path);
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(requestMethod);
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os=conn.getOutputStream();
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            bw.write(getParamsData(DataParams));
            bw.flush();
            bw.close();

            int responseCode=conn.getResponseCode();

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
                response="";
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
