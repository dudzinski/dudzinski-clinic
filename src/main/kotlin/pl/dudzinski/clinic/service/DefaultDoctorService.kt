package pl.dudzinski.clinic.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.dudzinski.clinic.handler.DoctorDTO
import pl.dudzinski.clinic.handler.ProtocolMapper
import pl.dudzinski.clinic.respositories.DoctorRepository
import java.util.*

@Service
@Transactional
class DefaultDoctorService(private val doctorRepository: DoctorRepository,
                           private val protocolMapper: ProtocolMapper) : DoctorService {

    override fun saveDoctor(doctorDTO: DoctorDTO): DoctorDTO {
        val doctor = doctorRepository.save(protocolMapper.toDoctor(doctorDTO))
        return protocolMapper.toDoctorDTO(doctor)
    }

    override fun getDoctor(id: Long): Optional<DoctorDTO> {
        val doctor = doctorRepository.findByIdOrNull(id)
        return if (doctor != null) Optional.of(protocolMapper.toDoctorDTO(doctor)) else Optional.empty()
    }

    override fun getAllDoctors(): List<DoctorDTO> = doctorRepository.findAll().map { protocolMapper.toDoctorDTO(it) }

    override fun deleteDoctor(id: Long) {
        doctorRepository.deleteById(id)
    }

    override fun updateDoctor(id: Long, doctorDTO: DoctorDTO) {
        val optionalDoctor = doctorRepository.findById(id)
        optionalDoctor.ifPresent {
            it.name = doctorDTO.name
            it.surname = doctorDTO.surname
            it.specialization = doctorDTO.specialization!!
        }
    }

    override fun existDoctor(id: Long): Boolean = doctorRepository.existsById(id)
}
