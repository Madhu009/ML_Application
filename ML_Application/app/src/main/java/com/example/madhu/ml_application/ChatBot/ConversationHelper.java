package com.example.madhu.ml_application.ChatBot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madhu on 08-04-2017.
 */
public class ConversationHelper {
    private static List<Conversation> conversations=new ArrayList<Conversation>();


    public static void setConversation(Conversation c)
    {
        conversations.add(c);
    }
    public static Conversation getConversation(int index)
    {
        return conversations.get(index);
    }
    public static List<Conversation> getConversations()
    {
        return conversations;
    }

}
