package cat.copernic.pdiaza.recolifter.ui.questionnaire

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import cat.copernic.pdiaza.recolifter.databinding.FragmentAlimentationBinding
import cat.copernic.pdiaza.recolifter.models.DataQuestionnaire
import cat.copernic.pdiaza.recolifter.ui.flux.MainActivity
import cat.copernic.pdiaza.recolifter.utils.AppConstants

/**
 * Clase encargada de vincular los datos de la alimentacion con la parte visual
 */
class AlimentationFragment : Fragment() {

    private var _binding: FragmentAlimentationBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataQuestionnaire: DataQuestionnaire

    private lateinit var radioButtonList: ArrayList<RadioButton>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        //Esconder el header
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        _binding = FragmentAlimentationBinding.inflate(inflater, container, false)

        radioButtonList = arrayListOf(
            binding.radioButtonLotMeat,
            binding.radioButtonModeratedMeat,
            binding.radioButtonLowMeat,
            binding.radioButtonFish,
            binding.radioButtonVegetarian,
            binding.radioButtonVegan,
        )

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
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btNextAlimetation.setOnClickListener {
            //Enviar la informacion del cuestionario
            val action =
                AlimentationFragmentDirections.actionAlimentationFragmentToHomeConsumFragment(
                    transportType = dataQuestionnaire.transportType!!,
                    transportTime = dataQuestionnaire.transportTime!!,
                    flights = dataQuestionnaire.flights!!,
                    alimentation = dataQuestionnaire.alimentation!!,
                    homeConsum = dataQuestionnaire.homeConsum!!
                )

            Navigation.findNavController(it).navigate(action)
        }

        binding.btBackAlimetation.setOnClickListener {
            //Enviar la informacion del cuestionario
            val action = AlimentationFragmentDirections.actionAlimentationFragmentToFlightsFragment(
                transportType = dataQuestionnaire.transportType!!,
                transportTime = dataQuestionnaire.transportTime!!,
                flights = dataQuestionnaire.flights!!,
                alimentation = dataQuestionnaire.alimentation!!,
                homeConsum = dataQuestionnaire.homeConsum!!
            )

            Navigation.findNavController(it).navigate(action)

        }

        binding.radioButtonLotMeat.setOnClickListener {
            radioButtonSelected(radioButtonList, binding.radioButtonLotMeat)
            dataQuestionnaire.alimentation = 0
        }

        binding.radioButtonModeratedMeat.setOnClickListener {
            radioButtonSelected(radioButtonList, binding.radioButtonModeratedMeat)
            dataQuestionnaire.alimentation = 1
        }

        binding.radioButtonLowMeat.setOnClickListener {
            radioButtonSelected(radioButtonList, binding.radioButtonLowMeat)
            dataQuestionnaire.alimentation = 2
        }

        binding.radioButtonFish.setOnClickListener {
            radioButtonSelected(radioButtonList, binding.radioButtonFish)
            dataQuestionnaire.alimentation = 3
        }

        binding.radioButtonVegetarian.setOnClickListener {
            radioButtonSelected(radioButtonList, binding.radioButtonVegetarian)
            dataQuestionnaire.alimentation = 4
        }

        binding.radioButtonVegan.setOnClickListener {
            radioButtonSelected(radioButtonList, binding.radioButtonVegan)
            dataQuestionnaire.alimentation = 5
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


}