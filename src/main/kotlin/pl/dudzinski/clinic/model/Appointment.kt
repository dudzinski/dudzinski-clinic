package pl.dudzinski.clinic.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*

@Entity
class Appointment(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long?,

        @ManyToOne(fetch = FetchType.LAZY)
        @JsonIgnore
        val patient: Patient,

        @ManyToOne(fetch = FetchType.LAZY)
        @JsonIgnore
        val doctor: Doctor,

        @Column(nullable = false)
        var date: Date
) {
    constructor(patient: Patient, doctor: Doctor, date: Date) : this(null, patient, doctor, date)
}
