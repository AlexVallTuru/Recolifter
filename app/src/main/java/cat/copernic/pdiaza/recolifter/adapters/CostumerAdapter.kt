package cat.copernic.pdiaza.recolifter.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import cat.copernic.pdiaza.recolifter.databinding.CardLayoutBinding
import cat.copernic.pdiaza.recolifter.models.DataReward
import cat.copernic.pdiaza.recolifter.ui.flux.RewardsFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import cat.copernic.pdiaza.recolifter.databaseManager.FirebaseReadWrite


class CostumerAdapter : RecyclerView.Adapter<CostumerAdapter.ViewHolder>() {

    var rewards: MutableList<DataReward> = ArrayList()
    var context: Context? = null
    var parentFragment: RewardsFragment? = null

    private lateinit var firebaseCompadre: FirebaseReadWrite

    fun CostumerAdapter(rewardList: MutableList<DataReward>, pF: RewardsFragment, contxt: Context?) {
        this.rewards = rewardList
        this.context = contxt
        this.parentFragment = pF
    }

    //és l'encarregat de retornar el ViewHolder ja configurat
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            CardLayoutBinding.inflate(
                layoutInflater, parent, false
            )
        )
    }

    //Aquest mètode s'encarrega de passar els objectes, un a un al ViewHolder personalitzat
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        firebaseCompadre = FirebaseReadWrite.initialize()

        with(holder) {
            with(rewards.get(position)) {
                binding.btPlantar.setOnClickListener {
                    Log.d("CostumeraAdapter", "TotalCredit =$parentFragment.tcredits Credits=$credits")
                    if (credits <= parentFragment!!.userRewards!!.userPoints) {
                        parentFragment!!.userRewards!!.userPoints -= credits
                        parentFragment!!.userRewards!!.cantidadDeArboles++
                        parentFragment!!.setTotalCredits()

                        firebaseCompadre.setObjectTrees(DataReward(this.title,this.description,this.credits,this.image))


                    } else {
                        Toast.makeText(
                            binding.btPlantar.context,
                            "No tens suficients credits",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                binding.itemTitle.text = this.item_title
                binding.itemImage.load(this.item_image)
                binding.itemCredits.text = this.item_credits.toString()

                val item = rewards.get(position)
                holder.bind(item)
            }
        }
        val item = rewards.get(position)
        holder.bind(item)

        //Estamblim un listener
        holder.itemView.setOnClickListener {

            val image = ImageView(context)
            Picasso.get().load(rewards.get(position).image).into(image)

            MaterialAlertDialogBuilder(context!!)
                .setTitle(rewards.get(position).title)
                .setMessage("${rewards.get(position).item_detail}\n")
                .setView(image)
                .setPositiveButton("Tancar", null)
                .show()
        }
    }

    override fun getItemCount(): Int {
        return rewards.size
    }

    class ViewHolder(val binding: CardLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(c: DataReward) {
        }
    }
}

