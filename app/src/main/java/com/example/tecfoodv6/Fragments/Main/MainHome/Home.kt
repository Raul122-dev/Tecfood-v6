package com.example.tecfoodv6.Fragments.Main.MainHome

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.tecfoodv6.Adapters.Restaurant.Restaurant_Cerca
import com.example.tecfoodv6.Adapters.Restaurant.User_Restaurant
import com.example.tecfoodv6.Models.Restaurant.Restaurant
import com.example.tecfoodv6.Models.Restaurant.Restaurant_Dist
import com.example.tecfoodv6.R
import com.example.tecfoodv6.Utils.Conections.ApiTecFood.ApiConnection
import com.example.tecfoodv6.Utils.SlideImage.Adapter.SliderImageAdapter
import com.example.tecfoodv6.Utils.SlideImage.Model.SliderItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.slider.Slider
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var rest_group = ArrayList<Restaurant>()

    // variables Slider Image View
    private lateinit var viewPager2: ViewPager2
    private val sliderHandler = Handler()

    // variables de ubicacion
    var jso: JSONObject? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("WrongConstant", "MissingPermission", "UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_home, container, false)

        val Rc_user = view.findViewById<RecyclerView>(R.id.Rc_user)

        val Rc_cerca = view.findViewById<RecyclerView>(R.id.Rc_cerca_user)

        //-------------- Slider Restaurant Image --------------//
        viewPager2 = view.findViewById<ViewPager2>(R.id.viewPager_ImageSlide)

        val sliderItems: MutableList<SliderItem> = ArrayList()

        sliderItems.add(SliderItem(1, "http://images3.memedroid.com/images/UPLOADED201/5b19e5b08f554.jpeg"))
        sliderItems.add(SliderItem(2, "https://www.muycomputer.com/wp-content/uploads/2021/06/Monterey-1.jpg"))
        sliderItems.add(SliderItem(3, "https://wallpaperaccess.com/full/3097725.jpg"))
        /*
        sliderItems.add(SliderItem(R.drawable.rest1))
        sliderItems.add(SliderItem(R.drawable.rest2))
        sliderItems.add(SliderItem(R.drawable.rest3))*/



        viewPager2.adapter = SliderImageAdapter(sliderItems, viewPager2)

        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.offscreenPageLimit = 3
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(30))
        compositePageTransformer.addTransformer {page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.25f
        }

        viewPager2.setPageTransformer(compositePageTransformer)

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 3000)
            }
        })
        //-------------- Slider Restaurant Image --------------//


        //RC Restaurant User
        Rc_user.layoutManager = LinearLayoutManager(activity, OrientationHelper.HORIZONTAL, false)

        //RC Restaurant cerca
        Rc_cerca.layoutManager = LinearLayoutManager(activity, OrientationHelper.HORIZONTAL, false)

        val request = ApiConnection().getService().getAllRestaurants().enqueue(object : Callback<List<Restaurant>>{
            override fun onResponse(
                call: Call<List<Restaurant>>,
                response: Response<List<Restaurant>>
            ) {
                rest_group = response.body() as ArrayList<Restaurant>
                //Log.i("restaurantes", restaurants.toString())
                Rc_user.adapter = User_Restaurant(rest_group )
            }

            override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        var new_restaurant_group = ArrayList<Restaurant_Dist>()

        var durationtext: String = ""

        var latitud_user = 0.0

        var longitude_user = 0.0

        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)

            fusedLocationClient.lastLocation.addOnSuccessListener(activity!!) { location ->

                if (location != null){

                    //lastLocation = location
                    //val currentLatLong = LatLng(location.latitude, location.longitude)

                    latitud_user = location.latitude
                    longitude_user = location.longitude

                    val queue = Volley.newRequestQueue(activity)


                    for (i in 0 until rest_group.size){

                        val url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + latitud_user + "," + longitude_user + "&destination=" + rest_group[i].latitude + "," + rest_group[i].longitude + "&key=AIzaSyBvicfZI6XGmHCCpJFzdmabInHJkV1meWg"

                        val jsonObjectRequest = JsonObjectRequest(url, null,
                            { response ->
                                try {
                                    jso = response

                                    val jRoutes: JSONArray
                                    var jLegs: JSONArray
                                    var jDuration: JSONObject

                                    jRoutes = jso!!.getJSONArray("routes")

                                    for (k in 0 until jRoutes.length()){
                                        jLegs = (jRoutes[k] as JSONObject).getJSONArray("legs")
                                        for (j in 0 until jLegs.length()) {
                                            val jo: JSONObject = jLegs.getJSONObject(i)
                                            jDuration = jo.getJSONObject("duration")
                                            durationtext = jDuration.getString("text")
                                        }
                                    }

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

                        new_restaurant_group.add(
                            Restaurant_Dist(
                                rest_group[i].id,
                                rest_group[i].nombre_rest,
                                rest_group[i].puntuacion,
                                rest_group[i].latitude,
                                rest_group[i].longitude,
                                rest_group[i].vista,
                                durationtext
                            )
                        )

                    }

                    Log.i("groupRest", new_restaurant_group.toString())

                    Log.i("Ubiacion",latitud_user.toString() + "////" + longitude_user.toString())

                    Rc_cerca.adapter = Restaurant_Cerca(new_restaurant_group)

                }

            }

        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 123);
        }


        return view
    }

    private val sliderRunnable = Runnable{
        viewPager2.currentItem = viewPager2.currentItem + 1
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }

    private fun rduration(jso: JSONObject) : String{
        val jRoutes: JSONArray
        var jLegs: JSONArray
        var jDuration: JSONObject
        var duration : String

        jRoutes = jso.getJSONArray("routes")

        for (i in 0 until jRoutes.length()){
            jLegs = (jRoutes[i] as JSONObject).getJSONArray("legs")
            for (j in 0 until jLegs.length()) {
                val jo: JSONObject = jLegs.getJSONObject(i)
                jDuration = jo.getJSONObject("duration")
                duration = jDuration.getString("text")
            }

        }

        return ""

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}