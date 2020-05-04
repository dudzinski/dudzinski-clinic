package pl.dudzinski.clinic.handler

data class PatientDTO(
        var id: Long?,
        var name: String?,
        var surname: String?,
        var address: List<AddressDTO>? = emptyList()
) {
    constructor() : this(null, null, null, null)
    constructor(name: String, surname: String, address: List<AddressDTO>?) : this(null, name, surname, address)
}

data class DoctorDTO(
        var id: Long?,
        var name: String?,
        var surname: String?,
        var specialization : String?
) {
    constructor() : this(null, null, null,null)
    constructor(name: String, surname: String, specialization: String) : this(null, name, surname, specialization)
}

data class AddressDTO(
        var id: Long?,
        var city: String?,
        var postcode: String?,
        var street: String?,
        var houseNo: String?
) {
    constructor() : this(null, null, null, null, null)
    constructor(city: String?, postcode: String?, street: String?, houseNo: String?) : this(null, city, postcode, street, houseNo)
}
