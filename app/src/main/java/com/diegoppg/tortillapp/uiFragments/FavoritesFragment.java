package com.diegoppg.tortillapp.uiFragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diegoppg.tortillapp.R;
import com.diegoppg.tortillapp.adapters.FavoriteAdapter;
import com.diegoppg.tortillapp.database.FactoryAPI;
import com.diegoppg.tortillapp.modelo.Tortilla;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.favoritesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        getListFavorites(
                tortillas -> {
                    recyclerView.setAdapter(new FavoriteAdapter(tortillas));
                },
                throwable -> {
                    // Handle the error here (onError)
                }
        );

    }

    public void getListFavorites(Consumer<ArrayList<Tortilla>> onSuccess, Consumer<Throwable> onError) {
        FactoryAPI.getFactoryAPI(getString(R.string.database))
                .getTortilla()
                .listTortillas()
                .thenAccept(tortillas -> {
                    if (tortillas != null) {
                        onSuccess.accept(new ArrayList<>(tortillas));
                    } else {
                        onSuccess.accept(new ArrayList<>()); // Return an empty list if tortillas are null
                    }
                })
                .exceptionally(e -> {
                    Log.d("PRUEBA", "Failed to retrieve tortillas: " + e.getMessage());
                    onError.accept(e);
                    return null;
                });
    }

    /*
    public ArrayList<Tortilla> getListFavorites() {
        ArrayList<Tortilla> list = new ArrayList<>();
        FactoryAPI.getFactoryAPI(getString(R.string.database)).getTortilla().listTortillas().thenAccept(tortillas -> {

            for (Tortilla t : tortillas) {
                Log.d("PRUEBA","tortilla: " + t);
                list.add(t);
            }

        }).exceptionally(e -> {
            // Handle sign in failure
            Log.d("PRUEBA","error: " + e.getMessage());
            return null;
        });

        return list;
    }
*/

}