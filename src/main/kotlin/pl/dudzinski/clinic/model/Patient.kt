package pl.dudzinski.clinic.model

import javax.persistence.*

@Entity
data class Patient(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long?,

        @Column(nullable = false)
        var name: String,

        @Column(nullable = false)
        var surname: String,

        @OneToMany(fetch = FetchType.LAZY)
        @JoinColumn(name = "patient_id")
        var appointments: List<Appointment> = emptyList(),

        @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
        var address: Address?
) {
    constructor(name: String, surname: String, appointments: List<Appointment>, address: Address?) :
            this(null, name, surname, appointments, address)
}
