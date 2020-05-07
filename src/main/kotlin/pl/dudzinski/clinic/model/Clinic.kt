package pl.dudzinski.clinic.model

import javax.persistence.*

@Entity
data class Clinic(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long?,

        var name: String,

        @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
        var address: Address?

) {
    constructor(name: String, address: Address) : this(null, name, address)
}
