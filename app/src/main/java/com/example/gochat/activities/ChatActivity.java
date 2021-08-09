package com.example.gochat.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.gochat.R;
import com.example.gochat.adapters.ChatAdapter;
import com.example.gochat.databinding.ActivityChatBinding;
import com.example.gochat.models.ChatMessage;
import com.example.gochat.models.User;
import com.example.gochat.utilities.Constants;
import com.example.gochat.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ChatActivity extends BaseActivity {

    private ActivityChatBinding binding;
    private User receiverUser;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    private String conversionId = null;
    private Boolean isReceiverAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadUserDetails();
        init();
        listenMessage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkReceiverIsAvailable();
    }

    private void init(){
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessages,
                getBitmapFormEncodeString(receiverUser.image),
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        binding.charRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private Bitmap getBitmapFormEncodeString(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    private void listenMessage(){
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID,receiverUser.id)
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID,receiverUser.id)
                .whereEqualTo(Constants.KEY_RECEIVER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private void checkReceiverIsAvailable(){
        database.collection(Constants.KEY_COLLECTION_USERS)
                .document(receiverUser.id)
                .addSnapshotListener(ChatActivity.this, (value, error) -> {
                    if(error != null){
                        return;
                    }
                    if(value != null){
                        if(value.getLong(Constants.KEY_AVAILABILITY) != null){
                            int available = Objects.requireNonNull(
                                    value.getLong(Constants.KEY_AVAILABILITY)).intValue();
                            isReceiverAvailable = available == 1;
                        }
                    }
                    if(isReceiverAvailable)
                        binding.textUserAvailable.setText(R.string.online);
                    else
                        binding.textUserAvailable.setText(R.string.offline);
                });
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error != null)
            return;
        if(value != null){
            int count = chatMessages.size();
            for(DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessages.add(chatMessage);
                }
            }
            Collections.sort(chatMessages,(obj1,obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if(count == 0){
                chatAdapter.notifyDataSetChanged();
            }
            else {
                chatAdapter.notifyItemRangeInserted(chatMessages.size(),chatMessages.size());
                binding.charRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }
            binding.charRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
        if(conversionId == null)
            checkForConversion();
    };

    private void sendMessage(){
        if(!binding.inputMessage.getText().toString().trim().isEmpty()) {
            HashMap<String, Object> message = new HashMap<>();
            message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
            message.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
            message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString().trim());
            message.put(Constants.KEY_TIMESTAMP, new Date());
            database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
            if(conversionId != null){
                updateConversion(binding.inputMessage.getText().toString().trim());
            }
            else {
                HashMap<String,Object> conversion = new HashMap<>();
                conversion.put(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID));
                conversion.put(Constants.KEY_SENDER_NAME,preferenceManager.getString(Constants.KEY_NAME));
                conversion.put(Constants.KEY_SENDER_IMAGE,preferenceManager.getString(Constants.KEY_IMAGE));
                conversion.put(Constants.KEY_RECEIVER_ID,receiverUser.id);
                conversion.put(Constants.KEY_RECEIVER_NAME,receiverUser.name);
                conversion.put(Constants.KEY_RECEIVER_IMAGE,receiverUser.image);
                conversion.put(Constants.KEY_LAST_MESSAGE,binding.inputMessage.getText().toString().trim());
                conversion.put(Constants.KEY_TIMESTAMP,new Date());
                addConversion(conversion);
            }
            binding.inputMessage.setText(null);
        }
    }

    private void addConversion(HashMap<String,Object> conversion){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .add(conversion)
                .addOnSuccessListener(documentReference -> conversionId = documentReference.getId());
    }

    private void updateConversion(String message){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS).document(conversionId)
                .update(
                        Constants.KEY_LAST_MESSAGE,message,
                        Constants.KEY_TIMESTAMP,new Date()
                );
    }

    private void setListeners(){
        binding.imageBack.setOnClickListener(view -> onBackPressed());
        binding.imageSend.setOnClickListener(view -> sendMessage());
    }

    private void loadUserDetails(){
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        byte[] bytes = Base64.decode(receiverUser.image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
        binding.textUserName.setText(receiverUser.name);
    }

    private String getReadableDateTime(Date date){
        return new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private void checkForConversion(){
        if(chatMessages.size() != 0){
            checkForConversionRemotely(
                    preferenceManager.getString(Constants.KEY_USER_ID),
                    receiverUser.id
            );
            checkForConversionRemotely(
                    receiverUser.id,
                    preferenceManager.getString(Constants.KEY_USER_ID)
            );
        }
    }

    private void checkForConversionRemotely(String senderId,String receiverId){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID,senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID,receiverId)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    private final OnCompleteListener<QuerySnapshot> onCompleteListener = task -> {
      if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0){
          DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
          conversionId = documentSnapshot.getId();
      }
    };

}