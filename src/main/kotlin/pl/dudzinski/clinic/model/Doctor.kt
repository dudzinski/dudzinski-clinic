package pl.dudzinski.clinic.model

import javax.persistence.Entity

@Entity
class Doctor(var specialization : String?) : Person(null, null, null) {
    constructor() : this(null)
}
