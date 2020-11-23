package com.example.foodietoronto;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodietoronto.ui.home.FeedFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private final LinkedList<String> items;
    private final LinkedList<String> rests;
    private final LinkedList<String> prices;
    private final LinkedList<String> locs;
    private final LinkedList<String> imgIDs;
    private LayoutInflater mInflater;
    private Context context;

    private FirebaseStorage imgdb = FirebaseStorage.getInstance();
    private StorageReference imgref;

    public WordListAdapter(Context con, LinkedList<String> itemnames, LinkedList<String> restnames, LinkedList<String> itemprices, LinkedList<String> itemlocations, LinkedList<String> itemimgids) {
        context = con;
        mInflater = LayoutInflater.from(context);
        items = itemnames;
        rests = restnames;
        prices = itemprices;
        locs = itemlocations;
        imgIDs = itemimgids;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.list_item, parent, false);
        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final WordViewHolder holder, int position) {
        String item = items.get(position);
        String rest = rests.get(position);
        String price = prices.get(position);
        String loc = locs.get(position);
        String imgid = imgIDs.get(position);

        holder.itemname.setText(item);
        holder.restname.setText(rest);
        holder.price.setText(price);
        holder.location.setText(loc);

        imgref = imgdb.getReference().child("images/" + imgid);
        //Glide.with(context).load(imgref).into(holder.itemimg);
        imgref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri.toString()).into(holder.itemimg);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class WordViewHolder extends RecyclerView.ViewHolder {
        public final TextView itemname;
        public final TextView restname;
        public final TextView price;
        public final TextView location;
        public final ImageView itemimg;
        final WordListAdapter mAdapter;

        public WordViewHolder(View itemView, WordListAdapter adapter) {
            super(itemView);
            itemname = itemView.findViewById(R.id.item_name);
            restname = itemView.findViewById(R.id.item_rest);
            price = itemView.findViewById(R.id.item_price);
            location = itemView.findViewById(R.id.item_location);
            itemimg = itemView.findViewById(R.id.item_image);
            mAdapter = adapter;

        }
    }
}
