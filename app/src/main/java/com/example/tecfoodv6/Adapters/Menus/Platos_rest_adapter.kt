package com.example.tecfoodv6.Adapters.Menus

import android.content.res.AssetManager
import android.content.res.loader.AssetsProvider
import android.graphics.Color
import android.graphics.ColorSpace.get
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.ResourceManagerInternal.get
import androidx.recyclerview.widget.RecyclerView
import com.example.tecfoodv6.Models.Menus.Menu_platos
import com.example.tecfoodv6.R
import kotlinx.android.synthetic.main.item_plato_restaurant.*

class Platos_rest_adapter(val platos: ArrayList<Menu_platos>): RecyclerView.Adapter<Platos_rest_adapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombrePlato: TextView = itemView.findViewById(R.id.txt_name_plato)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_plato_restaurant, parent, false)

        val txtname = view.findViewById<TextView>(R.id.txt_name_plato)

        txtname.typeface = Typeface.createFromAsset(parent.context.assets, "fonts/DkCrayonCrumble.ttf")

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txtNombrePlato.text = platos[position].descripcion

        when(platos[position].tipo){
            "entrada" -> {
                holder.txtNombrePlato.setTextColor(Color.parseColor("#00FF74"))
            }
            "almuerzo" -> {
                holder.txtNombrePlato.setTextColor(Color.parseColor("#D100FF"))
            }
            "postre" -> {
                holder.txtNombrePlato.setTextColor(Color.parseColor("#00C1FF"))
            }
            "bebida" -> {
                holder.txtNombrePlato.setTextColor(Color.parseColor("#FBFF00"))
            }
        }

    }

    override fun getItemCount() = platos.size
}