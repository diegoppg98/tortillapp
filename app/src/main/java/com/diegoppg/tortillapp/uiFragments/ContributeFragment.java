package com.diegoppg.tortillapp.uiFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.diegoppg.tortillapp.R;
import com.diegoppg.tortillapp.database.FactoryAPI;
import com.diegoppg.tortillapp.firebase.FirebaseTortilla;
import com.diegoppg.tortillapp.modelo.Tortilla;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContributeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContributeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContributeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContributeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContributeFragment newInstance(String param1, String param2) {
        ContributeFragment fragment = new ContributeFragment();
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
        return inflater.inflate(R.layout.fragment_contribute, container, false);
    }

    MapView mapView = null;
    GestureDetector gestureDetector;
    private Marker currentMarker;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load/initialize the osmdroid configuration
        File osmdroidBasePath = new File(requireContext().getCacheDir(), "osmdroid");
        Configuration.getInstance().setOsmdroidBasePath(osmdroidBasePath);
        Configuration.getInstance().setOsmdroidTileCache(osmdroidBasePath);

        // Initialize the MapView
        mapView = view.findViewById(R.id.mapviewAdd);
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        // Set default zoom and location
        mapView.getController().setZoom(10.0);
        GeoPoint startPoint = new GeoPoint(43.53368, -5.64822);
        mapView.getController().setCenter(startPoint);


        // Crear y añadir un MapEventsOverlay para detectar eventos en el mapa
        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                // No hacemos nada en un single tap
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                // If there's not an existing marker, create it
                if (currentMarker == null) {
                    currentMarker = new Marker(mapView);
                }

                // Añadir un marcador en la ubicación del long press
                currentMarker.setPosition(p);
                currentMarker.setTitle("Tortilla");
                currentMarker.setSnippet("Lat: " + p.getLatitude() + "\n Lon: " + p.getLongitude());
                mapView.getOverlays().add(currentMarker);
                mapView.invalidate(); // Refresca el mapa para mostrar el marcador

                return true;
            }
        };

        MapEventsOverlay OverlayEventos = new MapEventsOverlay(mReceive);
        mapView.getOverlays().add(OverlayEventos);

        //Add button listener
        Button addButton = view.findViewById(R.id.AddButton);

        addButton.setOnClickListener(v -> {

            if (currentMarker != null) {
                EditText name = view.findViewById(R.id.NameTortilla);
                // Add the marker to the database
                FactoryAPI.getFactoryAPI(getString(R.string.database)).getTortilla().
                        addTortilla(name.getText().toString(), currentMarker.getPosition().getLatitude(),
                                currentMarker.getPosition().getLongitude());
            }

        });










/*
        mapView.setOnTouchListener((View v, MotionEvent event) -> {
            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {

                // Get the coordinates where the user long pressed
                GeoPoint geoPoint = (GeoPoint) mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY());

                // If there's an existing marker, remove it
                if (currentMarker != null) {
                    mapView.getOverlays().remove(currentMarker);
                }

                // Add a new marker at the long pressed position
                currentMarker = new Marker(mapView);
                currentMarker.setPosition(geoPoint);
                currentMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                currentMarker.setTitle("Marker at: " + geoPoint.getLatitude() + ", " + geoPoint.getLongitude());

                // Set a click listener to delete the marker when clicked
                currentMarker.setOnMarkerClickListener((marker, mapView) -> {
                    mapView.getOverlays().remove(marker);
                    currentMarker = null; // Clear the reference to the current marker
                    mapView.invalidate();
                    return true;
                });

                mapView.getOverlays().add(currentMarker);

                // Refresh the map view
                mapView.invalidate();

            }
            return true;
        });
*/




        /*
        // Set up the GestureDetector to handle long clicks
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                Log.w("mark", "ADd");

                // Get the coordinates where the user long pressed
                GeoPoint geoPoint = (GeoPoint) mapView.getProjection().fromPixels((int) e.getX(), (int) e.getY());

                // If there's an existing marker, remove it
                if (currentMarker != null) {
                    mapView.getOverlays().remove(currentMarker);
                }

                // Add a new marker at the long pressed position
                currentMarker = new Marker(mapView);
                currentMarker.setPosition(geoPoint);
                currentMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                currentMarker.setTitle("Marker at: " + geoPoint.getLatitude() + ", " + geoPoint.getLongitude());

                // Set a click listener to delete the marker when clicked
                currentMarker.setOnMarkerClickListener((marker, mapView) -> {
                    mapView.getOverlays().remove(marker);
                    currentMarker = null; // Clear the reference to the current marker
                    mapView.invalidate();
                    return true;
                });

                mapView.getOverlays().add(currentMarker);

                // Refresh the map view
                mapView.invalidate();

                // Optionally, show a toast message
            }
        });

        // Set the touch listener to use the GestureDetector
        mapView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
        */
        //mapView.removeMapListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mapView != null) {
            mapView.onDetach();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }
}