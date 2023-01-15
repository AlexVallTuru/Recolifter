package cat.copernic.pdiaza.recolifter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.pdiaza.recolifter.R
import cat.copernic.pdiaza.recolifter.models.DataRecycleProduct
import cat.copernic.pdiaza.recolifter.ui.flux.scanner.ScannerViewHolder

/**
 * Clase encargada para hacer de puente entre los datos y las vistas contenidas en un ListView
 */
class ScannerAdapter(private val scannerList:List<DataRecycleProduct>): RecyclerView.Adapter<ScannerViewHolder>(){

    /**
     * onCreateViewHOlder pinta en el recyclerview todos los items de las lista.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScannerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ScannerViewHolder(layoutInflater.inflate(R.layout.item_scanner_historic, parent, false))

    }

    /**
     * onBindViewHolder interactua con cada uno de los items del recyclerview.
     */
    override fun onBindViewHolder(holder: ScannerViewHolder, position: Int) {
        val item = scannerList[position]
        holder.render(item)

    }

    /**
     * getItemCount devuelve el total de items que el recyclerview tendra.
     */
    override fun getItemCount(): Int {
        return scannerList.size
    }
}