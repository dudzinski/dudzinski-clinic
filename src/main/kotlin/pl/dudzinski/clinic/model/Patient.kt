package pl.dudzinski.clinic.model

import javax.persistence.*

@Entity
class Patient(
        name : String,
        surname : String,

        @OneToMany(fetch = FetchType.LAZY)
        @JoinColumn(name = "patient_id")
        var appointments: List<Appointment> = emptyList(),

        @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
        var address: Address?

) : Person(null, name, surname)
