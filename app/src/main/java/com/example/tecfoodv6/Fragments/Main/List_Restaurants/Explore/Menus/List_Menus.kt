package com.example.tecfoodv6.Fragments.Main.List_Restaurants.Explore.Menus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.tecfoodv6.Adapters.Restaurant.Restaurant_List
import com.example.tecfoodv6.Adapters.Restaurant.User_Restaurant
import com.example.tecfoodv6.Models.Restaurant.Restaurant
import com.example.tecfoodv6.R
import com.example.tecfoodv6.Utils.Conections.ApiTecFood.ApiConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [List_Menus.newInstance] factory method to
 * create an instance of this fragment.
 */
class List_Menus : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val  view : View = inflater.inflate(R.layout.fragment_list__menus, container, false)

        val RcRestaurantes = view.findViewById<RecyclerView>(R.id.rc_menu_list)

        RcRestaurantes.layoutManager =  LinearLayoutManager(activity)

        val request = ApiConnection().getService().getAllRestaurants().enqueue(object :
            Callback<List<Restaurant>> {
            override fun onResponse(
                call: Call<List<Restaurant>>,
                response: Response<List<Restaurant>>
            ) {
                val restaurants = response.body() as ArrayList<Restaurant>
                RcRestaurantes.adapter = Restaurant_List(restaurants)
            }

            override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment List_Menus.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            List_Menus().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}