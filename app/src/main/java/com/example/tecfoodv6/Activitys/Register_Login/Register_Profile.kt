package com.example.tecfoodv6.Activitys.Register_Login

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.tecfoodv6.Activitys.Main.MainActivity
import com.example.tecfoodv6.R
import com.example.tecfoodv6.Utils.Conections.ApiTecFood.ApiConnection
import com.example.tecfoodv6.Utils.GetRealURI.RealUri
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class Register_Profile : AppCompatActivity() {

    private lateinit var dataUri: Uri

    lateinit var id_user : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_profile)

        val intent = intent

        if (intent != null){
            id_user = intent.getStringExtra("id_user").toString()
        }else{
            Toast.makeText(this, "Uno un problema con la creacion del usuario", Toast.LENGTH_SHORT).show()
        }

        btnImageSelect.setOnClickListener {
            requestPermission()
        }

        btnProfileRegister.setOnClickListener{

            val realPATH = RealUri()

            val district = eDistrict.text.toString().trim()
            val favoritePlate = ePlateFav.text.toString().trim()
            val number = eNumberPhone.text.toString().trim()
            val uriImage = dataUri?.let { realPATH.getPathFromUri(applicationContext, it) }
            val file = File(uriImage)


            if (district.isEmpty() && favoritePlate.isEmpty() && number.isEmpty()){
                Toast.makeText(this, "Llenar los campos", Toast.LENGTH_SHORT).show()
            }else{

                val builder : MultipartBody.Builder = MultipartBody.Builder()

                builder.setType(MultipartBody.FORM)

                builder.addFormDataPart("user", id_user)
                builder.addFormDataPart("distrito", district)
                builder.addFormDataPart("plato_fav",favoritePlate)
                builder.addFormDataPart("phone_number", number)
                builder.addFormDataPart("picture", file.name, RequestBody.create("image/*".toMediaTypeOrNull(), file))

                val requestB : MultipartBody = builder.build()

                val request = ApiConnection().getService().registerProfileWithImageBody(requestB)
                request.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        Log.i("GOOD", "GOOD")
                        //Log.i("response", response.toString())
                        //Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_SHORT).show()

                        Intent(applicationContext, MainActivity::class.java).also{
                            startActivity(it)
                            finish()
                        }

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        t.printStackTrace()
                        Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_SHORT).show()
                    }

                })

            }

        }

        btnMasTarde.setOnClickListener{

            val snackbar : Snackbar = Snackbar.make(LoginLayoutView,"Podra editar su perfil en cualquier momento", Snackbar.LENGTH_SHORT)

            snackbar.show()

            Intent(applicationContext, MainActivity::class.java).also{
                startActivity(it)
                finish()
            }

        }

    }

    //----------------------- Open Gallery --------------------------------//
    private fun requestPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    pickPhotoFromGalery()
                }

                else -> requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        }else{
            pickPhotoFromGalery()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){isGranted->

        if(isGranted){
            pickPhotoFromGalery()
        }else{
            Toast.makeText(this, "Necesitas los permisos necesarios", Toast.LENGTH_SHORT).show()
        }

    }

    @SuppressLint("NewApi")
    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result->

        if(result.resultCode == RESULT_OK){
            dataUri = result.data?.data!!
            ImgSelected.setImageURI(dataUri)

            Log.i("URLdataOnResult", dataUri.toString())

        }
    }

    private fun pickPhotoFromGalery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }
}