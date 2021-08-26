package com.example.tecfoodv6.Activitys.Restaurant_details

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.fragment.app.FragmentTransaction
import com.example.tecfoodv6.Fragments.Main.Maps.MapsFragment
import com.example.tecfoodv6.Models.Restaurant.Restaurant
import com.example.tecfoodv6.R
import com.example.tecfoodv6.Utils.Conections.ApiTecFood.ApiConnection
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_restaurant.*
import kotlinx.android.synthetic.main.activity_restaurant_map.*
import kotlinx.android.synthetic.main.activity_restaurant_map.txt_contacto
import kotlinx.android.synthetic.main.activity_restaurant_map.txt_direccion
import kotlinx.android.synthetic.main.activity_restaurant_map.txt_distrito
import kotlinx.android.synthetic.main.activity_restaurant_map.txt_tiempo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Restaurant_map : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_map)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        val intent = intent
        val id_rest = intent.getStringExtra("id_rest")

        if (id_rest != null) {

            loadDataMapRest(id_rest.toInt())

        }else {
            Log.i("id de restaurante", "nose puedes hacer eres pendejo")
        }

        BottomSheetBehavior.from(sheet).apply {
            peekHeight = 200
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        Back_button.setOnClickListener {
            onBackPressed()
        }



    }

    fun loadDataMapRest(id: Int){
        val request = ApiConnection().getService().getRestaurantById(id).enqueue(object :
            Callback<Restaurant> {
            override fun onResponse(call: Call<Restaurant>, response: Response<Restaurant>) {
                val restauranteU = response.body()
                if (restauranteU != null) {

                    //collapsingToolbar.title = restauranteU.descripcion
                    txt_direccion.text = "Direccion:  " + restauranteU.direccion
                    txt_distrito.text = "Distrito: " + restauranteU.ubicacion
                    txt_tiempo.text = "Tiempo de llegada: " + "......"
                    txt_contacto.text = "Contacto: " + restauranteU.contacto

                    val mpa_rest = MapsFragment(restauranteU)

                    supportFragmentManager.beginTransaction().replace(R.id.fragment_map, mpa_rest).setTransition(
                        FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()

                }
            }

            override fun onFailure(call: Call<Restaurant>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }
}