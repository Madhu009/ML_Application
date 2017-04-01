package com.example.madhu.ml_application;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.madhu.ml_application.Utilities.HTTPURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends Activity {

    Button bt;
    EditText tv1,tv2;
    private String name="",pwd="";
    private ProgressDialog pDialog;
    private JSONObject json;
    private int success=0;
    private HTTPURLConnection service;
    private String path = "http://yourdomain/add_employee.py";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt=(Button)findViewById(R.id.button);
        tv1=(EditText)findViewById(R.id.editText);
        tv2=(EditText)findViewById(R.id.editText2);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check if we get null values
                if(!tv1.getText().toString().equals("")&&!tv2.getText().toString().equals(""))
                {
                    name=tv1.getText().toString();
                    pwd=tv2.getText().toString();

                    //call the web service
                    new SendDataToServer().execute();
                }
                else {

                    Toast.makeText(getApplicationContext(), "Please Enter all fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }//end oncreate method

    private class SendDataToServer extends AsyncTask<Void,Void,Void>
    {
        HashMap<String,String> DataParams;
        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            DataParams=new HashMap<String, String>();
            DataParams.put("name", name);
            DataParams.put("pwd", pwd);
            //Call ServerData() method to call webservice and store result in response
            response=service.ServerCall(path,DataParams,"POST");
            try {
                json = new JSONObject(response);
                //Get Values from JSONobject
                System.out.println("success=" + json.get("success"));
                success = json.getInt("success");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if(success==1) {
                Toast.makeText(getApplicationContext(), "Employee Added successfully..!", Toast.LENGTH_LONG).show();
            }
        }
    }

}//end class

