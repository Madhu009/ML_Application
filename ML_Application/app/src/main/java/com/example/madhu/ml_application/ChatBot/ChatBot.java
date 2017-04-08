package com.example.madhu.ml_application.ChatBot;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.madhu.ml_application.R;
import com.example.madhu.ml_application.ChatBot.ConversationHelper;
import com.example.madhu.ml_application.demo.custom.CustomActivity;

import java.util.Calendar;
import java.util.List;

public class ChatBot extends AppCompatActivity {

   private List<Conversation> convs;

    private EditText txt;
    private Button btsend;
    ChatAdapter adapter;
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
                   String msg=txt.getText().toString();
                    Conversation co=new Conversation(2);
                    co.setMsg(msg);
                    co.setDate(Calendar.getInstance().getTime());
                    co.setStatus(100);
                    convs.add(co);
                    adapter.notifyDataSetChanged();
                }
                else
                    Toast.makeText(getApplicationContext(),"no internet connection",Toast.LENGTH_LONG).show();

            }
        });

    }



}
