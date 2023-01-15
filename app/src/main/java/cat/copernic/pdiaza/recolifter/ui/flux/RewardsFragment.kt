package cat.copernic.pdiaza.recolifter.ui.flux

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.pdiaza.recolifter.adapters.CostumerAdapter
import cat.copernic.pdiaza.recolifter.databaseManager.FirebaseReadWrite
import cat.copernic.pdiaza.recolifter.databinding.FragmentRewardsBinding
import cat.copernic.pdiaza.recolifter.models.DataReward
import cat.copernic.pdiaza.recolifter.models.DataUserRewards
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

/**
 * Clase encargada de vincular los datos de los premios con la parte visual
 */
class RewardsFragment : Fragment() {

    private lateinit var _binding: FragmentRewardsBinding
    private val binding get() = _binding

    private lateinit var firebaseCompadre: FirebaseReadWrite
    private var db = FirebaseFirestore.getInstance()
    private var userRewards: DataUserRewards? = null

    var tcredits: Int = 0
    var contador: Int = 0

    override fun onCreateView(
        infalter: LayoutInflater, container:
        ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val rewards: MutableList<DataReward> = mutableListOf()
        _binding = FragmentRewardsBinding.inflate(infalter, container, false)
        firebaseCompadre = FirebaseReadWrite.initialize()

        //GET BDD

        val treetypes = db.collection("TreeTypes")
        treetypes.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val credits = document.getLong("credits")
                    val descripcion = document.getString("description")
                    val image = document.getString("image")
                    val title = document.getString("title")

                    if (credits != null) {
                        rewards.add(
                            DataReward(
                                title.toString(),
                                descripcion.toString(),
                                credits.toInt(),
                                image.toString()
                            )
                        )
                    }
                    //val reward = document.toObject(Reward::class.java)
                    //rewards.add(reward)
                }
                val adapter = CostumerAdapter()
                adapter.CostumerAdapter(rewards, this, context)
                binding.recyclerView.layoutManager = LinearLayoutManager(context)

                //Generem el adapter
                binding.recyclerView.adapter = adapter
            }


        //GET A LA BDD
        val docRef =
            db.collection("UserRewards").document(FirebaseAuth.getInstance().currentUser!!.uid)
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                userRewards = documentSnapshot.toObject<DataUserRewards>()
                //var userRewards:String = documentSnapshot.data?.get("UserPoints").toString()
                _binding.totalcredits.text = userRewards!!.userPoints.toString()
                if (userRewards != null) {
                    if (userRewards!!.userPoints != null) {
                        this.tcredits = userRewards!!.userPoints!!
                    }
                    if (userRewards!!.cantidadDeArboles != null) {
                        this.contador = userRewards!!.cantidadDeArboles!!
                        Log.d("Contador", "Contador +$contador")
                    }
                }
            }




        return binding.root

    }

    fun setTotalCredits(tcredits: Int, contador: Int) {
        binding.totalcredits.text = tcredits.toString()
        firebaseCompadre.setUserPoints(DataUserRewards(tcredits, contador))
    }
}

