package pl.dudzinski.clinic.model

import javax.persistence.*

@Entity
class Patient(
        @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.ALL])
        @JoinColumn(name = "patient_id")
        var address: List<Address>? = emptyList()
) : Person(null, null, null)
