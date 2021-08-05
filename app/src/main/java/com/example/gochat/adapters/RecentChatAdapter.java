package com.example.gochat.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gochat.databinding.ItemContainerUserBinding;
import com.example.gochat.listeners.ConversionListener;
import com.example.gochat.models.ChatMessage;
import com.example.gochat.models.User;

import java.util.List;

public class RecentChatAdapter extends RecyclerView.Adapter<RecentChatAdapter.RecentChatViewHolder>{

    private final List<ChatMessage> chatMessages;
    private final ConversionListener conversionListener;

    public RecentChatAdapter(List<ChatMessage> chatMessages,ConversionListener conversionListener) {
        this.chatMessages = chatMessages;
        this.conversionListener = conversionListener;
    }

    @NonNull
    @Override
    public RecentChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false

        );
        return new RecentChatViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentChatViewHolder holder, int position) {
        holder.setData(chatMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    class RecentChatViewHolder extends RecyclerView.ViewHolder{

        private ItemContainerUserBinding itemContainerUserBinding;

        RecentChatViewHolder(ItemContainerUserBinding itemContainerUserBinding) {
            super(itemContainerUserBinding.getRoot());
            this.itemContainerUserBinding = itemContainerUserBinding;
        }

        void setData(ChatMessage chatMessage){
            itemContainerUserBinding.imageProfile.setImageBitmap(
                    UserAdapter.decodeImage(chatMessage.conversionImage)
            );
            itemContainerUserBinding.textUserName.setText(chatMessage.conversionName);
            itemContainerUserBinding.textPhone.setText(chatMessage.message);

            itemContainerUserBinding.getRoot().setOnClickListener(view -> {
                User user = new User();
                user.id = chatMessage.conversionId;
                user.name = chatMessage.conversionName;
                user.image = chatMessage.conversionImage;
                conversionListener.OnConversionClicked(user);
            });
        }
    }
}
