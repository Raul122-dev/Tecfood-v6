package com.example.tecfoodv6.Activitys.StartApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.example.tecfoodv6.Activitys.Register_Login.Login
import com.example.tecfoodv6.Activitys.StartApp.Adapter.SliderAdapter
import com.example.tecfoodv6.Activitys.StartApp.Model.IntroSlide
import com.example.tecfoodv6.R
import kotlinx.android.synthetic.main.activity_start_app_slide.*

class StartApp_Slide : AppCompatActivity() {

    //ListOf Slide Views
    private val sliderAdapter = SliderAdapter(
        listOf(
            IntroSlide(
                "Bienvenido a TECFOOD",
                "Aqui va una brebe descripccion de lo que se va a presentar en esta pagina, por ahora no hay ni pincho ya que no se se sabe que chucha se va a poner",
                R.drawable.ic_launcher_background
            ),
            IntroSlide(
                "Bienvenido a TECFOOD",
                "Aqui va una brebe descripccion de lo que se va a presentar en esta pagina, por ahora no hay ni pincho ya que no se se sabe que chucha se va a poner",
                R.drawable.ic_launcher_background
            ),
            IntroSlide(
                "Bienvenido a TECFOOD",
                "Aqui va una brebe descripccion de lo que se va a presentar en esta pagina, por ahora no hay ni pincho ya que no se se sabe que chucha se va a poner",
                R.drawable.ic_launcher_background
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_app_slide)

        //Adapter Instance
        slideViewPager.adapter = sliderAdapter
        //Init Setup Indicators
        setupIndicators()
        //Init Indicator Value "0"
        setCurrentIndicator(0)
        //Action to change page
        slideViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //Change Indicator value
                setCurrentIndicator(position)
            }
        })

        //Button action next
        btn_next.setOnClickListener{
            if (slideViewPager.currentItem + 1 < sliderAdapter.itemCount){
                slideViewPager.currentItem += 1
            }else{
                Intent(applicationContext, Login::class.java).also{
                    startActivity(it)
                    finish()
                }
            }
        }

        //Button action skip
        txt_skip.setOnClickListener{
            Intent(applicationContext, Login::class.java).also{
                startActivity(it)
                finish()
            }
        }

    }

    private fun setupIndicators(){
        val indicators = arrayOfNulls<ImageView>(sliderAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8,0,8,0)
        for (i in indicators.indices){
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            indicatorsContainer.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index : Int){
        val childCount = indicatorsContainer.childCount
        for (i in 0 until childCount){
            val imageView = indicatorsContainer.get(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active
                    )
                )
            }else{
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }
}