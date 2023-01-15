package cat.copernic.pdiaza.recolifter.ui.login.register

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import cat.copernic.pdiaza.recolifter.R
import cat.copernic.pdiaza.recolifter.databaseManager.FirebaseReadWrite
import cat.copernic.pdiaza.recolifter.databinding.ActivityLoginBinding
import cat.copernic.pdiaza.recolifter.models.DataUser
import cat.copernic.pdiaza.recolifter.ui.flux.MainActivity
import cat.copernic.pdiaza.recolifter.utils.AppConstants
import cat.copernic.pdiaza.recolifter.utils.Util
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * Clase encargada de vincular los datos del inicio de session con la parte visual
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseCompadre: FirebaseReadWrite
    private lateinit var auth: FirebaseAuth
    private lateinit var googleClient: GoogleSignInClient
    private lateinit var binding: ActivityLoginBinding
    private lateinit var changeActivity: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Oculta el actionBar (header)
        supportActionBar?.hide()

        FirebaseApp.initializeApp(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        firebaseCompadre = FirebaseReadWrite.initialize()

        //Opciones de registro de google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleClient = GoogleSignIn.getClient(this, gso)

        binding.loginButton.setOnClickListener {
            var email = this.binding.loginUserEditText.text.toString().trim()
            var password = this.binding.loginPasswordEditText.text.toString().trim()

            if (email.length < 5 || password.length < 6) {
                Util.showSnackBar(it, getString(R.string.invalid_credentials))
            } else {
                signIn(email, password)
            }
        }

        binding.forgotPasswordClicker.setOnClickListener {
            changeActivity = Intent(this, PasswordRecoveryActivity::class.java)
            startActivity(changeActivity)
        }
        binding.registerClicker.setOnClickListener {
            changeActivity = Intent(this, RegisterActivity::class.java)
            startActivity(changeActivity)
        }

        binding.googleButton.setOnClickListener {
            googleClient.signOut()
            logInGoogle()
        }
    }

    /**
     * Si el usuario está ya está logeado te lleva al MainActivity
     */
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI()
        }
    }

    /**
     * Recibe los datos del usuario, si existen te lleva al MainActivity, en caso contrario muestra un error
     * @param email
     * @param password
     */
    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(
                        baseContext, getString(R.string.authentication_succeed),
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, getString(R.string.authentication_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        // [END sign_in_with_email]
    }

    /**
     * Cambia a la pantalla MainActivity.
     */
    private fun updateUI() {
        changeActivity = Intent(this, MainActivity::class.java)
        changeActivity.putExtra(AppConstants.FIRST_FRAGMENT, AppConstants.LOGIN_KEY)
        startActivity(changeActivity)
        finish()
    }

    /**
     * Login con google
     */
    private fun logInGoogle() {
        val signInIntent = googleClient.signInIntent
        googleLauncher.launch(signInIntent)

    }

    /**
     * Muestra las cuentas de google y confirma si se ha seleccionado una.
     */
    private val googleLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d(TAG, "result code: ${result.resultCode} activity code: ${Activity.RESULT_OK}")
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            } else {
                Log.d(TAG, "result code: ${result.toString()}")
            }
        }

    /**
     * Comprueba si la cuenta es correcta.
     * @param Task<GoogleSignInAccount>
     */
    private fun handleResults(task: Task<GoogleSignInAccount>) {
        Log.d(TAG, "task creation")
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUIGoogle(account)
            }
        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    /**
     * Comrprueba si la cuenta ya ha sido registrada, si no ha sido guarda los valore en base de datos,
     * y inicia con la cuenta de google.
     * @param account cuenta de google.
     */
    fun updateUIGoogle(account: GoogleSignInAccount) {
        firebaseCompadre.auth.fetchSignInMethodsForEmail(account.email.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val isNewUser = task.result.signInMethods!!.isEmpty()


                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    firebaseCompadre.auth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            Log.d(TAG, "User creation")
                            if (task.isSuccessful) {
                                Log.d("LoginActivity", "google Successful")
                                val intent: Intent = Intent(this, MainActivity::class.java)
                                intent.putExtra(AppConstants.FIRST_FRAGMENT, AppConstants.LOGIN_KEY)

                                if (isNewUser) {
                                    Log.d(TAG, "New User!")
                                    val user = DataUser(
                                        account.givenName,
                                        account.familyName,
                                        account.email,
                                        account.photoUrl.toString(),
                                        null,
                                        null,
                                        null
                                    )
                                    firebaseCompadre.setUserProfileData(user)
                                } else Log.d(TAG, "Old User!")

                                startActivity(intent)
                                finish()
                            } else {
                                Log.d(TAG, task.exception.toString())
                            }
                        }


                } else {
                    Log.d(TAG, task.exception.toString())
                }
            }


    }

    companion object {
        private const val TAG = "SignInPassword"
    }
}