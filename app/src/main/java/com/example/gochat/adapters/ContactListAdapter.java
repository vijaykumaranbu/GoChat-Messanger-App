package com.example.gochat.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gochat.R;
import com.example.gochat.databinding.ItemContainerContactBinding;
import com.example.gochat.models.Contact;

import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {

    private final List<Contact> contacts;
    private LayoutInflater layoutInflater;

    public ContactListAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        ItemContainerContactBinding itemContainerContactBinding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.item_container_contact,
                parent,
                false
        );
        return new ContactViewHolder(itemContainerContactBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.setBinding(contacts.get(position));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerContactBinding itemContainerContactBinding;

        public ContactViewHolder(@NonNull ItemContainerContactBinding itemContainerContactBinding) {
            super(itemContainerContactBinding.getRoot());
            this.itemContainerContactBinding = itemContainerContactBinding;
        }

        public void setBinding(Contact contact){
            itemContainerContactBinding.setContact(contact);
            itemContainerContactBinding.executePendingBindings();
        }
    }
}
