package com.example.gochat.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gochat.adapters.UserAdapter;
import com.example.gochat.databinding.ActivityUsersBinding;
import com.example.gochat.models.User;
import com.example.gochat.utitilies.Constants;
import com.example.gochat.utitilies.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        getUsers();
    }

    private void setListeners(){
        binding.imageBack.setOnClickListener(view -> onBackPressed());
    }

    private void getUsers(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_NAME)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    loading(false);
                    if(queryDocumentSnapshots.size() > 0){
                        List<User> users = new ArrayList<>();
                        String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                        for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                            if(currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.phoneNumber = queryDocumentSnapshot.getString(Constants.KEY_PHONE_NUMBER);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_NEW_TOKEN);
                            users.add(user);
                        }
                        if(users.size() > 0){
                            UserAdapter adapter = new UserAdapter(users);
                            binding.userRecyclerView.setAdapter(adapter);
                        }
                        else
                            showErrorMessage();
                    }
                    else
                        showErrorMessage();
                });
    }

    private void showErrorMessage(){
        binding.textError.setText(String.format("%s","No user available"));
        binding.textError.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading){
        if(isLoading)
            binding.progressBar.setVisibility(View.VISIBLE);
        else
            binding.progressBar.setVisibility(View.GONE);
    }
}