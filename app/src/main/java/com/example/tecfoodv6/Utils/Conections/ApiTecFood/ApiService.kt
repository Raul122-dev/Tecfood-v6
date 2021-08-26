package com.example.tecfoodv6.Utils.Conections.ApiTecFood

import com.example.tecfoodv6.Models.Menus.Menu_info
import com.example.tecfoodv6.Models.Menus.Menu_platos
import com.example.tecfoodv6.Models.Register_Login.Login.LoginRequest
import com.example.tecfoodv6.Models.Register_Login.Login.LoginResponse
import com.example.tecfoodv6.Models.Register_Login.Register.User.RegisterRequest
import com.example.tecfoodv6.Models.Register_Login.Register.User.RegisterResponse
import com.example.tecfoodv6.Models.Restaurant.Restaurant
import com.example.tecfoodv6.Models.User.UserProfile
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    //--------------------------Get requests--------------------------//

    //Obtener Perfil_User
    @GET("Perfil/")
    fun getProfileUser(@Query("user") id: Int): Call<List<UserProfile>>

    //Obtener Restaurant
    @GET("Restaurante/{id}/")
    fun getRestaurantById(@Path("id") id: Int): Call<Restaurant>

    //Obtener Todos los Restaurantes
    @GET("Restaurante/")
    fun getAllRestaurants(): Call<List<Restaurant>>

    //Obtener Todos los menus
    @GET("Platos_Menus/")
    fun getAllPlatosMenus(): Call<List<Menu_platos>>

    //Obtener Menus de Restaurante
    @GET("Menus/")
    fun getMenusOfRestaurantById(@Query("restaurante") idRest: Int): Call<List<Menu_info>>


    //--------------------------Post requests--------------------------//

    //Login user
    @POST("login/")
    fun loginUser(@Body loginRequest: LoginRequest) : Call<LoginResponse>

    //Register user
    @POST("register/")
    fun registerUser(@Body registerRequest: RegisterRequest) : Call<RegisterResponse>

    //Register user profile
    @POST("Perfil/")
    fun registerProfileWithImageBody(@Body file : RequestBody): Call<ResponseBody>

}