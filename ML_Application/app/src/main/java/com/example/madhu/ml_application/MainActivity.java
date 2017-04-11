package com.example.madhu.ml_application;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.madhu.ml_application.Utilities.HTTPURLConnection;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class MainActivity extends Activity {

    Button bt,bt2;
    EditText tv1,tv2;
    private String name="",pwd="";
    private ProgressDialog pDialog;
    private JSONObject json;
    private int success=0;
    LoginButton LB;
    CallbackManager callbackManager;

    private HTTPURLConnection service=new HTTPURLConnection();
    private String path = "http://10.0.2.2:8080/ML_Application/rest/Service/reg";

private String j;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();


        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();


        profileTracker.startTracking();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                System.out.println("Key is "+Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        setContentView(R.layout.activity_main);

        //fgadbjkjkb
        //hiuhiuh
        Button b;
        LB = (LoginButton) findViewById(R.id.fblogin_button);
        // LB.setReadPermissions("User_Friends");
        LB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LB.registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                // App code

                                // LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile", "user_friends"));
                                // Log.e("-->", Arrays.asList("public_profile", "user_friends").toString());
                                Toast.makeText(getApplication(), "Succesfully Fetched the data from Facebook", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancel() {
                                // App code
                                Toast.makeText(getApplication(), "Cancelled By User", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                                Toast.makeText(getApplication(), " Network Error", Toast.LENGTH_SHORT).show();
                            }


                        });


            }
        });

        bt=(Button)findViewById(R.id.button);
        bt2=(Button)findViewById(R.id.loginbutton);
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
                    if(isOnline()) {
                        //call the web service
                        new SendDataToServer().execute();
                    }
                    else
                        Toast.makeText(getApplicationContext(),"no conn",Toast.LENGTH_LONG).show();
                }
                else {

                    Toast.makeText(getApplicationContext(), "Please Enter all fields", Toast.LENGTH_LONG).show();
                }
            }
        });


    bt2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            startActivity(new Intent(getApplicationContext(),Login.class));
        }
    });
}//end oncreate method




    private void displayMessage(Profile profile){
        if(profile != null){
          //  textView.setText(profile.getName());
        }
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
            pDialog = new ProgressDialog(MainActivity.this);
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

            if(response.equals("yes"))
            {
                startActivity(new Intent(getApplicationContext(),ImageUpload.class));
                finish();
            }
            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            System.out.println("respons  "+response);
            response="";
        }
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}//end class



