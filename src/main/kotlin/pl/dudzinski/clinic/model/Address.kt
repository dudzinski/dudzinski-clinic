package pl.dudzinski.clinic.model

import javax.persistence.*

@Entity
class Address(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long?,
        var city: String?,
        var postcode: String?,
        var street: String?,
        val houseNo: String?

) {
    constructor() : this(null, null, null, null, null)
}
