package pl.dudzinski.clinic.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import pl.dudzinski.clinic.handler.PatientDTO
import pl.dudzinski.clinic.handler.toPatient
import pl.dudzinski.clinic.handler.toPatientDTO
import pl.dudzinski.clinic.model.Patient
import pl.dudzinski.clinic.respositories.PatientRepository

@Service
@Transactional
class DefaultPatientService(private val patientRepository: PatientRepository) : PatientService {

    override fun savePatient(patientDTO: PatientDTO): PatientDTO {
        return patientRepository.save(patientDTO.toPatient()).toPatientDTO()
    }

    override fun getPatient(id: Long): PatientDTO {
        return patientRepository.findById(id).orElseThrow {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find patient with id: $id")
        }.toPatientDTO()
    }

    override fun getAllPatients(): List<PatientDTO> = patientRepository.findAll().map(Patient::toPatientDTO)

    override fun deletePatient(id: Long) {
        val appointment = patientRepository.findById(id).orElseThrow {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find patient with id: $id")
        }
        patientRepository.delete(appointment)
    }

    override fun updatePatient(id: Long, patientDTO: PatientDTO) {
        val patient = patientRepository.findById(id).orElseThrow {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find patient with id: $id")
        }
        patient.name = patientDTO.name
        patient.surname = patientDTO.surname
    }

    override fun existPatient(id: Long): Boolean = patientRepository.existsById(id)
}
