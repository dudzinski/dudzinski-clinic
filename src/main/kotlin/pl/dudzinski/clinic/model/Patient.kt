package pl.dudzinski.clinic.model

import javax.persistence.*

@Entity
class Patient(
        name : String,
        surname : String,

        @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
        @JoinColumn(name = "patient_id")
        var appointments: List<Appointment>?,

        @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.ALL])
        @JoinColumn(name = "patient_id") var address: List<Address>?

) : Person(null, name, surname)
