package com.example.tecfoodv6.Adapters.Restaurant

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tecfoodv6.Activitys.Restaurant_details.Restaurant_view
import com.example.tecfoodv6.Models.Restaurant.Restaurant
import com.example.tecfoodv6.R
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso

class User_Restaurant(val restaurantes: ArrayList<Restaurant>) : RecyclerView.Adapter<User_Restaurant.ViewHolder>()  {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imgRest: ImageView = itemView.findViewById(R.id.img_rest)
        val txtNombre: TextView = itemView.findViewById(R.id.txt_name_rest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val transformation = RoundedTransformationBuilder()
            .cornerRadiusDp(12F)
            .oval(false)
            .build()

        holder.txtNombre.text = restaurantes[position].nombre_rest
        Picasso.get()
            .load(restaurantes[position].vista)
            .transform(transformation)
            .fit()
            .error(R.drawable.ic_launcher_background)
            .into(holder.imgRest)

        holder.itemView.setOnClickListener(){
            var context = holder.itemView.getContext()
            val id_rest = (restaurantes[position].id).toString()
            val intent = Intent(context, Restaurant_view::class.java)
            intent.putExtra("id_rest", id_rest)

            context.startActivity(intent)
        }
    }

    override fun getItemCount() = restaurantes.size
}
