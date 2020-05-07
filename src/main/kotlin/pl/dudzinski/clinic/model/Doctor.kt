package pl.dudzinski.clinic.model

import javax.persistence.*

@Entity
data class Doctor(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long?,

        @Column(nullable = false)
        var name: String,

        @Column(nullable = false)
        var surname: String,

        @Column(nullable = false)
        var specialization: String,

        @OneToMany(fetch = FetchType.LAZY)
        @JoinColumn(name = "doctor_id")
        var appointments: List<Appointment> = emptyList()

) {
    constructor(name: String, surname: String, specialization: String, appointments: List<Appointment>) :
            this(null, name, surname, specialization, appointments)
}
