package cat.copernic.pdiaza.recolifter.ui.flux.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import cat.copernic.pdiaza.recolifter.R
import cat.copernic.pdiaza.recolifter.databaseManager.FirebaseReadWrite
import cat.copernic.pdiaza.recolifter.databinding.FragmentHomeBinding
import cat.copernic.pdiaza.recolifter.models.DataCarbonFootPrinter
import cat.copernic.pdiaza.recolifter.models.DataUser
import cat.copernic.pdiaza.recolifter.models.DataReward
import cat.copernic.pdiaza.recolifter.models.DataUserRewards
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.toObject
import org.imaginativeworld.whynotimagecarousel.CarouselItem

/**
 * Clase encargada de vincular los datos del home con la parte visual
 */
class HomeFragment : Fragment() {
    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding
    private val rewards: MutableList<DataReward> = mutableListOf()
    private lateinit var firebaseCompadre: FirebaseReadWrite
    private var totalCo2: Int = 0
    private var userRewards: DataUserRewards? = null
    private var nombreUsuario: String = ""
    private var apellidoUsuario: String? = null
    private var imageUserProfile: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        context?.let { Glide.get(it).clearMemory() }
        firebaseCompadre = FirebaseReadWrite.initialize()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.dataLayout.visibility = View.INVISIBLE
        binding.shimmerViewContainer.startShimmer()

        getHomeData()
        return binding.root
    }

    /**
     * getHomeData carga toda la informacion de base de datos a la ventana home
     */
    private fun getHomeData() {
        //Get user rewards data from database firestore
        firebaseCompadre.db.collection("UserRewards")
            .document(FirebaseAuth.getInstance().currentUser!!.uid).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    userRewards = documentSnapshot.toObject<DataUserRewards>()
                    Log.d("document", "UserRewars=${userRewards!!.cantidadDeArboles}")
                    Log.d(FirebaseReadWrite.TAG, "DocumentSnapshot successfully read!")
                } else {
                    userRewards = DataUserRewards()
                    firebaseCompadre.setUserPoints(userRewards!!)
                }
                binding.treeCounter.text = userRewards!!.cantidadDeArboles.toString()
                changeShimmerToDataLayout()

            }
            .addOnFailureListener { e -> Log.w(FirebaseReadWrite.TAG, "Error reading document", e) }

        //Get CarbonFootPrinter data from database firestore
        firebaseCompadre.getUserCo2Printer().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                totalCo2 = documentSnapshot.toObject<DataCarbonFootPrinter>()!!.totalCarbon
                Log.d(FirebaseReadWrite.TAG, "DocumentSnapshot successfully read!")
            } else {
                firebaseCompadre.setUserCo2Printer(DataCarbonFootPrinter())
            }
            binding.totalCo2.text = totalCo2.toString()
            binding.tvCarbonDescription.text =
                getString(R.string.descripcioEmprenta_home, totalCo2.toString())

        }.addOnFailureListener { e -> Log.w(FirebaseReadWrite.TAG, "Error reading document", e) }

        //Get datos de l'usuario
        firebaseCompadre.getUserProfileData().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                nombreUsuario = documentSnapshot.toObject<DataUser>()!!.firstName.toString()
                apellidoUsuario = documentSnapshot.toObject<DataUser>()!!.lastName
                Log.d(FirebaseReadWrite.TAG, "DocumentSnapshot successfully read!")
                imageUserProfile = documentSnapshot.toObject<DataUser>()!!.image
            }

            //En caso de no tener foto de perfil se matiene la imagen predeterminada
            if (imageUserProfile != null) {
                Glide.with(binding.ivImageProfileHome).load(imageUserProfile)
                    .into(binding.ivImageProfileHome)
            }
            binding.tvFirstNameHome.text = nombreUsuario
            binding.tvSurnameHome.text = apellidoUsuario ?: ""

        }.addOnFailureListener { e ->
            Log.w(
                FirebaseReadWrite.TAG, "Error reading document", e
            )
        }

        //Cargamos los datos de firebase para poder adjuntarlos al carrousel
        firebaseCompadre.db.collection("TreeTypes").get().addOnSuccessListener { result ->
            //Al realizar el clear no duplicamos el carrousel
            rewards.clear()
            for (document in result) {
                val credits = document.getLong("credits")
                val descripcion = document.getString("description")
                val image = document.getString("image")
                val title = document.getString("title")
                if (title != null) {
                    rewards.add(
                        DataReward(
                            title.toString(),
                            descripcion.toString(),
                            credits!!.toInt(),
                            image.toString()
                        )
                    )
                }
            }

            val listCarouse = mutableListOf<CarouselItem>()
                rewards.forEach { Reward ->
                    listCarouse.add(
                        CarouselItem(
                            Reward.image, Reward.title
                        )
                    )
                }
                binding.carouselId.addData(listCarouse)


        }
    }

    /**
     * changeShimmerToDataLayout finaliza el efecto shimmer y muestra la capa visual con sus datos
     */
    private fun changeShimmerToDataLayout() {
        binding.shimmerViewContainer.stopShimmer()
        binding.shimmerViewContainer.visibility = View.INVISIBLE
        binding.dataLayout.visibility = View.VISIBLE
    }
}

