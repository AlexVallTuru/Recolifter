package cat.copernic.pdiaza.recolifter.models

import java.util.*

/**
 * Clase para almacenar datos de los productos escaneados
 */
data class DataRecycleProduct(
    var scanCode: String,
    var name: String,
    var points: Int,
    val image: String,
    var date: Date?
    ) {

    constructor() : this(
        "", "",
        0, "",
        null,
    )


}
