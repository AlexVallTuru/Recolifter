package cat.copernic.pdiaza.recolifter.ui.login.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import cat.copernic.pdiaza.recolifter.R
import cat.copernic.pdiaza.recolifter.databinding.ActivityPreLoginBinding
import cat.copernic.pdiaza.recolifter.databinding.ActivityRegisterBinding

/**
 * Clase encargada de vincular los datos del preLogin con la parte visual
 */
class PreLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


    
        // Esto oculta el actionBar (header)
        supportActionBar?.hide()



        var changeActivity : Intent

        binding.registerButton.setOnClickListener{
            //cambia a la actividad de registro
            changeActivity = Intent(this, RegisterActivity::class.java)
            startActivity(changeActivity)
        }

        binding.loginText.setOnClickListener {
            //cambia a la actividad de login
            changeActivity = Intent(this, LoginActivity::class.java)
            startActivity(changeActivity)
        }

    }

}