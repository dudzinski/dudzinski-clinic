package pl.dudzinski.clinic.service

import com.github.fge.jsonpatch.JsonPatch
import pl.dudzinski.clinic.handler.AppointmentDTO

interface AppointmentService {
    fun getAllAppointments(): List<AppointmentDTO>
    fun getAppointment(id: Long): AppointmentDTO
    fun getAppointmentsForPatient(patientId: Long): List<AppointmentDTO>
    fun saveAppointment(appointmentDTO: AppointmentDTO): AppointmentDTO
    fun deleteAppointment(id: Long)
    fun existAppointment(id: Long): Boolean
    fun updateAppointment(id: Long, appointmentDTO: AppointmentDTO)
    fun patchAppointment(id: Long, patch: JsonPatch)
}
