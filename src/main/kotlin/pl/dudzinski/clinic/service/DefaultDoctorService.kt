package pl.dudzinski.clinic.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import pl.dudzinski.clinic.handler.DoctorDTO
import pl.dudzinski.clinic.handler.toDoctor
import pl.dudzinski.clinic.handler.toDoctorDTO
import pl.dudzinski.clinic.handler.toPatientDTO
import pl.dudzinski.clinic.model.Doctor
import pl.dudzinski.clinic.respositories.AppointmentRepository
import pl.dudzinski.clinic.respositories.DoctorRepository
import java.util.*

@Service
@Transactional
class DefaultDoctorService(private val appointmentRepository: AppointmentRepository,
                           private val doctorRepository: DoctorRepository) : DoctorService {

    override fun saveDoctor(doctorDTO: DoctorDTO): DoctorDTO {
        return doctorRepository.save(doctorDTO.toDoctor()).toDoctorDTO()
    }

    override fun getDoctor(id: Long): DoctorDTO {
        return doctorRepository.findById(id).orElseThrow {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find doctor with id: $id")
        }.toDoctorDTO()
    }

    override fun getAllDoctors(): List<DoctorDTO> = doctorRepository.findAll().map(Doctor::toDoctorDTO)

    override fun deleteDoctor(id: Long) {
        val doctor = doctorRepository.findById(id).orElseThrow {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find doctor with id: $id")
        }
        appointmentRepository.deleteAll(doctor.appointments)
        doctorRepository.delete(doctor)
    }

    override fun updateDoctor(id: Long, doctorDTO: DoctorDTO) {
        val doctor = doctorRepository.findById(id).orElseThrow {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find doctor with id: $id")
        }
        doctor.name = doctorDTO.name
        doctor.surname = doctorDTO.surname
        doctor.specialization = doctorDTO.specialization
    }

    override fun existDoctor(id: Long): Boolean = doctorRepository.existsById(id)
}
