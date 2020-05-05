package pl.dudzinski.clinic.handler

import java.util.*

data class AppointmentDTO(
        var id: Long?,
        var patient: PatientDTO,
        var doctor: DoctorDTO,
        var date: Long
) {
    constructor(patient: PatientDTO, doctorDTO: DoctorDTO, date: Long) :
            this(null, patient, doctorDTO, date)

    constructor(patient: PatientDTO, doctorDTO: DoctorDTO, date: Date) :
            this(null, patient, doctorDTO, date.time)

    fun date() : Date = Date(date)
}


data class PatientDTO(
        var id: Long?,
        var name: String,
        var surname: String,
        var addresses: List<AddressDTO>?
) {
    constructor(name: String, surname: String, address: List<AddressDTO>?) :
            this(null, name, surname, address)

    constructor(name: String, surname: String) :
            this(null, name, surname, emptyList())
}

data class DoctorDTO(
        var id: Long?,
        var name: String,
        var surname: String,
        var specialization: String
) {
    constructor(name: String, surname: String, specialization: String) :
            this(null, name, surname, specialization)
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
