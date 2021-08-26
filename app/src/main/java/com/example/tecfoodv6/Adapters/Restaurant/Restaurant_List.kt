package com.example.tecfoodv6.Adapters.Restaurant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tecfoodv6.Models.Restaurant.Restaurant
import com.example.tecfoodv6.R
import com.squareup.picasso.Picasso

class Restaurant_List(val restaurantes : ArrayList<Restaurant>) : RecyclerView.Adapter<Restaurant_List.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ImgRest: ImageView = itemView.findViewById(R.id.img_rest_list)
        val TxtNombre: TextView = itemView.findViewById(R.id.txt_name_rest_list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_restaurant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get()
            .load(restaurantes[position].vista)
            .fit()
            .error(R.drawable.ic_launcher_background)
            .into(holder.ImgRest)
        holder.TxtNombre.text = restaurantes[position].nombre_rest
    }

    override fun getItemCount() = restaurantes.size
}