package cat.copernic.pdiaza.recolifter.ui.questionnaire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import cat.copernic.pdiaza.recolifter.databinding.FragmentTransportTimePickerBinding
import cat.copernic.pdiaza.recolifter.models.DataQuestionnaire
import cat.copernic.pdiaza.recolifter.utils.AppConstants

/**
 * Clase encargada de vincular los datos del tiempo de transporte con la parte visual
 */
class TransportTimePickerFragment : Fragment() {

    private var _binding: FragmentTransportTimePickerBinding? = null
    private val binding get() = _binding!!

    private lateinit var dataQuestionnaire : DataQuestionnaire

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Ocultar header
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        _binding = FragmentTransportTimePickerBinding.inflate(inflater, container, false)

        //Recibir informacion questionario
        arguments?.let {
            dataQuestionnaire = DataQuestionnaire(
                it.getIntArray(AppConstants.TRASPORT_TYPE_KEY),
                it.getIntArray(AppConstants.TRASPORT_TIME_KEY),
                it.getIntArray(AppConstants.FLIGHTS_KEY),
                it.getInt(AppConstants.ALIMENTATION_KEY),
                it.getInt(AppConstants.HOME_CONSUM_KEY)
            )
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //timer formato 24 horas
        binding.timePickerTransport.setIs24HourView(true)


        binding.btNextTransportTimePicker.setOnClickListener {
            setTimePickerToQuestionnaireData()

            //Enviar informacion cuestionario
            val action =
                TransportTimePickerFragmentDirections.actionTransportTimePickerFragmentToFlightsFragment(
                    transportType = dataQuestionnaire.transportType!!,
                    transportTime = dataQuestionnaire.transportTime!!,
                    flights = dataQuestionnaire.flights!!,
                    alimentation = dataQuestionnaire.alimentation!!,
                    homeConsum = dataQuestionnaire.homeConsum!!
                )
            Navigation.findNavController(it).navigate(action)

        }

        binding.btBackTransportTimePicker.setOnClickListener {
            setTimePickerToQuestionnaireData()

            //Enviar informacion cuestionario
            val action =
                TransportTimePickerFragmentDirections.actionTransportTimePickerFragmentToTransportTypeFragment(
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
     * Guardar informacion picker al objero dataQuestionnaire
     */
    private fun setTimePickerToQuestionnaireData(){
        dataQuestionnaire.transportTime!!.set(0,binding.timePickerTransport.hour)
        dataQuestionnaire.transportTime!!.set(1,binding.timePickerTransport.minute)
    }


}