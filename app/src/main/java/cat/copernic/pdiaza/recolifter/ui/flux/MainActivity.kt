package cat.copernic.pdiaza.recolifter.ui.flux

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cat.copernic.pdiaza.recolifter.R
import cat.copernic.pdiaza.recolifter.databinding.ActivityMainBinding
import cat.copernic.pdiaza.recolifter.ui.login.register.LoginActivity
import cat.copernic.pdiaza.recolifter.utils.AppConstants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hotchemi.android.rate.AppRate

/**
 * Clase encargada de manejar los fragments
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.backQueue.clear()

        val firstFragment = intent.getStringExtra(AppConstants.FIRST_FRAGMENT).toString()
        choseFirstFragment(navController, firstFragment)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        /**
         * Configuracion del navView
         */
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_map,
                R.id.navigation_scanner,
                R.id.navigation_reward
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        rateApp()

    }


    /**
     * Método que sirve para crear el menú de usuario, retorna true si el menú se ha generado correctamente.
     * @param menu
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_option_menu, menu)
        return true
    }

    /**
     * Método que se ejecuta al seleccionar un elemento del menú de opciones.
     * Utiliza un controlador de navegación para navegar entre las diferentes vistas de la aplicación.
     * @param item elemento seleccionado en el menú de opciones
     * @return true si se ha procesado el elemento de menú correctamente, false en caso contrario.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return when (item.itemId) {
            R.id.signOut -> {
                signOut()
                true
            }
            R.id.carbonFootPrint -> {
                navController.navigate(R.id.action_global_navigation_carbon_footprint)
                true
            }
            R.id.userProfile -> {
                navController.navigate(R.id.action_global_navigation_user_profile)
                true
            }
            android.R.id.home -> {
                navController.popBackStack()
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Muestra un mensaje al pulsar el boton de atras y cierra la aplicacion si lo vuelves a pulsar
     */
    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        for (x in navController.backQueue) {
            Log.d("backQueue", x.destination.toString())
        }

        if (doubleBackToExitPressedOnce) {
            finish()
            return
        }

        val backQueueCount = navController.backQueue.count()
        if (backQueueCount <= 2) {
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, getString(R.string.dobleClickToExit), Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                doubleBackToExitPressedOnce = false
            }, 1000)
        } else {
            super.onBackPressed()
        }
    }

    /**
     * Cierra la sesión de usuario y te devuelve a la pantalla de Login.
     */
    private fun signOut() {
        // [START auth_sign_out]
        Firebase.auth.signOut()
        // [END auth_sign_out]
        val changeActivity = Intent(this, LoginActivity::class.java)
        startActivity(changeActivity)
        finish()
    }

    private fun rateApp(){
        AppRate.with(this)
            .setInstallDays(0) // default 10, 0 means install day.
            .setLaunchTimes(3) // default 10
            .setRemindInterval(2) // default 1
            .setOnClickButtonListener {
                Toast.makeText(this,"Test",Toast.LENGTH_SHORT).show()
            }
            .monitor()

        AppRate.showRateDialogIfMeetsConditions(this)
        AppRate.with(this).showRateDialog(this)
    }

    /**
     * Escoje que metodo iniciara al abrir la aplicacion
     */
    private fun choseFirstFragment(navController: NavController, fragment: String) {
        if (fragment.equals("Register")) {
            navController.navigate(R.id.action_global_questionnaireFragment)
        } else {
            navController.navigate(R.id.action_global_navigation_home)
        }
    }

    /**
     * Esconde el navView
     */
    fun hideBottomNav() {
        binding.navView.visibility = View.GONE
    }

    /**
     * Muestra el navView
     */
    fun showBottomNav() {
        binding.navView.visibility = View.VISIBLE
    }

}