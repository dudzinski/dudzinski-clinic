package pl.dudzinski.clinic.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import pl.dudzinski.clinic.handler.AppointmentDTO
import pl.dudzinski.clinic.handler.toAppointmentDTO
import pl.dudzinski.clinic.model.Appointment
import pl.dudzinski.clinic.respositories.AppointmentRepository
import pl.dudzinski.clinic.respositories.DoctorRepository
import pl.dudzinski.clinic.respositories.PatientRepository
import java.util.*


@Service
@Transactional
class AppointmentService(private val appointmentRepository: AppointmentRepository,
                         private val doctorRepository: DoctorRepository,
                         private val patientRepository: PatientRepository,
                         private val objectMapper: ObjectMapper) {


    fun getAllAppointments(): List<AppointmentDTO> {
        return appointmentRepository.findAll().map(Appointment::toAppointmentDTO)
    }

    fun getAppointment(id: Long): AppointmentDTO {
        return appointmentRepository.findById(id).orElseThrow {
            throw ResponseStatusException(NOT_FOUND, "Could not find appointment with id: $id")
        }.toAppointmentDTO()
    }

    fun getAppointmentsForPatient(patientId: Long): List<AppointmentDTO> {
        if (!patientRepository.existsById(patientId)) {
            throw ResponseStatusException(NOT_FOUND, "Could not find patient with id: $patientId")
        }
        return appointmentRepository.findByPatientId(patientId).map(Appointment::toAppointmentDTO)
    }

    fun saveAppointment(appointmentDTO: AppointmentDTO): AppointmentDTO {
        val doctor = appointmentDTO.doctor.id?.let {
            doctorRepository.findById(it).orElseThrow {
                throw ResponseStatusException(NOT_FOUND, "Could not find doctor with id: ${appointmentDTO.doctor.id}")
            }
        }

        val patient = appointmentDTO.patient.id?.let {
            patientRepository.findById(it).orElseThrow {
                throw ResponseStatusException(NOT_FOUND, "Could not find patient with id: ${appointmentDTO.patient.id}")
            }
        }

        val appointment = Appointment(patient!!, doctor!!, Date(appointmentDTO.date))
        return appointmentRepository.save(appointment).toAppointmentDTO()
    }

    fun deleteAppointment(id: Long) {
        val appointment = appointmentRepository.findById(id).orElseThrow {
            throw ResponseStatusException(NOT_FOUND, "Could not find appointment with id: $id")
        }
        appointmentRepository.delete(appointment)
    }

    fun existAppointment(id: Long) = appointmentRepository.existsById(id)

    fun updateAppointment(id: Long, appointmentDTO: AppointmentDTO) {
        val appointment = appointmentRepository.findById(id).orElseThrow {
            throw ResponseStatusException(NOT_FOUND, "Could not find appointment with id: $id")
        }
        appointment.date = appointmentDTO.date()
    }

    fun patchAppointment(id: Long, patch: JsonPatch) {
        val appointment = appointmentRepository.findById(id).orElseThrow {
            throw ResponseStatusException(NOT_FOUND, "Could not find appointment with id: $id")
        }

        val patched: JsonNode = patch.apply(objectMapper.convertValue(appointment.toAppointmentDTO(), JsonNode::class.java))
        val patchedAppointment = objectMapper.treeToValue(patched, AppointmentDTO::class.java)
        appointment.date = patchedAppointment.date()
    }
}
