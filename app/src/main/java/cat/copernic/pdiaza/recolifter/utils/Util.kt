package cat.copernic.pdiaza.recolifter.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Clase encargada de habilitar funciones necesarias para otras clases del proyecto.
 */
object Util {

    /**
     * Dialogo para mostrar un mensaje
     */
    fun showSnackBar(view: View, message: String){
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    /**
     * Transfor una fecha a un string con formato.
     */
    fun dataFormatString(date: Date?):String{
        return if(date != null){
            val df: DateFormat = SimpleDateFormat("d-MM-yyyy")
            df.format(date)
        }else{
            ""
        }

    }
}