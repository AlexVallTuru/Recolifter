package cat.copernic.pdiaza.recolifter.databaseManager

import android.util.Log
import cat.copernic.pdiaza.recolifter.models.DataUser
import cat.copernic.pdiaza.recolifter.models.DataCarbonFootPrinter
import cat.copernic.pdiaza.recolifter.models.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

/**
 * Clase encargada de guardar y recuperar la informacion de la base de datos
 */
class FirebaseReadWrite {

    var auth: FirebaseAuth
    var db: FirebaseFirestore
    var storage: FirebaseStorage

    companion object {
        const val TAG = "Cloud Firestore"
        private var FirebaseCompadre: FirebaseReadWrite? = null
        fun initialize(): FirebaseReadWrite {
            if (FirebaseCompadre == null) {
                FirebaseCompadre = FirebaseReadWrite()
            }
            return FirebaseCompadre!!
        }
    }

    /**
     * Inicializa las instancias de Storage, Authenticator y Firestore
     */
    init {
        storage = Firebase.storage
        auth = Firebase.auth
        db = Firebase.firestore

        // [START set_firestore_settings]
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings
        // [END set_firestore_settings]
    }

    /**
     * Envía el email y el nombre de usuario a la BBDD
     *
     * @param email recibe el email
     */
    fun addEmailAndUser(email: String) {
        val splitter = "@"
        val array = email.split(splitter).toTypedArray()
        val user = hashMapOf(
            "email" to email,
            "firstName" to array.first().capitalize(),
            "sex" to "Helicoptere Apache de Combate"
        )
        // Add a new document with a generated ID
        db.collection("UserProfile").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .set(user)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    /**
     * Recibe un objeto de tipo DataUser con la información del perfil de usuario y lo envía a la BBDD
     * @param userProfile
     */
    fun setUserProfileData(userProfile: DataUser) {
        db.collection("UserProfile").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .set(userProfile)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    fun setUserPoints(userRewards: DataUserRewards) {
        db.collection("UserRewards").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .set(userRewards)
            .addOnCompleteListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    fun setObjectTrees(reward: DataReward) {
        val timeStamp: String = SimpleDateFormat("dd/MM/yyy").format(Date())
        val treeTypes = hashMapOf(
            "title" to reward.title,
            "actualdate" to timeStamp
        )
        db.collection("UserRewards/" + FirebaseAuth.getInstance().currentUser!!.uid + "/[TypeTree]")
            .document()
            .set(treeTypes)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    /**
     * Obtiene los datos del usuario logueado y retorna la tarea.
     */
    fun getUserProfileData(): Task<DocumentSnapshot> {
        return db.collection("UserProfile").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
    }

    /**
     * Devuelve la tarea del usuario registrado de la collecion UserCo2Print
     * @return DocumentSnapshot del usuario
     */
    fun getUserCo2Printer(): Task<DocumentSnapshot> {
        return db.collection("UserCo2Print").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
    }

    /**
     * Guarda el objeto DataCarbonFootPrinter en el documento del usuario de la coleccion UserCo2Print
     * @param dataCarbonFootPrinter objeto con la informacion del consumo de carbono del usuario
     */
    fun setUserCo2Printer(dataCarbonFootPrinter: DataCarbonFootPrinter) {
        db.collection("UserCo2Print").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .set(dataCarbonFootPrinter)
            .addOnCompleteListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    /**
     * Devuelve la tarea del usuario registrado de la collecion UserRewards
     * @return DocumentSnapshot del usuario
     */
    fun getUserRewards(): Task<DocumentSnapshot> {
        return db.collection("UserRewards").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
    }

    /**
     * Guarda el objeto DataCarbonFootPrinter en el documento del usuario de la coleccion UserRewards
     * @param userReward objeto con la informacion de los premios del usuario
     */
    fun setUserRewards(userReward: DataUserRewards) {
        db.collection("UserRewards").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .set(userReward)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    /**
     * Devuelve un array de RecycleProduct del usuario registrado de la colleccion UserScanner
     * @return QuerySnapshot
     */
    fun getScannerProduct(): Task<QuerySnapshot> {
        return db.collection("UserScanner").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .collection("RecycleProduct").get()
    }


    /**
     * Guarda el objeto DataRecycleProduct en el documento del usuario de la coleccion UserScanner
     * @param recycleProduct objeto con la informacion del producto escaneado
     * @param documentNumber id del documento
     */
    fun setScannerProduct(recycleProduct: DataRecycleProduct, documentNumber: String) {
        db.collection("UserScanner").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .collection("RecycleProduct").document(documentNumber)
            .set(recycleProduct)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    /**
     * Devuelve un array de documentos de la collecion ProductScanner
     * @return QuerySnapshot
     */
    fun getListProductsScan(): Task<QuerySnapshot> {
        return db.collection("ProductScanner").get()
    }

    /**
     * Guarda DataRecyclerProducts en firebase
     * @param scannProduct objeto con la informacion del producto
     */
    fun setListProductsScan(scannProduct: DataRecycleProduct) {
        db.collection("ProductScanner").document()
            .set(scannProduct)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

}