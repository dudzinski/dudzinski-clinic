package pl.dudzinski.clinic.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import pl.dudzinski.clinic.handler.ClinicDTO
import pl.dudzinski.clinic.handler.toClinic
import pl.dudzinski.clinic.handler.toClinicDTO
import pl.dudzinski.clinic.model.Clinic
import pl.dudzinski.clinic.respositories.ClinicRepository

@Service
@Transactional
class DefaultClinicService(private val clinicRepository: ClinicRepository) : ClinicService {

    override fun saveClinic(clinicDTO: ClinicDTO): ClinicDTO {
        return clinicRepository.save(clinicDTO.toClinic()).toClinicDTO()
    }

    override fun getClinic(id: Long): ClinicDTO {
        return clinicRepository.findById(id).orElseThrow {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find clinic with id: $id")
        }.toClinicDTO()
    }

    override fun getAllClinics(): List<ClinicDTO> = clinicRepository.findAll().map(Clinic::toClinicDTO)

    override fun deleteClinic(id: Long) {
        val clinic = clinicRepository.findById(id).orElseThrow {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find clinic with id: $id")
        }
        clinicRepository.delete(clinic)
    }

    override fun updateClinic(id: Long, clinicDTO: ClinicDTO) {
        val clinic = clinicRepository.findById(id).orElseThrow {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find clinic with id: $id")
        }
        clinic.name = clinicDTO.name
    }

    override fun existClinic(id: Long): Boolean = clinicRepository.existsById(id)
}
