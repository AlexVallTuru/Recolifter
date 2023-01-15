package cat.copernic.pdiaza.recolifter.ui.questionnaire

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import cat.copernic.pdiaza.recolifter.databinding.FragmentFlightsBinding
import cat.copernic.pdiaza.recolifter.models.DataQuestionnaire
import cat.copernic.pdiaza.recolifter.ui.flux.MainActivity
import cat.copernic.pdiaza.recolifter.utils.AppConstants

const val SHOT_FLY_POSITION = 0
const val MEDIUM_FLY_POSITION = 1
const val LONG_FLY_POSITION = 2

const val MIN_FLIGHTS = 0
const val MAX_FLIGHTS = 50

/**
 * Clase encargada de vincular los datos de los vuelos con la parte visual
 */
class FlightsFragment : Fragment() {

    private var _binding: FragmentFlightsBinding? = null
    private val binding get() = _binding!!

    private lateinit var dataQuestionnaire : DataQuestionnaire

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Esconder header
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        _binding = FragmentFlightsBinding.inflate(inflater, container, false)

        /**
         * Recibir la informacion del cuestionario.
         */
        arguments?.let {
            dataQuestionnaire = DataQuestionnaire(
                it.getIntArray(AppConstants.TRASPORT_TYPE_KEY),
                it.getIntArray(AppConstants.TRASPORT_TIME_KEY),
                it.getIntArray(AppConstants.FLIGHTS_KEY),
                it.getInt(AppConstants.ALIMENTATION_KEY),
                it.getInt(AppConstants.HOME_CONSUM_KEY)
            )
        }

        binding.shortPicker.minValue= MIN_FLIGHTS
        binding.shortPicker.maxValue= MAX_FLIGHTS

        binding.mediumPicker.minValue= MIN_FLIGHTS
        binding.mediumPicker.maxValue= MAX_FLIGHTS

        binding.longPicker.minValue= MIN_FLIGHTS
        binding.longPicker.maxValue= MAX_FLIGHTS

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btNextFlights.setOnClickListener {
            setFlightsToQuestionnaireData()

            //Enviar la informacion del cuestionario
            val action =
                FlightsFragmentDirections.actionFlightsFragmentToAlimentationFragment(
                    transportType = dataQuestionnaire.transportType!!,
                    transportTime = dataQuestionnaire.transportTime!!,
                    flights = dataQuestionnaire.flights!!,
                    alimentation = dataQuestionnaire.alimentation!!,
                    homeConsum = dataQuestionnaire.homeConsum!!
                )

            Navigation.findNavController(it).navigate(action)
        }

        binding.btBackFlights.setOnClickListener {
            setFlightsToQuestionnaireData()

            //Enviar la informacion del cuestionario
            val action =
                FlightsFragmentDirections.actionFlightsFragmentToTransportTimePickerFragment(
                    transportType = dataQuestionnaire.transportType!!,
                    transportTime = dataQuestionnaire.transportTime!!,
                    flights = dataQuestionnaire.flights!!,
                    alimentation = dataQuestionnaire.alimentation!!,
                    homeConsum = dataQuestionnaire.homeConsum!!
                )
            Navigation.findNavController(it).navigate(action)
        }
    }

    /**
     * Guardar informacion de los picker en el objeto dataQuestionnaire
     */
    private fun setFlightsToQuestionnaireData() {
        dataQuestionnaire.flights!!.set(SHOT_FLY_POSITION,binding.shortPicker.value)
        dataQuestionnaire.flights!!.set(MEDIUM_FLY_POSITION,binding.mediumPicker.value)
        dataQuestionnaire.flights!!.set(LONG_FLY_POSITION,binding.longPicker.value)
    }


}