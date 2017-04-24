package com.example.madhu.ml_application.ChatBot;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.madhu.ml_application.R;
import com.example.madhu.ml_application.ChatBot.ConversationHelper;
import com.example.madhu.ml_application.Utilities.HTTPURLConnection;
import com.example.madhu.ml_application.demo.custom.CustomActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatBot extends AppCompatActivity {

   private List<Conversation> convs;

    private String path = "http://10.0.2.2:5000/gett";
    //private String path = "http://10.0.2.2:8080/ML_Application/rest/Service/chat";//java
    private HTTPURLConnection service=new HTTPURLConnection();

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private EditText txt;
    private Button btsend;
    private ImageButton btnSpeak;
    ChatAdapter adapter;
    String msg="";
    CustomActivity c=new CustomActivity();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        convs=ConversationHelper.getConversations();
        ListView chatList = (ListView) findViewById(R.id.ChatList);
        adapter=new ChatAdapter(convs,getLayoutInflater());
        chatList.setAdapter(adapter);

        txt = (EditText) findViewById(R.id.ChatEditText);
        txt.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        btsend=(Button)findViewById(R.id.ChatSend);
        btsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txt.getText().toString().equals(""))
                {
                   msg=txt.getText().toString();

                    Conversation co=new Conversation();
                    co.setMsg(msg);
                    txt.setText("");
                    co.setDate(Calendar.getInstance().getTime());
                    co.setStatus(100);
                    convs.add(co);
                    adapter.notifyDataSetChanged();
                    new SendDataToServer().execute();
                }
                else
                    Toast.makeText(getApplicationContext(),"no text",Toast.LENGTH_LONG).show();

            }
        });

        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);



        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

    }


    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txt.setText(result.get(0));
                }
                break;
            }

        }
    }

    //Server Call
    private class SendDataToServer extends AsyncTask<Void,Void,Void>
    {
        HashMap<String,String> DataParams;
        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DataParams=new HashMap<String, String>();
            DataParams.put("msg", msg);
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
            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
            try {
                JSONObject obj = new JSONObject(response);
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                if (obj.getString("response").equals("yes")) {
                    Conversation c=convs.get(convs.size()-1);
                    c.setStatusSending("Sent");
                    adapter.notifyDataSetChanged();

                    String message = obj.getString("msg");

                    Conversation res=new Conversation();

                    res.setMsg(message);
                    res.setDate(Calendar.getInstance().getTime());
                    res.setStatusSending("Received");
                    res.setReceiver(false);
                    convs.add(res);
                    adapter.notifyDataSetChanged();

                }
                else
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();


            } catch (JSONException ex) {
                System.out.println("madhu");
                System.out.println(ex);

            }



        }
    }




}
