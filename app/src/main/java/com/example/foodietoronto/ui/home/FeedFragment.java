package com.example.foodietoronto.ui.home;

import android.net.SSLSessionCache;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodietoronto.R;
import com.example.foodietoronto.WordListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class FeedFragment extends Fragment {

    private FeedViewModel homeViewModel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage imgdb = FirebaseStorage.getInstance();
    private StorageReference imgref = imgdb.getReference();
    private CollectionReference posts = db.collection("Posts/");
    //private DocumentReference postsref = db.document("Posts");
    private ListenerRegistration registration;

    private static final String KEY_ITEMNAME = "itemname";
    private static final String KEY_RESTNAME = "restname";
    private static final String KEY_PRICE = "price";
    private static final String KEY_LOC = "loc";
    private static final String KEY_IMG = "img";

    //private ArrayList<Map<String,String>> postList  = new ArrayList<>();
    private ArrayList<ArrayList<String>> postList  = new ArrayList<>();
    private LinkedList<String> items = new LinkedList<>();
    private LinkedList<String> restnames = new LinkedList<>();
    private LinkedList<String> prices = new LinkedList<>();
    private LinkedList<String> locations = new LinkedList<>();
    private LinkedList<String> imgIDs = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;

    private TextView feed;
    private Button delete;
    private Button sortpricelowtohigh;
    private Button sortpricehightolow;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(FeedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_feed, container, false);
        /*final TextView textView = root.findViewById(R.id.text_home);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        //fillLists();
        mRecyclerView = root.findViewById(R.id.recyclerview);
        mAdapter = new WordListAdapter(getContext(),items,restnames,prices,locations,imgIDs);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        //feed = root.findViewById(R.id.textItem);
        //feed.setText(String.valueOf(items.size()));

        /*delete = root.findViewById(R.id.buttonDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost();
            }
        });*/
        sortpricelowtohigh = root.findViewById(R.id.buttonSortLowtoHigh);
        sortpricelowtohigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderByPriceLowtoHigh();
            }
        });
        sortpricehightolow = root.findViewById(R.id.buttonSortHightoLow);
        sortpricehightolow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderByPriceHightoLow();
            }
        });
        //loadPosts();

        return root;
    }

    public void fillLists() {
        posts.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot post : task.getResult()) {
                                String id = post.getId();
                                String itemname = post.get("itemname").toString();
                                String restname = post.get("restname").toString();
                                String price = post.get("price").toString();
                                String location = post.get("loc").toString();
                                String imgID = "";
                                if (post.get("img") != null) {
                                    imgID = post.get("img").toString();
                                }

                                ArrayList<String> temp = new ArrayList<>();
                                temp.add(id); temp.add(imgID);
                                postList.add(temp);

                                items.addLast(itemname);
                                restnames.addLast(restname);
                                prices.addLast(price);
                                locations.addLast(location);
                                imgIDs.addLast(imgID);
                            }
                        }
                        else    {
                            Toast.makeText(getContext(), "Error filling lists from db!", Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(getContext(), String.valueOf(items.size()), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();


        registration = posts.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null)  {
                    return;
                }
                String data = "";

                for(QueryDocumentSnapshot post : value) {
                    String id = post.getId();
                    String itemname = post.get("itemname").toString();
                    String restname = post.get("restname").toString();
                    String price = post.get("price").toString();
                    String location = post.get("loc").toString();
                    String imgID = "";
                    if (post.get("img") != null) {
                        imgID = post.get("img").toString();
                    }

                    ArrayList<String> temp = new ArrayList<>();
                    temp.add(id); temp.add(imgID);
                    postList.add(temp);

                    data += "Item Name: " + itemname
                            + "\nFood Spot Name: " + restname
                            + "\nPrice: " + price
                            + "\nLocation: " + location
                            + "\nImgID: " + imgID + "\n\n";

                    items.addLast(itemname);
                    restnames.addLast(restname);
                    prices.addLast(price);
                    locations.addLast(location);
                    imgIDs.addLast(imgID);
                }
                //feed.setText(items.toString());
                //Toast.makeText(getContext(), String.valueOf(items.size()), Toast.LENGTH_LONG).show();
                mAdapter.notifyDataSetChanged();
                /*mAdapter = new WordListAdapter(getContext(),items,restnames,prices,locations,imgIDs);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));*/
            }
        });
        //feed.setText(items.toString());


    }

    public void orderByPriceLowtoHigh()  {
        posts
                .orderBy("price", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        items.clear();
                        restnames.clear();
                        prices.clear();
                        locations.clear();
                        imgIDs.clear();


                        for(QueryDocumentSnapshot post : queryDocumentSnapshots) {
                            String id = post.getId();
                            String itemname = post.get("itemname").toString();
                            String restname = post.get("restname").toString();
                            String price = post.get("price").toString();
                            String location = post.get("loc").toString();
                            String imgID = "";
                            if (post.get("img") != null) {
                                imgID = post.get("img").toString();
                            }

                            ArrayList<String> temp = new ArrayList<>();
                            temp.add(id); temp.add(imgID);
                            postList.add(temp);

                            data += "Item Name: " + itemname
                                    + "\nFood Spot Name: " + restname
                                    + "\nPrice: " + price
                                    + "\nLocation: " + location
                                    + "\nImgID: " + imgID + "\n\n";

                            items.addLast(itemname);
                            restnames.addLast(restname);
                            prices.addLast(price);
                            locations.addLast(location);
                            imgIDs.addLast(imgID);
                        }
                        //feed.setText(items.toString());
                        //Toast.makeText(getContext(), String.valueOf(items.size()), Toast.LENGTH_LONG).show();
                        mAdapter.notifyDataSetChanged(); //= new WordListAdapter(getContext(),items,restnames,prices,locations,imgIDs);
                        //mRecyclerView.setAdapter(mAdapter);
                        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Order By Price Error!", Toast.LENGTH_LONG).show();
                    }
                });


    }

    public void orderByPriceHightoLow()  {
        posts
                .orderBy("price", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";

                        for(QueryDocumentSnapshot post : queryDocumentSnapshots) {
                            String id = post.getId();
                            String itemname = post.get("itemname").toString();
                            String restname = post.get("restname").toString();
                            String price = post.get("price").toString();
                            String location = post.get("loc").toString();
                            String imgID = "";
                            if (post.get("img") != null) {
                                imgID = post.get("img").toString();
                            }



                            ArrayList<String> temp = new ArrayList<>();
                            temp.add(id); temp.add(imgID);
                            postList.add(temp);

                            data += "Item Name: " + itemname
                                    + "\nFood Spot Name: " + restname
                                    + "\nPrice: " + price
                                    + "\nLocation: " + location
                                    + "\nImgID: " + imgID + "\n\n";
                        }
                        feed.setText(data);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Order By Price Error!", Toast.LENGTH_LONG).show();
                    }
                });


    }

    @Override
    public void onStop() {
        super.onStop();
        //registration.remove();
    }

    public void deletePost()    {
        String docID = postList.get(0).get(0);
        String imgID = postList.get(0).get(1);
        DocumentReference doc = db.document("Posts/" + docID);
        StorageReference img = imgref.child("images/" + imgID);

        doc.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Post Deleted!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Post Deletion Error!", Toast.LENGTH_LONG).show();
                    }
                });

        if(imgID != null) {
            img.delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Image Deleted!", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Image Deletion Error!", Toast.LENGTH_LONG).show();
                        }
                    });
        }

        postList.remove(0);

    }

    public void loadPosts()   {
        posts.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";

                        for(QueryDocumentSnapshot post : queryDocumentSnapshots) {
                            String id = post.getId();
                            String itemname = post.get("itemname").toString();
                            String restname = post.get("restname").toString();
                            String price = post.get("price").toString();
                            String location = post.get("loc").toString();
                            String imgID = "";
                            if (post.get("img") != null) {
                                imgID = post.get("img").toString();
                            }

                            ArrayList<String> temp = new ArrayList<>();
                            temp.add(id); temp.add(imgID);
                            postList.add(temp);

                            data += "Item Name: " + itemname
                                    + "\nFood Spot Name: " + restname
                                    + "\nPrice: " + price
                                    + "\nLocation: " + location
                                    + "\nImgID: " + imgID + "\n\n";

                            items.addLast(itemname);
                            restnames.addLast(restname);
                            prices.addLast(price);
                            locations.addLast(location);
                            imgIDs.addLast(imgID);
                        }
                        //feed.setText(data);

                        Toast.makeText(getContext(), "Load Posts Success?!", Toast.LENGTH_LONG).show();
                    }
                });

    }

}