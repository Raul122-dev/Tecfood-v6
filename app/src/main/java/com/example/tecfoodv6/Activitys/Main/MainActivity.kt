package com.example.tecfoodv6.Activitys.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.tecfoodv6.Fragments.Main.List_Restaurants.Explore.Menus.Explore_Main
import com.example.tecfoodv6.Fragments.Main.MainHome.Home
import com.example.tecfoodv6.Fragments.Main.Preferences.Preferences
import com.example.tecfoodv6.Models.User.UserProfile
import com.example.tecfoodv6.R
import com.example.tecfoodv6.Utils.Conections.ApiTecFood.ApiConnection
import com.example.tecfoodv6.Utils.SessionManager.LoginPreferences
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_lateral_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var username: String

    lateinit var session: LoginPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        session = LoginPreferences(this)

        val navigationView: NavigationView = findViewById(R.id.menu_navegacion)
        navigationView.setNavigationItemSelectedListener(this)

        //Start initial fragment
        fragmentShow(Home(), "")

        //Open Lateral Menu
        btn_menu.setOnClickListener(){
            navigation.openDrawer(GravityCompat.START)

            btnInviteFriends.setOnClickListener{
                Toast.makeText(this, "Link de invitacion copiado", Toast.LENGTH_LONG).show()
                navigation.closeDrawer(GravityCompat.START)
            }


            btn_menu_hacerse_premium.setOnClickListener(View.OnClickListener {
                fragmentShow(Preferences(), "")

                bottomNavigation.show(2, true)

                navigation.closeDrawer(GravityCompat.START)
            })

        }

        //Functionality bottom Menu
        //Start menu options
        bottomNavigation.add(MeowBottomNavigation.Model(0, R.drawable.ic_baseline_home_24))
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.ic_baseline_apps_24))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.ic_baseline_account_box_24))
        //Start selected option
        bottomNavigation.show(0, true)
        //Functionality of the options
        bottomNavigation.setOnClickMenuListener {
            when(it.id){
                0 -> {
                    fragmentShow(Home(), "left")
                }
                1 -> {
                    fragmentShow(Explore_Main(), "center")
                }
                2 -> {
                    fragmentShow(Preferences(), "right")
                }

            }
        }



        //Load info User profile

        if (session.isLoggedIn()){

            val user: HashMap<String, String> = session.getUserDetails()

            username = user[LoginPreferences.KEY_USERNAME].toString()
            val email = user[LoginPreferences.KEY_EMAIL]
            val userId = user[LoginPreferences.KEY_ID]

            //user_txt_name.text = "nombre"

            loadDataUser(userId!!.toInt())

        }


    }

    fun loadDataUser (id : Int){
        val request = ApiConnection().getService().getProfileUser(id).enqueue(object : Callback<List<UserProfile>>{
            override fun onResponse(
                call: Call<List<UserProfile>>,
                response: Response<List<UserProfile>>
            ) {
                val userData = response.body()

                Picasso.get()
                    .load(userData!![0].picture)
                    .error(R.drawable.ic_launcher_background)
                    .into(user_img_profile)

                user_txt_name.text = username
                user_txt_info.text = "Plato favorito: " + userData!![0].plato_fav

                Picasso.get()
                    .load("https://www.nawpic.com/media/2020/desktop-backgrounds-nawpic-1.png")
                    .error(R.drawable.ic_launcher_background)
                    .into(background_header)

                Picasso.get()
                    .load("https://3.bp.blogspot.com/-aK64iMaevMk/XJvAHsOm-iI/AAAAAAAAM-4/waToFo4Gd8YAXG_eIz1y2Zp1ZhrzkFuPwCLcBGAs/s1600/Publicidad%2BMovil.jpg")
                    .error(R.drawable.ic_launcher_background)
                    .into(btnInviteFriends)

                Picasso.get()
                    .load("https://www.pngkit.com/png/full/201-2013815_estrella-amarilla-rate-star-icon.png")
                    .error(R.drawable.ic_launcher_background)
                    .into(back_haz_pre)

            }

            override fun onFailure(call: Call<List<UserProfile>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            //opciones menu lateral
            //borramos invitacion y hacerse premium
            R.id.menuDireccion -> {
                /*
                val nextActivity = Intent(applicationContext, MenuLat_Direccion::class.java)
                startActivity(nextActivity)*/
            }
            R.id.menuMisNotificaciones -> {
                /*val nextActivity = Intent(applicationContext, MenuLat_Notificaciones::class.java)
                startActivity(nextActivity)*/
            }
            R.id.menuAfiliarRestaurante -> {
                /*
                val nextActivity = Intent(applicationContext, MenuLat_AfiliarRestaurante::class.java)
                startActivity(nextActivity)*/
            }
            R.id.menuAcercaDeTecFood -> {
                /*val nextActivity = Intent(applicationContext, MenuLat_InfoTecfood::class.java)
                startActivity(nextActivity)*/
            }
            R.id.menuSalirAplicacion -> {
                session.LogoutUser()
            }
        }
        return true
    }

    fun fragmentShow(fragment: Fragment, position: String) {
        when (position){
            "right" -> {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left)
                    .replace(R.id.Page_main, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()
            }
            "center" -> {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_bot_to_top, R.anim.exit_top_to_bot)
                    .replace(R.id.Page_main, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()
            }
            "left" -> {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                    .replace(R.id.Page_main, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()
            }
            "" -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.Page_main, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()
            }
        }
    }


}


