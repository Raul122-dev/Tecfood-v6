package com.example.tecfoodv6.Activitys.StartApp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tecfoodv6.Activitys.StartApp.Model.IntroSlide
import com.example.tecfoodv6.R

class SliderAdapter(private val introSlides : List<IntroSlide>): RecyclerView.Adapter<SliderAdapter.IntroSliderViewHolder>(){

    inner class IntroSliderViewHolder(view: View) : RecyclerView.ViewHolder(view){

        private val textTitle = view.findViewById<TextView>(R.id.txt_slide)
        private val textDescription = view.findViewById<TextView>(R.id.subtxt_slide)
        private val imageIcon = view.findViewById<ImageView>(R.id.img_slide)

        fun bind(introSlide: IntroSlide) {
            textTitle.text = introSlide.title
            textDescription.text = introSlide.description
            imageIcon.setImageResource(introSlide.icon)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSliderViewHolder {
        return  IntroSliderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.startapp_slide_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: IntroSliderViewHolder, position: Int) {
        holder.bind(introSlides[position])
    }

    override fun getItemCount(): Int {
        return introSlides.size
    }

}