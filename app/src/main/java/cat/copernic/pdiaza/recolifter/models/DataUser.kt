package cat.copernic.pdiaza.recolifter.models

/**
 * Clase para almacenar datos del usuario
 */
data class DataUser(
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var image: String? = null,
    var dateOfBrith: String? = null,
    var phoneNumber: String? = null,
    var sex: String? = null
)