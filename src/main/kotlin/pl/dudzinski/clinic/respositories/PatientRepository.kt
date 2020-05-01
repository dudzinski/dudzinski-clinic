package pl.dudzinski.clinic.respositories

import org.springframework.data.repository.CrudRepository
import pl.dudzinski.clinic.model.Patient

interface PatientRepository : CrudRepository<Patient, Long>
