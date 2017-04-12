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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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

        LB = (LoginButton) findViewById(R.id.fblogin_button);
         LB.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        LB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LB.registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {

                                String accessToken = loginResult.getAccessToken().getToken();
                                Log.i("accessToken", accessToken);

                                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                                        new GraphRequest.GraphJSONObjectCallback() {

                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.i("LoginActivity", response.toString());
                                        // Get facebook data from login
                                        Bundle bFacebookData = getFacebookData(object);
                                        Toast.makeText(getApplication(), "Succesfully Fetched the data from Facebook", Toast.LENGTH_SHORT).show();

                                        System.out.println("id is "+ bFacebookData.getString("id"));
                                        System.out.println("email is "+ bFacebookData.getString("email"));

                                    }
                                        });
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                                request.setParameters(parameters);
                                request.executeAsync();

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



    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        }
        catch(JSONException e) {
            Log.d("TAG","Error parsing JSON");
            return null;
        }
    }

    private void displayMessage(Profile profile){
        if(profile != null){
          //  textView.setText(profile.getName());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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



