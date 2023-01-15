package cat.copernic.pdiaza.recolifter.ui.questionnaire

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import cat.copernic.pdiaza.recolifter.R
import cat.copernic.pdiaza.recolifter.databaseManager.FirebaseReadWrite
import cat.copernic.pdiaza.recolifter.databinding.FragmentHomeConsumBinding
import cat.copernic.pdiaza.recolifter.models.DataCarbonFootPrinter
import cat.copernic.pdiaza.recolifter.models.DataQuestionnaire
import cat.copernic.pdiaza.recolifter.ui.flux.MainActivity
import cat.copernic.pdiaza.recolifter.utils.AppConstants


/**
 * Clase encargada de vincular los datos del consumo de hogar con la parte visual
 */
class HomeConsumFragment : Fragment() {

    private var _binding: FragmentHomeConsumBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataQuestionnaire: DataQuestionnaire
    private lateinit var firebase: FirebaseReadWrite
    private var dataCarbonFootPrinter = DataCarbonFootPrinter()

    private lateinit var radioButtonList: ArrayList<RadioButton>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        //Ocultar header
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        _binding = FragmentHomeConsumBinding.inflate(inflater, container, false)

        radioButtonList = arrayListOf(
            binding.radioButtonOnePerson,
            binding.radioButtonTwoPerson,
            binding.radioButtonTreePerson
        )

        //Recibir la informacion del cuestionario
        arguments?.let {
            dataQuestionnaire = DataQuestionnaire(
                it.getIntArray(AppConstants.TRASPORT_TYPE_KEY),
                it.getIntArray(AppConstants.TRASPORT_TIME_KEY),
                it.getIntArray(AppConstants.FLIGHTS_KEY),
                it.getInt(AppConstants.ALIMENTATION_KEY),
                it.getInt(AppConstants.HOME_CONSUM_KEY)
            )
        }

        firebase = FirebaseReadWrite.initialize()

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btNextHomeConsum.setOnClickListener {
            calculationCarbonFootPrinter()
            firebase.setUserCo2Printer(dataCarbonFootPrinter)

            //Enviar la informacion del cuestionario
            val action =
                HomeConsumFragmentDirections.actionHomeConsumFragmentToNavigationCarbonFootprint(
                    transportType = dataQuestionnaire.transportType!!,
                    transportTime = dataQuestionnaire.transportTime!!,
                    flights = dataQuestionnaire.flights!!,
                    alimentation = dataQuestionnaire.alimentation!!,
                    homeConsum = dataQuestionnaire.homeConsum!!
                )
            Navigation.findNavController(it).navigate(action)

        }

        binding.btBackHomeConsum.setOnClickListener {

            //Enviar la informacion del cuestionario
            val action =
                HomeConsumFragmentDirections.actionHomeConsumFragmentToAlimentationFragment(
                    transportType = dataQuestionnaire.transportType!!,
                    transportTime = dataQuestionnaire.transportTime!!,
                    flights = dataQuestionnaire.flights!!,
                    alimentation = dataQuestionnaire.alimentation!!,
                    homeConsum = dataQuestionnaire.homeConsum!!
                )

            Navigation.findNavController(it).navigate(action)

        }

        binding.radioButtonOnePerson.setOnClickListener {
            radioButtonSelected(radioButtonList, binding.radioButtonOnePerson)
            dataQuestionnaire.homeConsum = 0
        }

        binding.radioButtonTwoPerson.setOnClickListener {
            radioButtonSelected(radioButtonList, binding.radioButtonTwoPerson)
            dataQuestionnaire.homeConsum = 1
        }

        binding.radioButtonTreePerson.setOnClickListener {
            radioButtonSelected(radioButtonList, binding.radioButtonTreePerson)
            dataQuestionnaire.homeConsum = 2
        }
    }

    /**
     * Gestionar que radiobutton ha sido seleccionado
     */
    private fun radioButtonSelected(
        list: ArrayList<RadioButton>, radioButtonSelected: RadioButton
    ) {
        for (radioButton in list) {
            radioButton.isChecked = radioButton == radioButtonSelected
        }
    }


    /**
     * Calculo de la huella de carbono
     */
    private fun calculationCarbonFootPrinter() {
        //calculation transport
        //TODO calculo tranpsorte
        val transportType =
            (dataQuestionnaire.transportType!![0] * 90) +
                    (dataQuestionnaire.transportType!![1] * 85) +
                    (dataQuestionnaire.transportType!![2] * 60) +
                    (dataQuestionnaire.transportType!![3] * 40) +
                    (dataQuestionnaire.transportType!![4] * 10) +
                    (dataQuestionnaire.transportType!![5] * 5)

        val transportTime =
            (dataQuestionnaire.transportTime!![0] * 60) +
                    dataQuestionnaire.transportTime!![1]

        dataCarbonFootPrinter.transportCarbon= transportType *transportTime


        //calculation flights
        dataCarbonFootPrinter.flightsCarbon =
            (dataQuestionnaire.flights!![0] * 50) +
                    (dataQuestionnaire.flights!![1] * 90) +
                    (dataQuestionnaire.flights!![2] * 120)


        //calculation alimentation
        when (dataQuestionnaire.alimentation) {
            0 -> dataCarbonFootPrinter.alimentationCarbon = 60
            1 -> dataCarbonFootPrinter.alimentationCarbon = 50
            2 -> dataCarbonFootPrinter.alimentationCarbon = 40
            3 -> dataCarbonFootPrinter.alimentationCarbon = 30
            4 -> dataCarbonFootPrinter.alimentationCarbon = 20
            5 -> dataCarbonFootPrinter.alimentationCarbon = 10
        }
        //calculation home consum
        when (dataQuestionnaire.homeConsum) {
            0 -> dataCarbonFootPrinter.homeConsumCarbon = 10
            1 -> dataCarbonFootPrinter.homeConsumCarbon = 20
            2 -> dataCarbonFootPrinter.homeConsumCarbon = 30
        }

        //calculation total carbon footprinter
        dataCarbonFootPrinter.totalCarbon =
            dataCarbonFootPrinter.transportCarbon +
                    dataCarbonFootPrinter.flightsCarbon +
                    dataCarbonFootPrinter.alimentationCarbon +
                    dataCarbonFootPrinter.homeConsumCarbon
    }

}