package cat.copernic.pdiaza.recolifter.models

/**
 * Clase para almacenar datos del cuaestionario de la huella de carbono
 */
data class DataQuestionnaire(
    var transportType: IntArray?,
    var transportTime: IntArray?,
    var flights: IntArray?,
    var alimentation: Int?,
    var homeConsum: Int?
)
