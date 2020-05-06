package pl.dudzinski.clinic.respositories

import org.springframework.data.repository.CrudRepository
import pl.dudzinski.clinic.model.Clinic

interface ClinicRepository : CrudRepository<Clinic, Long>
