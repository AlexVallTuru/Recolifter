package cat.copernic.pdiaza.recolifter.ui.login.register


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import cat.copernic.pdiaza.recolifter.R
import cat.copernic.pdiaza.recolifter.databinding.ActivityPasswordRecoveryBinding
import cat.copernic.pdiaza.recolifter.utils.Util
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PasswordRecoveryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordRecoveryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPasswordRecoveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Esto oculta el actionBar (header)
        supportActionBar?.hide()

        binding.loginButton.setOnClickListener {
            //TODO verificar que el correo es correcto
            sendPasswordReset()
        }
    }

    /**
     * Envía un correo electrónico para restablecer la contraseña.
     */
    private fun sendPasswordReset() {
        // [START send_password_reset]
        val emailAddress = binding.emailText.text.toString()

        Firebase.auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                    var changeActivity = Intent(this, LoginActivity::class.java)
                    startActivity(changeActivity)
                } else {
                    Log.d(TAG, "Wrong email")
                    Util.showSnackBar(this.binding.root, getString(R.string.invalid_email))
                }
            }
    }

    companion object {
        private const val TAG = "PasswordRecovery"
    }
}
