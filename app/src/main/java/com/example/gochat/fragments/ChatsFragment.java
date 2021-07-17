package com.example.gochat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.gochat.R;
import com.example.gochat.adapters.ContactListAdapter;
import com.example.gochat.databinding.FragmentChatsBinding;
import com.example.gochat.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private FragmentChatsBinding chatsBinding;

    public ChatsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        chatsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chats, container, false);
        chatsBinding.chatRecyclerView.setAdapter(new ContactListAdapter(getData()));
        return chatsBinding.getRoot();
    }

    private List<Contact> getData(){
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(1,
                "Vikram",
                "actor",
                "28/06/21",
                "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.mrdustbin.com%2Fen%2Fcinema%2Fsouth-indian-actor-name-with-photo%2F&psig=AOvVaw1qVay2nkJVaH6dxYvUECfd&ust=1626535272448000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCLiknKPy5_ECFQAAAAAdAAAAABAD"));
        contacts.add(new Contact(2,
                "jiva",
                "actor",
                "06/05/21",
                "https://www.google.com/url?sa=i&url=https%3A%2F%2Fin.pinterest.com%2Ft100997%2Factors%2F&psig=AOvVaw1qVay2nkJVaH6dxYvUECfd&ust=1626535272448000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCLiknKPy5_ECFQAAAAAdAAAAABAK"));
        contacts.add(new Contact(3,
                "Vikram",
                "actor",
                "28/06/21",
        "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwallpaperaccess.com%2Fvikram&psig=AOvVaw0d25LUPm0XEKTtjiGF2fmP&ust=1626537535746000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCNCivNP65_ECFQAAAAAdAAAAABAD"));
        contacts.add(new Contact(4,
                "Vikram",
                "actor",
                "28/06/21",
                "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.mrdustbin.com%2Fen%2Fcinema%2Fsouth-indian-actor-name-with-photo%2F&psig=AOvVaw1qVay2nkJVaH6dxYvUECfd&ust=1626535272448000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCLiknKPy5_ECFQAAAAAdAAAAABAD"));
        contacts.add(new Contact(5,
                "Vikram",
                "actor",
                "28/06/21",
                "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.mrdustbin.com%2Fen%2Fcinema%2Fsouth-indian-actor-name-with-photo%2F&psig=AOvVaw1qVay2nkJVaH6dxYvUECfd&ust=1626535272448000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCLiknKPy5_ECFQAAAAAdAAAAABAD"));
        contacts.add(new Contact(6,
                "Vikram",
                "actor",
                "28/06/21",
                "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.mrdustbin.com%2Fen%2Fcinema%2Fsouth-indian-actor-name-with-photo%2F&psig=AOvVaw1qVay2nkJVaH6dxYvUECfd&ust=1626535272448000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCLiknKPy5_ECFQAAAAAdAAAAABAD"));
        contacts.add(new Contact(7,
                "Vikram",
                "actor",
                "28/06/21",
                "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.mrdustbin.com%2Fen%2Fcinema%2Fsouth-indian-actor-name-with-photo%2F&psig=AOvVaw1qVay2nkJVaH6dxYvUECfd&ust=1626535272448000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCLiknKPy5_ECFQAAAAAdAAAAABAD"));
        contacts.add(new Contact(8,
                "Vikram",
                "actor",
                "28/06/21",
                "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.mrdustbin.com%2Fen%2Fcinema%2Fsouth-indian-actor-name-with-photo%2F&psig=AOvVaw1qVay2nkJVaH6dxYvUECfd&ust=1626535272448000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCLiknKPy5_ECFQAAAAAdAAAAABAD"));
        contacts.add(new Contact(9,
                "Vikram",
                "actor",
                "28/06/21",
                "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.mrdustbin.com%2Fen%2Fcinema%2Fsouth-indian-actor-name-with-photo%2F&psig=AOvVaw1qVay2nkJVaH6dxYvUECfd&ust=1626535272448000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCLiknKPy5_ECFQAAAAAdAAAAABAD"));
        contacts.add(new Contact(10,
                "Vikram",
                "actor",
                "28/06/21",
                "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.mrdustbin.com%2Fen%2Fcinema%2Fsouth-indian-actor-name-with-photo%2F&psig=AOvVaw1qVay2nkJVaH6dxYvUECfd&ust=1626535272448000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCLiknKPy5_ECFQAAAAAdAAAAABAD"));

        return contacts;
    }
}