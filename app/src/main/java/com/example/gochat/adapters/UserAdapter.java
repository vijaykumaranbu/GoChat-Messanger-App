package com.example.gochat.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gochat.databinding.ItemContainerUserBinding;
import com.example.gochat.listeners.UserListener;
import com.example.gochat.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> users;
    private final UserListener listener;

    public UserAdapter(List<User> users, UserListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(itemContainerUserBinding,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setBinding(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerUserBinding itemContainerUserBinding;
        private final UserListener listener;

        public UserViewHolder(@NonNull ItemContainerUserBinding itemContainerUserBinding, UserListener listener) {
            super(itemContainerUserBinding.getRoot());
            this.itemContainerUserBinding = itemContainerUserBinding;
            this.listener = listener;
        }

        public void setBinding(User user) {
            itemContainerUserBinding.imageProfile.setImageBitmap(decodeImage(user.image));
            itemContainerUserBinding.textUserName.setText(user.name);
            itemContainerUserBinding.textPhone.setText(user.phoneNumber);

            itemContainerUserBinding.getRoot().setOnClickListener(view -> {
                listener.OnUserClicked(user);
            });
        }
    }

    public static Bitmap decodeImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}
