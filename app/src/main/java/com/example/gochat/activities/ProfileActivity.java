package com.example.gochat.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.gochat.adapters.UserAdapter;
import com.example.gochat.databinding.ActivityProfileBinding;
import com.example.gochat.utilities.Constants;
import com.example.gochat.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class ProfileActivity extends BaseActivity {

    private ActivityProfileBinding binding;
    private String encodeImage;
    private PreferenceManager preferenceManager;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        checkUserIsAvailable();
    }

    private void setListeners(){
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        binding.imageAdd.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });

        binding.save.setOnClickListener(view -> {
            if(encodeImage == null)
                showToast("Select Image");
            else if(binding.inputName.getText().toString().trim().isEmpty())
                showToast("Enter Your Name");
            else if(binding.inputAbout.getText().toString().trim().isEmpty())
                showToast("About can't be empty");
            else
                if(preferenceManager.getBoolean(Constants.KEY_IS_USER_AVAILABLE))
                   updateUser();
                else
                    addUser();
        });
    }

    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        if(imageUri != null){
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                binding.imageProfile.setImageBitmap(bitmap);
                                encodeImage = encodeImage(bitmap);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
    );

    private void checkUserIsAvailable(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_PHONE_NUMBER,phoneNumber)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null &&
                            task.getResult().getDocuments().size() > 0){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_USER_AVAILABLE,true);
                        preferenceManager.putString(Constants.KEY_USER_ID,documentSnapshot.getId());
                        encodeImage = documentSnapshot.getString(Constants.KEY_IMAGE);
                        binding.imageProfile.setImageBitmap(UserAdapter.decodeImage(encodeImage));
                        binding.inputName.setText(String.format("%s",documentSnapshot.getString(Constants.KEY_NAME)));
                        binding.inputAbout.setText(documentSnapshot.getString(Constants.KEY_ABOUT));
                    }
                })
                .addOnFailureListener(e -> showToast(e.getMessage()));
    }

    private void addUser(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String,Object> data = new HashMap<>();
        data.put(Constants.KEY_NAME,binding.inputName.getText().toString().trim());
        data.put(Constants.KEY_ABOUT,binding.inputAbout.getText().toString().trim());
        data.put(Constants.KEY_PHONE_NUMBER,phoneNumber);
        data.put(Constants.KEY_IMAGE,encodeImage);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                    preferenceManager.putString(Constants.KEY_USER_ID,documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME,binding.inputName.getText().toString().trim());
                    preferenceManager.putString(Constants.KEY_PHONE_NUMBER,phoneNumber);
                    preferenceManager.putString(Constants.KEY_IMAGE,encodeImage);
                    Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener( e -> {
                    showToast(e.getMessage());
                });
    }

    private void updateUser(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String,Object> data = new HashMap<>();
        data.put(Constants.KEY_NAME,binding.inputName.getText().toString().trim());
        data.put(Constants.KEY_ABOUT,binding.inputAbout.getText().toString().trim());
        data.put(Constants.KEY_PHONE_NUMBER,phoneNumber);
        data.put(Constants.KEY_IMAGE,encodeImage);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID))
                .set(data)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                    preferenceManager.putString(Constants.KEY_NAME,binding.inputName.getText().toString().trim());
                    preferenceManager.putString(Constants.KEY_PHONE_NUMBER,phoneNumber);
                    preferenceManager.putString(Constants.KEY_IMAGE,encodeImage);
                    Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener( e -> {
                    showToast(e.getMessage());
                });
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}