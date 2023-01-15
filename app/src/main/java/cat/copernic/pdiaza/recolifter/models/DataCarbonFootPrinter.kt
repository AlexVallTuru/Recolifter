package cat.copernic.pdiaza.recolifter.models

/**
 * Clase para almacenar datos de la huella de carbono
 */
data class DataCarbonFootPrinter(
    var totalCarbon: Int,
    var transportCarbon: Int,
    var flightsCarbon: Int,
    var alimentationCarbon: Int,
    var homeConsumCarbon: Int

) {
    constructor() : this(
        0, 0,
        0, 0,
        0,
    )

    override fun toString(): String {
        return "DataCarbonFootPrinter(totalCarbon=$totalCarbon, transportCarbon=$transportCarbon, flightsCarbon=$flightsCarbon, alimentationCarbon=$alimentationCarbon, homeConsumCarbon=$homeConsumCarbon)"
    }


}

