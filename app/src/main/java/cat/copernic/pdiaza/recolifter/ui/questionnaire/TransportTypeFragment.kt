package cat.copernic.pdiaza.recolifter.ui.questionnaire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import cat.copernic.pdiaza.recolifter.R
import cat.copernic.pdiaza.recolifter.databinding.FragmentTransportTypeBinding
import cat.copernic.pdiaza.recolifter.models.DataQuestionnaire
import cat.copernic.pdiaza.recolifter.ui.flux.MainActivity
import cat.copernic.pdiaza.recolifter.utils.AppConstants

/**
 * Clase encargada de vincular los datos de los tipos de transporte con la parte visual
 */
class TransportTypeFragment : Fragment() {

    private var _binding: FragmentTransportTypeBinding? = null
    private val binding get() = _binding!!

    private var isCarClicked = false
    private var isMotorBikeClicked = false
    private var isBusClicked = false
    private var isTrainClicked = false
    private var isBikeClicked = false
    private var isWalkClicked = false

    private lateinit var dataQuestionnaire : DataQuestionnaire

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        _binding = FragmentTransportTypeBinding.inflate(inflater, container, false)

        //Recibir informacion cuestionario
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

        binding.btNextTransportType.setOnClickListener {
            //Enviar informacion cuestionario
            val action =
                TransportTypeFragmentDirections.actionTransportTypeFragmentToTransportTimePickerFragment(
                    transportType = dataQuestionnaire.transportType!!,
                    transportTime = dataQuestionnaire.transportTime!!,
                    flights = dataQuestionnaire.flights!!,
                    alimentation = dataQuestionnaire.alimentation!!,
                    homeConsum = dataQuestionnaire.homeConsum!!
                )
            Navigation.findNavController(it)
                .navigate(action)
        }

        binding.btBackTransportType.setOnClickListener {
            //Enviar informacion cuestionario
            val action =
                TransportTypeFragmentDirections.actionTransportTypeFragmentToQuestionnaireFragment(
                    transportType = dataQuestionnaire.transportType!!,
                    transportTime = dataQuestionnaire.transportTime!!,
                    flights = dataQuestionnaire.flights!!,
                    alimentation = dataQuestionnaire.alimentation!!,
                    homeConsum = dataQuestionnaire.homeConsum!!
                )
            Navigation.findNavController(it)
                .navigate(action)
        }

        //Cambio de imagen si esta pulsado
        binding.imageButtonCar.setOnClickListener {
            var image : Int = if(!isCarClicked) R.drawable.im_car_clicked else R.drawable.im_car
            var carIsSelected : Int = if(!isCarClicked) 1 else 0

            dataQuestionnaire.transportType?.set(0,carIsSelected)
            binding.imageButtonCar.setImageResource(image)
            isCarClicked = !isCarClicked
        }

        binding.imageButtonMotorbike.setOnClickListener {
            var image : Int = if(!isMotorBikeClicked) R.drawable.im_motorbike_clicked else R.drawable.im_motobike
            var motorbikeIsSelected : Int = if(!isMotorBikeClicked) 1 else 0

            dataQuestionnaire.transportType?.set(1,motorbikeIsSelected)
            binding.imageButtonMotorbike.setImageResource(image)
            isMotorBikeClicked = !isMotorBikeClicked
        }

        binding.imageButtonBus.setOnClickListener {
            var image : Int = if(!isBusClicked) R.drawable.im_bus_clicked else R.drawable.im_bus
            var busIsSelected : Int = if(!isBusClicked) 1 else 0

            dataQuestionnaire.transportType?.set(2,busIsSelected)
            binding.imageButtonBus.setImageResource(image)
            isBusClicked = !isBusClicked
        }

        binding.imageButtonTrain.setOnClickListener {
            var image : Int = if(!isTrainClicked) R.drawable.im_train_clicked else R.drawable.im_train
            var trainIsSelected : Int = if(!isTrainClicked) 1 else 0

            dataQuestionnaire.transportType?.set(3,trainIsSelected)
            binding.imageButtonTrain.setImageResource(image)
            isTrainClicked = !isTrainClicked
        }

        binding.imageButtonBike.setOnClickListener {
            var image : Int = if(!isBikeClicked) R.drawable.im_bike_clicked else R.drawable.im_bike
            var bikeIsSelected : Int = if(!isBikeClicked) 1 else 0

            dataQuestionnaire.transportType?.set(3,bikeIsSelected)
            binding.imageButtonBike.setImageResource(image)
            isBikeClicked = !isBikeClicked
        }

        binding.imageButtonWalk.setOnClickListener {
            var image : Int = if(!isWalkClicked) R.drawable.im_walk_clicked else R.drawable.im_walk
            var walkIsSelected : Int = if(!isWalkClicked) 1 else 0

            dataQuestionnaire.transportType?.set(3,walkIsSelected)
            binding.imageButtonWalk.setImageResource(image)
            isWalkClicked = !isWalkClicked
        }

    }


    override fun onResume() {
        super.onResume()
        (activity as MainActivity).hideBottomNav()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

    }


}






