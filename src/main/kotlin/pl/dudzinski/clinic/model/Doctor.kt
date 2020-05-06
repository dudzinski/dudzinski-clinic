package pl.dudzinski.clinic.model

import javax.persistence.*

@Entity
class Doctor(

        name: String,
        surname: String,

        @Column(nullable = false)
        var specialization: String,

        @OneToMany(fetch = FetchType.LAZY)
        @JoinColumn(name = "doctor_id")
        var appointments: List<Appointment> = emptyList()

) : Person(null, name, surname)
