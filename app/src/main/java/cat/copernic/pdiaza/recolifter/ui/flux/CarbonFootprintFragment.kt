package cat.copernic.pdiaza.recolifter.ui.flux

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import cat.copernic.pdiaza.recolifter.R
import cat.copernic.pdiaza.recolifter.databaseManager.FirebaseReadWrite
import cat.copernic.pdiaza.recolifter.databinding.FragmentCarbonFootprintBinding
import cat.copernic.pdiaza.recolifter.models.DataCarbonFootPrinter
import com.google.firebase.firestore.ktx.toObject

/**
 * Clase encargada de vincular los datos de la huella de carbono con la parte visual
 */
class CarbonFootprintFragment : Fragment() {

    private var _binding: FragmentCarbonFootprintBinding? = null
    private val binding: FragmentCarbonFootprintBinding get() = _binding!!
    private lateinit var firebase: FirebaseReadWrite
    private var dataCarbonFootPrinter: DataCarbonFootPrinter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarbonFootprintBinding.inflate(inflater, container, false)

        firebase = FirebaseReadWrite.initialize()
        binding.dataLayout.visibility = View.INVISIBLE
        binding.shimmerViewContainer.startShimmer()
        getCo2Data()

        (activity as MainActivity).hideBottomNav()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        // Inflate the layout for this fragment
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recalculatedCarbonFootprinter.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_navigation_carbon_footprint_to_questionnaireFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).hideBottomNav()
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).showBottomNav()
    }

    /**
     * Carga los datos de la huella de carbono de base de datos.
     */
    private fun getCo2Data() {
        firebase.getUserCo2Printer().addOnSuccessListener {
            this.dataCarbonFootPrinter = it.toObject<DataCarbonFootPrinter>()
            Log.d("dataUser", dataCarbonFootPrinter.toString())

            //Change the layout when the data is download from firebase
            changeShimmerToDataLayout()
        }
            .addOnCompleteListener {
                Log.d(FirebaseReadWrite.TAG, "DocumentSnapshot successfully read!")
            }
            .addOnFailureListener { e -> Log.w(FirebaseReadWrite.TAG, "Error reading document", e) }
    }

    /**
     * Para el efeto shimmer y muestra los valores en la parte visual.
     */
    private fun changeShimmerToDataLayout() {
        binding.shimmerViewContainer.stopShimmer()
        binding.shimmerViewContainer.visibility = View.INVISIBLE
        binding.dataLayout.visibility = View.VISIBLE


        binding.textTransport.text = dataCarbonFootPrinter?.transportCarbon.toString()
        binding.textFlights.text = dataCarbonFootPrinter?.flightsCarbon.toString()
        binding.textAlimentation.text = dataCarbonFootPrinter?.alimentationCarbon.toString()
        binding.textHomeConsum.text = dataCarbonFootPrinter?.homeConsumCarbon.toString()
        binding.textTotalCarbonFootprinter.text = dataCarbonFootPrinter?.totalCarbon.toString()
    }
}