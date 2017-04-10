package com.example.madhu.ml_application;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.madhu.ml_application.ChatBot.ChatBot;
import com.example.madhu.ml_application.Shopping.CatalogActivity;
import com.example.madhu.ml_application.Utilities.HTTPURLConnection;

import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    Button bt;
    EditText tv1,tv2;
    private String name="",pwd="";
    private ProgressDialog pDialog;
    private JSONObject json;
    private int success=0;
    private HTTPURLConnection service=new HTTPURLConnection();
    private String path = "http://10.0.2.2:8080/ML_Application/rest/Service/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bt=(Button)findViewById(R.id.btLogin);
        tv1=(EditText)findViewById(R.id.editTextL);
        tv2=(EditText)findViewById(R.id.editText2L);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tv1.getText().toString().equals("")&&!tv2.getText().toString().equals(""))
                {
                    name=tv1.getText().toString();
                    pwd=tv2.getText().toString();
                    if(isOnline()) {
                        //call the web service
                        new SendDataToServer().execute();
                    }
                    else
                        Toast.makeText(getApplicationContext(),"no internet connection",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private class SendDataToServer extends AsyncTask<Void,Void,Void>
    {
        HashMap<String,String> DataParams;
        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DataParams=new HashMap<String, String>();
            DataParams.put("name", name);
            DataParams.put("pwd", pwd);
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            //Call ServerData() method to call webservice and store result in response

            try {

                response=service.ServerCall(path,DataParams);

                //  json = new JSONObject(response);
                //Get Values from JSONobject
                //success = json.getInt("success");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            //
            System.out.println("respons  "+response);
            if(response.equals("yes")) {

                startActivity(new Intent(getApplicationContext(), ChatBot.class));
                Toast.makeText(getApplicationContext(), "Logged in successfully", Toast.LENGTH_LONG).show();
                finish();
            }
            else
                Toast.makeText(getApplicationContext(), "Invalid Username/Password", Toast.LENGTH_LONG).show();
                tv2.setText("");
        }
    }

}
