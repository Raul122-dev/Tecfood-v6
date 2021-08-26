package com.example.tecfoodv6.Utils.SlideImage.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.tecfoodv6.R
import com.example.tecfoodv6.Utils.SlideImage.Model.SliderItem
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso

class SliderImageAdapter internal constructor(
    sliderItems: MutableList<SliderItem>,
    viewPager2: ViewPager2
): RecyclerView.Adapter<SliderImageAdapter.SliderViewHolder>(){

    private val sliderItems: List<SliderItem>
    private val viewPager2: ViewPager2

    init {
        this.sliderItems = sliderItems
        this.viewPager2 = viewPager2
    }

    class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val imageView: RoundedImageView = itemView.findViewById(R.id.imageSlide)

        fun image(sliderItem: SliderItem){
            //imageView.setImageResource(sliderItem.image)

            Picasso.get()
                .load(sliderItem.image)
                .fit()
                .error(R.drawable.ic_launcher_background)
                .into(imageView)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.slide_image_container,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.image(sliderItems[position])
        if (position == sliderItems.size - 2){
            viewPager2.post(runnable)
        }

        holder.itemView.setOnClickListener(){
            var context = holder.itemView.getContext()
            Toast.makeText(context, "preciono la imagen " + sliderItems[position].image.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }

    private val runnable = Runnable {
        sliderItems.addAll(sliderItems)
        notifyDataSetChanged()
    }
}