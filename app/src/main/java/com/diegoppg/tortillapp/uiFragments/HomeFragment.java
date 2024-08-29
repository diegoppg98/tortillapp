package com.diegoppg.tortillapp.uiFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.diegoppg.tortillapp.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  Context ctx = requireActivity().getApplicationContext();
      //  Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    MapView mapView = null;
    GestureDetector gestureDetector;

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        File osmdroidBasePath = new File(requireActivity().getFilesDir(), "osmdroid");
        if (!osmdroidBasePath.exists()) {
            osmdroidBasePath.mkdirs();
        }
        Configuration.getInstance().setOsmdroidBasePath(osmdroidBasePath);
        Configuration.getInstance().setOsmdroidTileCache(new File(osmdroidBasePath, "tiles"));

        mapView = requireView().findViewById(R.id.mapview);
        mapView.setMultiTouchControls(true);

      //  mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        GeoPoint startPoint = new GeoPoint(43.53368, -5.64822); // Coordenadas del marcador
        mapView.getController().setZoom(15.0);
        mapView.getController().setCenter(startPoint);

        Marker marker = new Marker(mapView);
        marker.setPosition(startPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marker);

        gestureDetector = new GestureDetector(requireContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                // Get the coordinates where the user long pressed
                GeoPoint geoPoint = (GeoPoint) mapView.getProjection().fromPixels((int) e.getX(), (int) e.getY());

                // Add a marker at the long pressed position
                Marker marker = new Marker(mapView);

                // Set a click listener to delete the marker when clicked
                marker.setOnMarkerClickListener((marker1, mapView1) -> {
                    mapView.getOverlays().remove(marker1);
                    mapView.invalidate();
                    return true; // Returning true indicates that we have handled the click event
                });

                marker.setPosition(geoPoint);
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                marker.setTitle("Marker at: " + geoPoint.getLatitude() + ", " + geoPoint.getLongitude());
                mapView.getOverlays().add(marker);
                mapView.invalidate();

                // Optionally, show a toast message
                Log.w("marker", "Marcador añadido en: " + geoPoint.getLatitude() + ", " + geoPoint.getLongitude());
            }
        });

        mapView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

/*
        mapView.setOnTouchListener((v, event) -> {
            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                // Get the coordinates where the user touched
                GeoPoint geoPoint = (GeoPoint) mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY());

                Log.w("marker", "Marcador añadido en: " + geoPoint.getLatitude() + ", " + geoPoint.getLongitude());

                // Add a marker at the touched position
                Marker marker2 = new Marker(mapView);
                marker2.setPosition(geoPoint);
                marker2.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                marker2.setTitle("Marker at: " + geoPoint.getLatitude() + ", " + geoPoint.getLongitude());
                mapView.getOverlays().add(marker2);
                mapView.invalidate();

                // Optionally, show a toast message
                //Toast.makeText(this, "Marker added at: " + geoPoint.getLatitude() + ", " + geoPoint.getLongitude(), Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        */


/*
        mapView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.w("marker", "adios");

                // Obtiene la ubicación geográfica donde se hizo el clic
                GeoPoint geoPoint = (GeoPoint) mapView.getProjection().fromPixels(
                        (int) mapView.getHandler().obtainMessage().arg1,
                        (int) mapView.getHandler().obtainMessage().arg2);

                Log.w("marker", "Marcador añadido en: " + geoPoint.getLatitude() + ", " + geoPoint.getLongitude());

                // Añadir un marcador en esa ubicación
                Marker marker = new Marker(mapView);
                marker.setPosition(geoPoint);
                marker.setTitle("Nuevo Marcador");
                marker.setSnippet("Lat: " + geoPoint.getLatitude() + " Lon: " + geoPoint.getLongitude());
                mapView.getOverlays().add(marker);
                mapView.invalidate(); // Refresca el mapa para mostrar el marcador

                // Opcional: Mostrar un mensaje al usuario
                Toast.makeText(requireContext(), "Marcador añadido en: " + geoPoint.getLatitude() + ", " + geoPoint.getLongitude(), Toast.LENGTH_SHORT).show();

                return true;
            }
        });
        */




/*
        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(requireContext()), mapView);
        mLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(mLocationOverlay);


        //Click listener
        //your items
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        items.add(new OverlayItem("Title", "Description", new GeoPoint(38.9079, -77.0362))); // Lat/Lon decimal degrees

        //the overlay
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        //do something
                        return true;
                    }
                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                }, requireContext());
        mOverlay.setFocusItemsOnTap(true);

        mapView.getOverlays().add(mOverlay);
        */
    }







    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }    }



}