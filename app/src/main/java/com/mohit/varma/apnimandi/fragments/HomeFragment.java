package com.mohit.varma.apnimandi.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.activitites.FootBiteActivity;
import com.mohit.varma.apnimandi.adapters.HomeInnerAdapter;
import com.mohit.varma.apnimandi.adapters.HomeAdapter;
import com.mohit.varma.apnimandi.interfaces.NetworkChangedCallBack;
import com.mohit.varma.apnimandi.model.Item;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements NetworkChangedCallBack {

    public static final String TAG = HomeFragment.class.getSimpleName();

    private ImageView imageView;
    private RecyclerView recyclerView;
    private ArrayList<Item> arrayList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.home_recyclerView);

//        imageView = (ImageView) view.findViewById(R.id.imageView);
//
//        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
//        StorageReference reference = firebaseStorage.getReferenceFromUrl("gs://apnimandi-c7058.appspot.com/image/mohit.jpg");
//        final long ONE_MEGABYTE = 1024 * 1024;
//        reference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                // Data for "images/island.jpg" is returns, use this as needed
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                Glide.with(getActivity()).load(bitmap)
//                        .apply(new RequestOptions().circleCrop()).transition(new DrawableTransitionOptions().crossFade()).into(imageView);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });

        arrayList = new ArrayList<>();

        arrayList.add(new Item("30% off", R.drawable.first, "Pateto", "250g - 100", 200, 1, 200));
        arrayList.add(new Item("40% off", R.drawable.second, "Onion", "250g - 100", 400, 1, 400));
        arrayList.add(new Item("50% off", R.drawable.third, "Coriender", "250g - 100", 300, 1, 300));
        arrayList.add(new Item("10% off", R.drawable.first, "Simple", "250g - 100", 500, 1, 500));
        arrayList.add(new Item("50% off", R.drawable.third, "Zinger", "250g - 100", 600, 1, 600));
        arrayList.add(new Item("20% off", R.drawable.first, "Alexder", "250g - 100", 700, 1, 700));
        arrayList.add(new Item("70% off", R.drawable.first, "Ateder", "250g - 100", 200, 1, 200));

        HomeInnerAdapter homeInnerAdapter = new HomeInnerAdapter(getActivity(), arrayList);

        List<String> list = new LinkedList<>();

        list.add("Most Popular" +
                "");

        HomeAdapter homeAdapter = new HomeAdapter(list,getActivity(), homeInnerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(homeAdapter);
        Toast.makeText(getActivity(), "oncreateview", Toast.LENGTH_SHORT).show();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Toast.makeText(context, "onattach", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(getActivity(), "onviewcreated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void networkState(boolean result) {
        Log.d(TAG, "networkState: "+result);
    }

}
