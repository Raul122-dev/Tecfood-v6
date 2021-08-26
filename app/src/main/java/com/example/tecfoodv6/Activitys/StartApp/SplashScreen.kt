package com.example.tecfoodv6.Activitys.StartApp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.ProgressBar
import com.example.tecfoodv6.Activitys.Register_Login.Login
import com.example.tecfoodv6.R
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.util.*

class SplashScreen : AppCompatActivity() {

    var count: Int = 0
    lateinit var pb : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //WindowFullView Activity
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Login::class.java)
            this.startActivity(intent)
            finish()
        }, 3000)

    }
    /*
    fun ProgresiveBar(){
        //Intance Progress Bar
        pb = progress_bar

        //Intance Timer
        val t = Timer()

        //Star TimerTask
        val tt: TimerTask = object : TimerTask() {
            override fun run() {
                count++
                pb.progress = count
                if (count == 100) t.cancel()
            }
        }

        //Timer Options
        t.schedule(tt, 0, 25)
    }*/
}