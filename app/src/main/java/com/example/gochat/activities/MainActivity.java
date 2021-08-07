package com.example.gochat.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import com.example.gochat.R;
import com.example.gochat.adapters.RecentChatAdapter;
import com.example.gochat.adapters.UserAdapter;
import com.example.gochat.databinding.ActivityMainBinding;
import com.example.gochat.databinding.DialogSignOutBinding;
import com.example.gochat.listeners.ConversionListener;
import com.example.gochat.models.ChatMessage;
import com.example.gochat.models.User;
import com.example.gochat.utilities.Constants;
import com.example.gochat.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ConversionListener {

    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;
    private List<ChatMessage> conversations;
    private RecentChatAdapter recentChatAdapter;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        init();
        getToken();
        setListeners();
        listenConversion();
    }

    private void init(){
        conversations = new ArrayList<>();
        recentChatAdapter = new RecentChatAdapter(conversations,this);
        binding.recentChatRecyclerView.setAdapter(recentChatAdapter);
        database = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadDetails();
    }

    private void loadDetails(){
        String image = preferenceManager.getString(Constants.KEY_IMAGE);
        binding.imageProfile.setImageBitmap(UserAdapter.decodeImage(image));
        binding.textUserName.setText(preferenceManager.getString(Constants.KEY_NAME));
    }

    private void setListeners(){
        binding.fabAddUser.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, UsersActivity.class));
        });
        binding.imageProfile.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("phoneNumber",preferenceManager.getString(Constants.KEY_PHONE_NUMBER));
            startActivity(intent);
        });
        binding.imageSignOut.setOnClickListener(view -> showSignOutDialog());
    }

    private void listenConversion(){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);

    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error != null){
            return;
        }
        if(value != null){
            for(DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = senderId;
                    chatMessage.receiverId = receiverId;
                    if(preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)){
                        chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                        chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE);
                    }
                    else {
                        chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                        chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE);
                    }
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    conversations.add(chatMessage);
                }
                else if(documentChange.getType() == DocumentChange.Type.MODIFIED){
                    for(int i = 0 ; i < conversations.size() ; i++){
                        String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        if(conversations.get(i).senderId.equals(senderId) && conversations.get(i).receiverId.equals(receiverId)){
                            conversations.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                            conversations.get(i).dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                            break;
                        }
                    }
                }
            }
            Collections.sort(conversations,(obj1,obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
            recentChatAdapter.notifyDataSetChanged();
            binding.recentChatRecyclerView.smoothScrollToPosition(0);
            binding.recentChatRecyclerView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }
    };

    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID));
        documentReference.update(Constants.KEY_NEW_TOKEN,token)
                .addOnFailureListener(e -> {
                    showToast(e.getMessage());
                });
    }

    private void signOut(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID));
        HashMap<String,Object> updates = new HashMap<>();
        updates.put(Constants.KEY_NEW_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(aVoid -> {
                    preferenceManager.clear();
                    Intent intent = new Intent(MainActivity.this,SendOTPActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    showToast(e.getMessage());
                });
    }

    private void showSignOutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        DialogSignOutBinding binding = DialogSignOutBinding.inflate(getLayoutInflater());
        builder.setView(binding.getRoot());
        AlertDialog dialogSignOut = builder.create();
        if(dialogSignOut.getWindow() != null)
            dialogSignOut.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        binding.btnCancel.setOnClickListener(view -> dialogSignOut.dismiss());
        binding.btnSignOut.setOnClickListener(view -> signOut());
        dialogSignOut.show();
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnConversionClicked(User user) {
        Intent intent = new Intent(MainActivity.this,ChatActivity.class);
        intent.putExtra(Constants.KEY_USER,user);
        startActivity(intent);
    }
}