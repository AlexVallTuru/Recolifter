package cat.copernic.pdiaza.recolifter.ui.questionnaire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import cat.copernic.pdiaza.recolifter.R
import cat.copernic.pdiaza.recolifter.databinding.FragmentQuestionnaireBinding
import cat.copernic.pdiaza.recolifter.ui.flux.MainActivity

/**
 * Clase encargada de vincular los datos del cuestionario con la parte visual
 */
class QuestionnaireFragment : Fragment() {

    private var _binding: FragmentQuestionnaireBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Esconder header
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        _binding = FragmentQuestionnaireBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.btSkip.setOnClickListener {
            navController.navigate(R.id.action_questionnaireFragment_to_navigation_home)
        }

        binding.btNextTransportType.setOnClickListener {
            //Enviar informacion cuestionario
            val action =
                QuestionnaireFragmentDirections.actionQuestionnaireFragmentToTransportTypeFragment(
                    transportType = intArrayOf(0, 0, 0, 0, 0, 0),
                    transportTime = intArrayOf(0, 0),
                    flights = intArrayOf(0, 0, 0),
                    alimentation = 0,
                    homeConsum = 0
                )
            navController.navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).hideBottomNav()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).showBottomNav()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

    }

}




