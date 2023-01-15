package cat.copernic.pdiaza.recolifter.ui.flux.scanner

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cat.copernic.pdiaza.recolifter.databinding.ItemScannerHistoricBinding
import cat.copernic.pdiaza.recolifter.models.DataRecycleProduct
import cat.copernic.pdiaza.recolifter.utils.Util

/**
 * Clase encargada de mostrar la informacion
 */
class ScannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemScannerHistoricBinding.bind(view)

    fun render(scannerHero: DataRecycleProduct) {
        binding.tvTitleHero.text = scannerHero.name
        binding.tvDataHero.text = Util.dataFormatString(scannerHero.date)
        binding.tvPointsHero.text = scannerHero.points.toString()
        Glide.with(binding.ivHero.context).load(scannerHero.image).into(binding.ivHero)
    }
}