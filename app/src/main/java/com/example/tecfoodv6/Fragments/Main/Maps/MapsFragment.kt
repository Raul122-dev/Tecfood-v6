package com.example.tecfoodv6.Fragments.Main.Maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.tecfoodv6.Models.Restaurant.Restaurant
import com.example.tecfoodv6.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class MapsFragment(val restaurante : Restaurant) : Fragment() {

    private lateinit var mMap: GoogleMap

    var jso: JSONObject? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    var marcadorDestino: Marker? = null

    val rest1 = restaurante

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        mMap = googleMap

        googleMap.uiSettings.setAllGesturesEnabled(true)

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true;
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 123);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->

            if (location != null){

                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 13f))

                val latitud_user = location.latitude
                val longitude_user = location.longitude

                marcadorDestino = mMap.addMarker(
                    MarkerOptions()
                        .position(currentLatLong)
                        .title("Yo estoy Aqui")
                )

                val queue = Volley.newRequestQueue(activity)
                val url = "https://maps.googleapis.com/maps/api/directions/json?origin="+latitud_user+","+longitude_user+"&destination="+rest1.latitude+","+rest1.longitude+"&key=AIzaSyBvicfZI6XGmHCCpJFzdmabInHJkV1meWg"
                val jsonObjectRequest = JsonObjectRequest(url, null,
                    { response ->
                        try {
                            jso = response
                            trazarRuta(jso!!)
                            Log.i("jsonRuta: ", "" + response)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    },
                    {
                        Toast.makeText(
                            activity,
                            "Problemas al obtener datos",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
                queue.add(jsonObjectRequest)

            }

        }



        val location = LatLng(restaurante.latitude.toDouble(), restaurante.longitude.toDouble())
        googleMap.addMarker(MarkerOptions().position(location).title(restaurante.nombre_rest))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 20f))

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_maps, container, false)

        // Get the button view
        val locationButton = (view.findViewById<View>(Integer.parseInt ("1")).parent as View).findViewById<View>(Integer.parseInt ("2"))

        // and next place it, for exemple, on bottom right (as Google Maps app)
        val rlp = locationButton.layoutParams as RelativeLayout.LayoutParams
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0)
        rlp.setMargins(0, 55, 55, 0)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun trazarRuta(jso: JSONObject) {
        val jRoutes: JSONArray
        var jLegs: JSONArray
        var jSteps: JSONArray
        try {
            jRoutes = jso.getJSONArray("routes")
            for (i in 0 until jRoutes.length()) {
                jLegs = (jRoutes[i] as JSONObject).getJSONArray("legs")
                for (j in 0 until jLegs.length()) {
                    jSteps = (jLegs[j] as JSONObject).getJSONArray("steps")
                    for (k in 0 until jSteps.length()) {
                        val polyline = "" + ((jSteps[k] as JSONObject)["polyline"] as JSONObject)["points"]
                        Log.i("end", "" + polyline)
                        val list = PolyUtil.decode(polyline)
                        mMap.addPolyline(PolylineOptions().addAll(list).color(Color.RED).width(10f))
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


}