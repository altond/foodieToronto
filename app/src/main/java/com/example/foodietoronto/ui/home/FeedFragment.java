package com.example.foodietoronto.ui.home;

import android.net.sip.SipSession;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.foodietoronto.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.Map;

public class FeedFragment extends Fragment {

    private FeedViewModel homeViewModel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage imgdb = FirebaseStorage.getInstance();
    private StorageReference imgref = imgdb.getReference();
    private CollectionReference posts = db.collection("Posts/");
    //private DocumentReference postsref = db.document("Posts");
    private ListenerRegistration registration;

    private TextView feed;

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

        feed = root.findViewById(R.id.textItem);
        //loadPosts();

        return root;
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
                    String itemname = post.get("itemname").toString();
                    String restname = post.get("restname").toString();
                    String price = post.get("price").toString();
                    String location = post.get("loc").toString();
                    String imgID = "";
                    if (post.get("img") != null) {
                        imgID = post.get("img").toString();
                    }


                    data += "Item Name: " + itemname
                            + "\nFood Spot Name: " + restname
                            + "\nPrice: " + price
                            + "\nLocation: " + location
                            + "\nImgID: " + imgID + "\n\n";
                }
                feed.setText(data);
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        //registration.remove();
    }

    public void loadPosts()   {
        posts.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";

                        for(QueryDocumentSnapshot post : queryDocumentSnapshots)    {
                            Map<String,Object> postmap = post.getData();
                            String itemname = postmap.get("itemname").toString();
                            String restname = post.get("restname").toString();
                            String price = post.get("price").toString();
                            String location = post.get("loc").toString();
                            //String imgID = post.get("img").toString();

                            data += "Item Name: " + itemname
                                    + "\nFood Spot Name: " + restname
                                    + "\nPrice: " + price
                                    + "\nLocation: " + location;
                                    //+ "\nImgID: " + imgID;
                        }
                        feed.setText(data);

                        Toast.makeText(getContext(), "Load Posts Success?!", Toast.LENGTH_LONG).show();
                    }
                });
    }

}