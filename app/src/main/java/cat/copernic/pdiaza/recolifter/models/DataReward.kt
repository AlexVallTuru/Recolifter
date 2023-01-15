package cat.copernic.pdiaza.recolifter.models

/**
 * Clase para almacenar datos de los premios
 */
data class DataReward(val title:String, val description:String, val credits:Int, val image:String){
    var item_title: String? = null
    var item_detail: String?=null
    var item_credits: Int? = null
    var item_image: String? = null

    init {
        this.item_title = title
        this.item_detail = description
        this.item_credits = credits
        this.item_image = image
    }
}