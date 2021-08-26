package com.example.tecfoodv6.Activitys.Restaurant_details

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tecfoodv6.Adapters.Menus.Platos_rest_adapter
import com.example.tecfoodv6.Models.Menus.Menu_info
import com.example.tecfoodv6.Models.Menus.Menu_platos
import com.example.tecfoodv6.Models.Restaurant.Restaurant
import com.example.tecfoodv6.R
import com.example.tecfoodv6.Utils.Conections.ApiTecFood.ApiConnection
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_restaurant.*
import kotlinx.android.synthetic.main.item_plato_restaurant.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Restaurant_view : AppCompatActivity() {

    var isAllFABVisible : Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)

        //FullView Screen
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        setSupportActionBar(toolbarLayout)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        val id_rest = intent.getStringExtra("id_rest")

        if (id_rest != null) {

            loadDataRestView(id_rest.toInt())

        }else {
            Log.i("id de restaurante", "nose puedes hacer eres pendejo")
        }

        title_entrada.typeface = Typeface.createFromAsset(assets, "fonts/DkCrayonCrumble.ttf")
        title_almuerzo.typeface = Typeface.createFromAsset(assets, "fonts/DkCrayonCrumble.ttf")
        title_postre.typeface = Typeface.createFromAsset(assets, "fonts/DkCrayonCrumble.ttf")
        title_bebida.typeface = Typeface.createFromAsset(assets, "fonts/DkCrayonCrumble.ttf")

        //Loading ImageView Restaurant View
        Picasso.get()
            .load("https://i.ytimg.com/vi/4bemhUE3ww0/maxresdefault.jpg")
            .into(img_promo_rest)

        Picasso.get()
            .load("https://dam.cocinafacil.com.mx/wp-content/uploads/2018/10/sopas-cremas-caldos.jpg")
            .into(img_entrada)

        Picasso.get()
            .load("https://peru.info/Portals/0/Images/Productos/6/146-imagen-181232682020.jpg")
            .into(img_almuerzo)

        Picasso.get()
            .load("https://i.ytimg.com/vi/SP2GiAO05x0/maxresdefault.jpg")
            .into(img_postre)

        Picasso.get()
            .load("https://www.gastrolabweb.com/u/fotografias/m/2020/11/7/f638x638-4905_63072_5050.jpg")
            .into(img_bebida)

        //Floating Buttons
        add_alarm.visibility = View.GONE
        add_person.visibility = View.GONE
        add_alarm_text.visibility = View.GONE
        add_person_text.visibility = View.GONE

        add_fab.shrink()

        add_fab.setOnClickListener{
            if (!isAllFABVisible){
                add_alarm.show()
                add_person.show()
                add_alarm_text.visibility = View.VISIBLE
                add_person_text.visibility = View.VISIBLE

                add_fab.extend()

                isAllFABVisible = true
            }else{
                add_alarm.hide()
                add_person.hide()
                add_alarm_text.visibility = View.GONE
                add_person_text.visibility = View.GONE

                add_fab.shrink()

                isAllFABVisible = false
            }
        }

        add_alarm.setOnClickListener{
            Toast.makeText(this, "Person Added", Toast.LENGTH_SHORT).show()
        }
        add_alarm.setOnClickListener{
            Toast.makeText(this, "Alarm Added", Toast.LENGTH_SHORT).show()
        }

        btn_map.setOnClickListener {
            Intent(applicationContext, Restaurant_map::class.java)
                .putExtra("id_rest", id_rest)
                .also{
                startActivity(it)
                //finish()
            }
        }

    }

    fun loadDataRestView(id: Int){
        val request = ApiConnection().getService().getRestaurantById(id).enqueue(object :
            Callback<Restaurant> {
            override fun onResponse(call: Call<Restaurant>, response: Response<Restaurant>) {
                val restauranteU = response.body()
                if (restauranteU != null) {
                    Log.i("REtsU", restauranteU.toString())

                    collapsingToolbar.title = restauranteU.nombre_rest
                    txt_direccion.text = "Direccion:  " + restauranteU.direccion
                    txt_distrito.text = "Distrito: " + restauranteU.ubicacion
                    txt_tiempo.text = "Tiempo de llegada: " + "......"
                    txt_contacto.text = "Contacto: " + restauranteU.contacto
                    Picasso.get().load(restauranteU.vista).error(R.drawable.ic_launcher_background).fit().centerCrop().into(img_top_bar)

                    data_info_menu(restauranteU.id)
                }
            }

            override fun onFailure(call: Call<Restaurant>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    fun data_info_menu(id: Int){
        val request = ApiConnection().getService().getMenusOfRestaurantById(id).enqueue(object :
            Callback<List<Menu_info>> {
            override fun onResponse(
                call: Call<List<Menu_info>>,
                response: Response<List<Menu_info>>
            ) {
                val menu_info_rest = response.body()
                if (menu_info_rest != null){

                    Log.i("REts", menu_info_rest.toString())

                    val id = menu_info_rest[0].id
                    val dia = menu_info_rest[0].dia
                    val total = menu_info_rest[0].total
                    val precio_almuerzo = menu_info_rest[0].precio_almuerzo
                    val restaurante = menu_info_rest[0].restaurante

                    txt_dia.text = "Menu del dia " + dia
                    txt_total.text = "Precio de Total: S/" + total
                    txt_almuerzo.text ="Precio del Almuerzo: S/" + precio_almuerzo

                    data_platos_menu(id)

                }
            }

            override fun onFailure(call: Call<List<Menu_info>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun data_platos_menu(restaurante: Int){

        val platos_rest: ArrayList<Menu_platos> = ArrayList()
        val entradas : ArrayList<Menu_platos> = ArrayList()
        val almuerzos : ArrayList<Menu_platos> = ArrayList()
        val postres : ArrayList<Menu_platos> = ArrayList()
        val bebidas : ArrayList<Menu_platos> = ArrayList()

        Rc_platos_entrada.layoutManager = LinearLayoutManager(this)
        Rc_platos_almuerzos.layoutManager = LinearLayoutManager(this)
        Rc_platos_postres.layoutManager = LinearLayoutManager(this)
        Rc_platos_bebidas.layoutManager = LinearLayoutManager(this)

        val request = ApiConnection().getService().getAllPlatosMenus().enqueue(object: Callback<List<Menu_platos>>{
            override fun onResponse(
                call: Call<List<Menu_platos>>,
                response: Response<List<Menu_platos>>
            ) {
                val menu_platos_rest = response.body()

                if (menu_platos_rest != null){

                    for (i in 0 until menu_platos_rest.size){

                        if (menu_platos_rest[i].registro == restaurante){

                            platos_rest.add(menu_platos_rest[i])

                        }

                    }

                    for (i in 0 until platos_rest.size){

                        when(platos_rest[i].tipo){
                            "entrada" -> {
                                entradas.add(platos_rest[i])
                            }
                            "almuerzo" -> {
                                almuerzos.add(platos_rest[i])
                            }
                            "postre" -> {
                                postres.add(platos_rest[i])
                            }
                            "bebida" -> {
                                bebidas.add(platos_rest[i])
                            }
                        }

                    }

                    Rc_platos_entrada.adapter = Platos_rest_adapter(entradas)
                    Rc_platos_almuerzos.adapter = Platos_rest_adapter(almuerzos)
                    Rc_platos_postres.adapter = Platos_rest_adapter(postres)
                    Rc_platos_bebidas.adapter = Platos_rest_adapter(bebidas)

                }
            }

            override fun onFailure(call: Call<List<Menu_platos>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}