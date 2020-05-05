package pl.dudzinski.clinic.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToMany

@Entity
class Doctor(

        name: String,
        surname: String,

        @Column(nullable = false)
        var specialization: String,

        @OneToMany
        @JoinColumn(name = "doctor_id")
        var appointments: List<Appointment>?

) : Person(null, name, surname)
