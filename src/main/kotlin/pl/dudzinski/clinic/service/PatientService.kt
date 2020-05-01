package pl.dudzinski.clinic.service

import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.dudzinski.clinic.handler.PatientDTO
import pl.dudzinski.clinic.handler.ProtocolMapper
import pl.dudzinski.clinic.respositories.PatientRepository
import java.util.*

@Service
@Transactional
class PatientService(private val patientRepository: PatientRepository,
                     private val protocolMapper: ProtocolMapper) {

    companion object {
        private val logger = LoggerFactory.getLogger(PatientService::class.java)
    }

    fun savePatient(patientDTO: PatientDTO): PatientDTO {
        val patient = patientRepository.save(protocolMapper.toPatient(patientDTO))
        return protocolMapper.toPatientDTO(patient)
    }

    fun getPatient(id: Long): Optional<PatientDTO> {
        val patient = patientRepository.findByIdOrNull(id)
        return if (patient != null) Optional.of(protocolMapper.toPatientDTO(patient)) else Optional.empty()
    }

    fun getAllPatients(): List<PatientDTO> = patientRepository.findAll().map { protocolMapper.toPatientDTO(it) }

    fun deletePatient(id: Long) {
        patientRepository.deleteById(id)
    }

    fun updatePatient(id: Long, patientDTO: PatientDTO) {
        val optionalPatient = patientRepository.findById(id)
        optionalPatient.ifPresent {
            it.name = patientDTO.name
            it.surname = patientDTO.surname
        }
    }

    fun existPatient(id: Long): Boolean = patientRepository.existsById(id)
}
