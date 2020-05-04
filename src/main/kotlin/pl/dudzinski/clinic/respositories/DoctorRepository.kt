package pl.dudzinski.clinic.respositories

import org.springframework.data.repository.CrudRepository
import pl.dudzinski.clinic.model.Doctor

interface DoctorRepository : CrudRepository<Doctor, Long>
