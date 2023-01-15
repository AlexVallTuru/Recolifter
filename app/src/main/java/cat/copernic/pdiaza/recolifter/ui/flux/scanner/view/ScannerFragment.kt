package cat.copernic.pdiaza.recolifter.ui.flux.scanner.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.pdiaza.recolifter.R
import cat.copernic.pdiaza.recolifter.adapters.ScannerAdapter
import cat.copernic.pdiaza.recolifter.databaseManager.FirebaseReadWrite
import cat.copernic.pdiaza.recolifter.databinding.FragmentScannerBinding
import cat.copernic.pdiaza.recolifter.models.DataRecycleProduct
import cat.copernic.pdiaza.recolifter.models.DataUserRewards
import cat.copernic.pdiaza.recolifter.ui.flux.scanner.provider.ScannerProvider
import com.google.firebase.firestore.ktx.toObject
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import java.util.*

/**
 * Clase encargada de vincular los datos del scanner con la parte visual
 */
class ScannerFragment : Fragment() {

    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseCompadre: FirebaseReadWrite
    private var userRewards: DataUserRewards? = null
    private lateinit var recyclerAdapter: ScannerAdapter
    private var productsScan: MutableMap<String, DataRecycleProduct> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentScannerBinding.inflate(inflater, container, false)
        firebaseCompadre = FirebaseReadWrite.initialize()
        initRecyclerView()
        getUserRewardsData()
        getDataProductScann()
        ScannerProvider.dataProductsScannerUser(firebaseCompadre, recyclerAdapter,this)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.scannerButton.setOnClickListener { initScanner() }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * initScanner inicia el escaner de codigo de barras
     */
    private fun initScanner() {
        barcodeLauncher.launch(ScanOptions())

    }

    // Register the launcher and result handler
    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if(result.contents!=null){
            scannerResultExist(result.contents)
        }
    }

    //Guarda el productScanner si existe en la lista de productos
    private fun scannerResultExist(scannerResult: String) {
        if (productsScan.containsKey(scannerResult)) {
            val productScanned = productsScan[scannerResult]
            if (productScanned != null) {
                //add to database the product scanned
                addProductToUser(productScanned)
                //update the points of the product
                updatePoints(productScanned.points)
                //update recycler view
                ScannerProvider.dataProductsScannerUser(firebaseCompadre, recyclerAdapter,this)
            }

        } else {
            Toast.makeText(context, getString(R.string.scan_product_no_exist), Toast.LENGTH_LONG)
                .show()
        }

    }

    //Guarda el productScanned en base de datos
    private fun addProductToUser(productScanned: DataRecycleProduct) {
        productScanned.date = Calendar.getInstance().time
        firebaseCompadre.setScannerProduct(productScanned, productScanned.scanCode)
    }

    //Actualiza los puntos del usuario
    private fun updatePoints(points: Int) {
        userRewards!!.userPoints += points
        firebaseCompadre.setUserRewards(userRewards!!)
        binding.tvPoints.text = userRewards!!.userPoints.toString()
    }

    //Carga los puntos del usuario y los muestra
    private fun getUserRewardsData() {
        firebaseCompadre.getUserRewards().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                userRewards = documentSnapshot.toObject<DataUserRewards>()
            } else {
                userRewards = DataUserRewards()
            }
            binding.tvPoints.text = userRewards!!.userPoints.toString()
        }
    }

    //Inicia el recyclerView
    private fun initRecyclerView() {
        val manager = LinearLayoutManager(context)
        //val decoration = DividerItemDecoration(context,manager.orientation)
        binding.recyclerScanner.layoutManager = manager
        recyclerAdapter = ScannerAdapter(ScannerProvider.historicProductsList)
        binding.recyclerScanner.adapter = recyclerAdapter
        //binding.recyclerScanner.addItemDecoration(decoration)
    }

    //Carga el listado de productos
    private fun getDataProductScann() {
        firebaseCompadre.getListProductsScan().addOnSuccessListener {
            for (documentSnapshot in it) {
                val item = documentSnapshot.toObject<DataRecycleProduct>()
                productsScan.put(item.scanCode, item)
                Log.d("ScannerFragment", "item: ${item.toString()}")
            }


        }
    }

    //Muestra o oculta el recyclerView
    fun historicScannerResult(list : List<DataRecycleProduct>){
        if(list.isEmpty()){
            binding.recyclerScanner.visibility = View.GONE
            binding.tvNoScannerResult.visibility = View.VISIBLE
        }else{
            binding.recyclerScanner.visibility = View.VISIBLE
            binding.tvNoScannerResult.visibility = View.GONE
        }
    }
}