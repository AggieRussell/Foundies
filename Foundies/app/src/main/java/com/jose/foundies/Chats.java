package com.jose.foundies;

/**
 * Created by josec on 4/24/2017.
 */

        import java.util.ArrayList;
        import java.util.Random;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v7.app.ActionBarActivity;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewGroup;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.ListView;
        import android.widget.TextView;

public class Chats extends Fragment {

    private EditText msg_edittext;
    private String user1 = "bob", user2 = "john";
    private Random random;
    public static ArrayList<ChatMessage> chatlist;
    public static ChatAdapter chatAdapter;
    ListView msgListView;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_layout, container, false);
        final Controller controller = (Controller) getActivity().getApplicationContext();


        random = new Random();

        msg_edittext = (EditText) view.findViewById(R.id.messageEditText);
        msgListView = (ListView) view.findViewById(R.id.msgListView);

        ImageButton sendButton = (ImageButton) view.findViewById(R.id.sendMessageButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                        sendTextMessage(view);
                }

            }
        );

        // ----Set autoscroll of listview when a new message arrives----//
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);


        final ChatMessage MessageA = new ChatMessage("A","B","Hello","123",true);
        final ChatMessage MessageB = new ChatMessage("A","B","Hi","123",false);
        final ChatMessage MessageC = new ChatMessage("A","B","How are you?","123",true);
        final ChatMessage MessageD = new ChatMessage("A","B","Great! and you?","123",false);
        chatlist = new ArrayList<ChatMessage>();
        chatlist.add(MessageA);
        chatlist.add(MessageB);
        chatlist.add(MessageC);
        chatlist.add(MessageD);

        chatAdapter = new ChatAdapter(getActivity(), chatlist);
        msgListView.setAdapter(chatAdapter);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {}

    public void sendTextMessage(View v) {

        String message = msg_edittext.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {
            final ChatMessage chatMessage = new ChatMessage(user1, user2,
                    message, "" + random.nextInt(1000), true);
            chatMessage.setMsgID();
            chatMessage.body = message;
            chatMessage.Date = CommonMethods.getCurrentDate();
            chatMessage.Time = CommonMethods.getCurrentTime();
            msg_edittext.setText("");
            chatAdapter.add(chatMessage);
            chatAdapter.notifyDataSetChanged();
        }
    }
}