package pl.dudzinski.clinic.respositories

import org.springframework.data.repository.CrudRepository
import pl.dudzinski.clinic.model.Appointment

interface AppointmentRepository : CrudRepository<Appointment, Long>{
    fun findByPatientId(id : Long) : List<Appointment>
}
