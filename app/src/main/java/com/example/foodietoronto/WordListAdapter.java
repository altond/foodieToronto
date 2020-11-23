package com.example.foodietoronto;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private final LinkedList<String> items;
    private final LinkedList<String> rests;
    private final LinkedList<String> prices;
    private final LinkedList<String> locs;
    private final LinkedList<String> imgIDs;
    private final ArrayList<ArrayList<String>> postlist;
    private LayoutInflater mInflater;
    private Context context;

    private FirebaseStorage imgdb = FirebaseStorage.getInstance();
    private StorageReference imgref;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference posts = db.collection("Posts/");

    public WordListAdapter(Context con, LinkedList<String> itemnames, LinkedList<String> restnames, LinkedList<String> itemprices, LinkedList<String> itemlocations, LinkedList<String> itemimgids, ArrayList<ArrayList<String>> postList) {
        context = con;
        mInflater = LayoutInflater.from(context);
        items = itemnames;
        rests = restnames;
        prices = itemprices;
        locs = itemlocations;
        imgIDs = itemimgids;
        postlist = postList;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.list_item, parent, false);
        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final WordViewHolder holder, final int position) {
        String item = items.get(position);
        String rest = rests.get(position);
        String price = prices.get(position);
        final String loc = locs.get(position);
        String imgid = imgIDs.get(position);

        holder.itemname.setText(item);
        holder.restname.setText("Food Spot Name: " + rest);
        holder.price.setText("Price: " + price);
        holder.location.setText("Location: " + loc);

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

        String docID = postlist.get(position).get(0);
        final String imgID = postlist.get(position).get(1);
        final DocumentReference doc = db.document("Posts/" + docID);
        final StorageReference imgdbref = imgdb.getReference();
        final StorageReference img = imgdbref.child("images/" + imgID);
        holder.deletepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(holder.itemimg.getContext(), imgID, Toast.LENGTH_LONG).show();
                doc.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(holder.itemimg.getContext(), "Post Deleted!", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(holder.itemimg.getContext(), "Post Deletion Error!", Toast.LENGTH_LONG).show();
                            }
                        });

                if(imgID != null) {
                    //Toast.makeText(holder.itemimg.getContext(), "Image Deleted!", Toast.LENGTH_LONG).show();
                    img.delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //Toast.makeText(holder.itemimg.getContext(), "Image Deleted!", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Toast.makeText(holder.itemimg.getContext(), "Image Deletion Error!", Toast.LENGTH_LONG).show();
                                }
                            });
                }
                items.remove(position);
                rests.remove(position);
                prices.remove(position);
                locs.remove(position);
                imgIDs.remove(position);
                postlist.remove(position);
                notifyItemRemoved(position);
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
        public final Button deletepost;
        final WordListAdapter mAdapter;

        public WordViewHolder(View itemView, WordListAdapter adapter) {
            super(itemView);
            itemname = itemView.findViewById(R.id.item_name);
            restname = itemView.findViewById(R.id.item_rest);
            price = itemView.findViewById(R.id.item_price);
            location = itemView.findViewById(R.id.item_location);
            itemimg = itemView.findViewById(R.id.item_image);
            deletepost = itemView.findViewById(R.id.buttonDelete);
            mAdapter = adapter;

        }
    }
}
