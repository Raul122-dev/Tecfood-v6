package com.example.tecfoodv6.Activitys.Register_Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.tecfoodv6.Activitys.Main.MainActivity
import com.example.tecfoodv6.Models.Register_Login.Login.LoginRequest
import com.example.tecfoodv6.Models.Register_Login.Login.LoginResponse
import com.example.tecfoodv6.Models.Register_Login.Register.User.RegisterRequest
import com.example.tecfoodv6.Models.Register_Login.Register.User.RegisterResponse
import com.example.tecfoodv6.R
import com.example.tecfoodv6.Utils.Conections.ApiTecFood.ApiConnection
import com.example.tecfoodv6.Utils.SessionManager.LoginPreferences
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    lateinit var session : LoginPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        session = LoginPreferences(this)

        if (session.isLoggedIn()){
            val i  = Intent(applicationContext, MainActivity::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
            finish()

        }

        btnRegisterView.setOnClickListener{
            btnRegisterView.background = resources.getDrawable(R.drawable.swicth_trcks, null)
            btnRegisterView.setTextColor(resources.getColor(R.color.white,null))
            btnLoginView.background = null
            RegisterLayout.visibility = View.VISIBLE
            LoginLayout.visibility = View.GONE
            txtOr.visibility = View.VISIBLE
            btnLogInPhone.visibility = View.VISIBLE
            btnLoginView.setTextColor(resources.getColor(R.color.principal,null))

            btnLogIn.text = "Registrarse"
        }

        btnLoginView.setOnClickListener{
            btnRegisterView.background = null
            btnRegisterView.setTextColor(resources.getColor(R.color.principal,null))
            btnLoginView.background = resources.getDrawable(R.drawable.swicth_trcks, null)
            RegisterLayout.visibility = View.GONE
            LoginLayout.visibility = View.VISIBLE
            txtOr.visibility = View.GONE
            btnLogInPhone.visibility = View.GONE
            btnLoginView.setTextColor(resources.getColor(R.color.white, null))

            btnLogIn.text = "Iniciar Sesion"
        }

        txt_login_invitado.setOnClickListener(){
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        btnLogInPhone.setOnClickListener(){
            Intent(this, MainActivity::class.java).also{
                startActivity(it)
                finish()
            }
        }

        btnLogIn.setOnClickListener{

            if (btnLogIn.text == "Iniciar Sesion"){

                val username = eMail.text.toString().trim()
                val password = password.text.toString().trim()

                if(username.isEmpty() && password.isEmpty()) {

                    //Toast.makeText(this, "LLene todos los campos", Toast.LENGTH_SHORT).show()

                    val snackbar : Snackbar = Snackbar.make(LoginLayoutView,"Llene todos los campos", Snackbar.LENGTH_SHORT)

                    snackbar.show()

                }else{

                    val loginRequest = LoginRequest(username, password)

                    login_User(loginRequest)

                }

            }else if (btnLogIn.text == "Registrarse"){

                val username = eUserreg.text.toString().trim()
                val email = eMailreg.text.toString().trim()
                val names = eNombresreg.text.toString().trim()
                val apellidos = eApellidosreg.text.toString().trim()
                val password = password1.text.toString().trim()
                val passwordConfirmation = password2.text.toString().trim()

                if (username.isEmpty() && email.isEmpty() && names.isEmpty() && apellidos.isEmpty()
                    && password.isEmpty() && passwordConfirmation.isEmpty()){

                    Toast.makeText(this, "Llene los campos requeridos", Toast.LENGTH_SHORT).show()

                }else{

                    val registerRequest = RegisterRequest(username, names, apellidos, email, password)

                    registerUser(registerRequest)

                }

            }

        }

    }

    private fun login_User(loginRequest: LoginRequest) {

        val request = ApiConnection().getService().loginUser(loginRequest).enqueue(object :
            Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if (response.isSuccessful()){

                    val loginResponse : LoginResponse? = response.body()

                    if (loginResponse != null) {
                        session.createLoginSession(loginResponse.Usuario[0].username, loginResponse.Usuario[0].email, loginResponse.Usuario[0].id.toString())
                        Log.i("Response Login", loginResponse.toString())
                    }

                    Intent(applicationContext, MainActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }

                    Log.i("Login", response.body().toString())

                }else{

                    Toast.makeText(applicationContext, "Un error a ocurrido", Toast.LENGTH_SHORT).show()

                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }

    private fun registerUser(registerRequest: RegisterRequest) {

        val request = ApiConnection().getService().registerUser(registerRequest).enqueue(object :
            Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if(response.isSuccessful()){

                    val registerResponse : RegisterResponse? = response.body()

                    if (registerResponse != null) {

                        val id_user = registerResponse.user.id

                        Log.i("User Created", registerResponse.user.id.toString() +" and " + registerResponse.user.email )

                        session.createLoginSession( registerResponse.user.username, registerResponse.user.email, id_user.toString())

                        Intent(applicationContext, Register_Profile::class.java)
                            .putExtra("id_user", id_user.toString())
                            .also {
                                startActivity(it)
                                finish()
                            }

                    }

                }else{

                    Toast.makeText(applicationContext, "Un error a ocurrido", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                t.printStackTrace()
            }

        })

    }

}