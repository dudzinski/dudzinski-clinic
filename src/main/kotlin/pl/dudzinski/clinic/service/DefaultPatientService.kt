package pl.dudzinski.clinic.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.dudzinski.clinic.handler.PatientDTO
import pl.dudzinski.clinic.handler.ProtocolMapper
import pl.dudzinski.clinic.respositories.PatientRepository
import java.util.*

@Service
@Transactional
class DefaultPatientService(private val patientRepository: PatientRepository,
                            private val protocolMapper: ProtocolMapper) : PatientService {

    override fun savePatient(patientDTO: PatientDTO): PatientDTO {
        val patient = patientRepository.save(protocolMapper.toPatient(patientDTO))
        return protocolMapper.toPatientDTO(patient)
    }

    override fun getPatient(id: Long): Optional<PatientDTO> {
        val patient = patientRepository.findByIdOrNull(id)
        return if (patient != null) Optional.of(protocolMapper.toPatientDTO(patient)) else Optional.empty()
    }

    override fun getAllPatients(): List<PatientDTO> = patientRepository.findAll().map { protocolMapper.toPatientDTO(it) }

    override fun deletePatient(id: Long) {
        patientRepository.deleteById(id)
    }

    override fun updatePatient(id: Long, patientDTO: PatientDTO) {
        val optionalPatient = patientRepository.findById(id)
        optionalPatient.ifPresent {
            it.name = patientDTO.name
            it.surname = patientDTO.surname
        }
    }

    override fun existPatient(id: Long): Boolean = patientRepository.existsById(id)
}
