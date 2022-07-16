package com.docoding.clickcare.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.docoding.clickcare.databinding.ItemContainerRecentConversionBinding;
import com.docoding.clickcare.listeners.UserListener;
import com.docoding.clickcare.model.ChatMessage;
import com.docoding.clickcare.model.User;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.ConversionViewHolder>{

    private final List<ChatMessage> chatMessages;
    private final UserListener conversionListener;

    public RecentConversationsAdapter(List<ChatMessage> chatMessages, UserListener conversionListener) {
        this.chatMessages = chatMessages;
        this.conversionListener = conversionListener;
    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversionViewHolder(
                ItemContainerRecentConversionBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConversionViewHolder holder, int position) {
        holder.setData(chatMessages.get(position), holder);
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    class ConversionViewHolder extends RecyclerView.ViewHolder{

        ItemContainerRecentConversionBinding binding;

        ConversionViewHolder(ItemContainerRecentConversionBinding itemContainerRecentConversionBinding){
            super(itemContainerRecentConversionBinding.getRoot());
            binding = itemContainerRecentConversionBinding;
        }

        void setData(ChatMessage chatMessage, ConversionViewHolder holder){
            Glide.with(holder.itemView.getContext())
                    .load(chatMessage.conversionImage)
                    .into(binding.imageProfile);
            binding.textName.setText(chatMessage.conversionName);
            binding.textRecentMessage.setText(chatMessage.message);
            binding.textDateSend.setText(new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(chatMessage.dateObject));

            binding.getRoot().setOnClickListener(v -> {
                User user = new User();
                user.id = chatMessage.conversionId;
                user.name = chatMessage.conversionName;
                user.image = chatMessage.conversionImage;
                conversionListener.onUserClicked(user);
            });
        }
    }
}

