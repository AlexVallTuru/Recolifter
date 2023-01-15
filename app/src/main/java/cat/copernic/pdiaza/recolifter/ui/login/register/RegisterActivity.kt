package cat.copernic.pdiaza.recolifter.ui.login.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cat.copernic.pdiaza.recolifter.R
import cat.copernic.pdiaza.recolifter.databaseManager.FirebaseReadWrite
import cat.copernic.pdiaza.recolifter.databinding.ActivityRegisterBinding
import cat.copernic.pdiaza.recolifter.models.DataUser
import cat.copernic.pdiaza.recolifter.ui.flux.MainActivity
import cat.copernic.pdiaza.recolifter.utils.AppConstants
import cat.copernic.pdiaza.recolifter.utils.Util
import com.google.firebase.FirebaseApp
import java.util.regex.Pattern

/**
 * Clase encargada de vincular los datos del registro con la parte visual
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseCompadre: FirebaseReadWrite
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var user: DataUser
    private val PASSWORD_PATTERN: Pattern = Pattern.compile(
        "^" +
                "(?=\\S+$)" +  // no white spaces
                ".{6,}" +  // at least 6 characters
                "$"
    )

    companion object {
        private const val TAG = "EmailPassword"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Esto oculta el actionBar (header)
        supportActionBar?.hide()

        //instanciar objeto connexion firebase
        FirebaseApp.initializeApp(this)
        firebaseCompadre = FirebaseReadWrite.initialize()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            var email = binding.registerUserEditText.text.toString().trim()
            var password = binding.registerPasswordEditText.text.toString().trim()
            if (!binding.condicionesCheckbox.isChecked) {
                Util.showSnackBar(
                    it,
                    getString(R.string.conditions_check_box)
                )
            } else {
                if (isEmailValid(email) && isPasswordValid(password)) {
                    createAccount(email, password, it)
                } else {
                    Util.showSnackBar(
                        it,
                        getString(R.string.minimum_password_length)
                    )
                }
            }
        }

        // Cambio al activity de Login
        binding.loginClicker.setOnClickListener {
            var changeActivity = Intent(this, LoginActivity::class.java)
            startActivity(changeActivity)
        }
        // Términos y condiciones
        binding.condicionesTextview.setOnClickListener {
            getUrlFromIntent(it)
        }

    }

    // Verifica si el e-mail es valid
    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Verifica si la password es valida
    private fun isPasswordValid(password: String): Boolean {
        return PASSWORD_PATTERN.matcher(password).matches()
    }

    // Crea la cuenta de usuario en Firebase
    private fun createAccount(email: String, password: String, view: View) {
        // [START create_user_with_email]
        firebaseCompadre.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    firebaseCompadre.addEmailAndUser(email)
                    updateUI()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Util.showSnackBar(
                        view,
                        getString(R.string.register_failed)
                    )
                }
            }
        // [END create_user_with_email]
    }

    private fun updateUI() {
        var changeActivity = Intent(this, MainActivity::class.java)
        changeActivity.putExtra(AppConstants.FIRST_FRAGMENT, AppConstants.REGISTER_KEY)
        startActivity(changeActivity)
        finish()
    }


    fun getUrlFromIntent(view: View) {
        //condiciones_textview
        val urlTerms: Uri = Uri.parse("https://youtu.be/8xmbfKq0zHU")
        val intent = Intent(Intent.ACTION_VIEW, urlTerms)
        startActivity(intent)
    }

}