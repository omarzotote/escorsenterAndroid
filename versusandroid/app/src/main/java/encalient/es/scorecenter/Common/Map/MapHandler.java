package encalient.es.scorecenter.Common.Map;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import encalient.es.scorecenter.R;
import es.encalient.ProtoMatchDTO;
import es.encalient.ProtoWeekDTO;

/**
 * Created by EnCalientes on 10/9/2015.
 */
public class MapHandler implements OnMapReadyCallback {

    ProtoMatchDTO.MatchDTO match;
    ProtoWeekDTO.WeekDTO week;
    GoogleMap mGoogleMap;
    Activity activity;

    public MapHandler(Activity _activity, ProtoMatchDTO.MatchDTO _match, ProtoWeekDTO.WeekDTO _week) {
        match = _match;
        week = _week;
        activity = _activity;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnMyLocationChangeListener(new LocationHandler());
    }

    private class LocationHandler implements GoogleMap.OnMyLocationChangeListener {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            LatLng fieldLocation = new LatLng(match.getField().getLatitude(), match.getField().getLongitude());

            CameraUpdate cameraStart = CameraUpdateFactory.newLatLngZoom(myLocation, 32);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLocation, 16);

            MarkerOptions fieldMarker = new MarkerOptions();
            fieldMarker.position(fieldLocation);
            fieldMarker.title(match.getField().getName());


            mGoogleMap.addMarker(fieldMarker);

            Routing routing = new Routing.Builder()
                    .travelMode(Routing.TravelMode.DRIVING)
                    .withListener(new RouteHandler())
                    .waypoints(myLocation, fieldLocation)
                    .build();
            routing.execute();

            mGoogleMap.moveCamera(cameraStart);
            mGoogleMap.animateCamera(cameraUpdate);
            mGoogleMap.setOnMyLocationChangeListener(null);
        }
    }

    private class RouteHandler implements RoutingListener {

        @Override
        public void onRoutingFailure() {

        }

        @Override
        public void onRoutingStart() {

        }

        @Override
        public void onRoutingSuccess(PolylineOptions polylineOptions, Route route) {

            PolylineOptions polyoptions = new PolylineOptions();
            polyoptions.color(activity.getResources().getColor(R.color.primaryColor));
            polyoptions.width(8);
            polyoptions.addAll(polylineOptions.getPoints());
            mGoogleMap.addPolyline(polyoptions);
        }

        @Override
        public void onRoutingCancelled() {

        }
    }
}
