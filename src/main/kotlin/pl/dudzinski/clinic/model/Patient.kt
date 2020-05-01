package pl.dudzinski.clinic.model

import javax.persistence.*

@Entity
data class Patient(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long?,

        @Column(nullable = false)
        var name: String?,

        @Column(nullable = false)
        var surname: String?,

        @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = [CascadeType.ALL])
        var address: List<Address>? = emptyList()
) {
    constructor() : this(null, null, null, null)
}
