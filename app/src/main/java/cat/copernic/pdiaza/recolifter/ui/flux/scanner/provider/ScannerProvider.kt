package cat.copernic.pdiaza.recolifter.ui.flux.scanner.provider

import android.util.Log
import androidx.fragment.app.FragmentActivity
import cat.copernic.pdiaza.recolifter.adapters.ScannerAdapter
import cat.copernic.pdiaza.recolifter.databaseManager.FirebaseReadWrite
import cat.copernic.pdiaza.recolifter.models.DataRecycleProduct
import cat.copernic.pdiaza.recolifter.models.Language
import cat.copernic.pdiaza.recolifter.ui.flux.MainActivity
import cat.copernic.pdiaza.recolifter.ui.flux.scanner.view.ScannerFragment
import com.google.firebase.firestore.ktx.toObject

/**
 * Clase encargada de proveer la informacion de base de datos al recyclerView
 */
class ScannerProvider {
    companion object {
        var historicProductsList: MutableList<DataRecycleProduct> = mutableListOf()

        fun dataProductsScannerUser(
            firebaseCompadre: FirebaseReadWrite,
            adapter: ScannerAdapter,
            fragment: ScannerFragment
        ) {
            historicProductsList.clear()
            firebaseCompadre.getScannerProduct(Language.instance.deviceLanguage.name).addOnSuccessListener {
                for (document in it) {
                    val item = document.toObject<DataRecycleProduct>()
                    historicProductsList.add(item)
                    Log.d("historicProductsList", "list: ${historicProductsList.toString()}")

                }
                Log.d("historicProductsList", historicProductsList.toString())
                adapter.notifyDataSetChanged()
                fragment.historicScannerResult(historicProductsList)
            }
        }
    }

}