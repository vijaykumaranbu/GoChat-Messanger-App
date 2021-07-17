package com.example.gochat.adapters;

import android.widget.ImageView;

import com.example.gochat.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class BindingAdapter{

    @androidx.databinding.BindingAdapter("android:imageURL")
    public static void setImageURL(ImageView imageView, String imageURL){
        try{
            Picasso.get().load(imageURL).placeholder(R.drawable.placeholder).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    imageView.animate().setDuration(300).alpha(1f).start();
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }catch (Exception e){

        }
    }
}
