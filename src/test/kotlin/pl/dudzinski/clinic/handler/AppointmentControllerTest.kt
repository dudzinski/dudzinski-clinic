package pl.dudzinski.clinic.handler

import org.joda.time.DateTime
import org.joda.time.LocalDateTime.now
import org.joda.time.Period
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pl.dudzinski.clinic.AbstractSpringIntegrationTest
import pl.dudzinski.clinic.assertAppointment
import pl.dudzinski.clinic.assertAppointments
import java.util.*


internal class AppointmentControllerTest : AbstractSpringIntegrationTest() {

    @BeforeEach
    fun `add some doctors and patient`() {
        assertEquals(HttpStatus.CREATED, invoker.saveDoctor(DoctorDTO("Tomasz", "Barańczuk", "Dentysta"))!!.statusCode)
        assertEquals(HttpStatus.CREATED, invoker.saveDoctor(DoctorDTO("Marek", "Dziuba", "Dermatolog"))!!.statusCode)
        assertEquals(HttpStatus.CREATED, invoker.saveDoctor(DoctorDTO("Zbigniew", "Krawiec", "Kardiolog"))!!.statusCode)

        assertEquals(HttpStatus.CREATED, invoker.savePatient(PatientDTO("Franek", "Kowalski", null))!!.statusCode)
        assertEquals(HttpStatus.CREATED, invoker.savePatient(PatientDTO("Piotr", "Dudziński", null))!!.statusCode)
    }

    @Test
    fun `get appointment which is not exist`() {
        assertResourceNotFound { invoker.getAppointment(33L) }
    }

    @Test
    fun `delete appointment which is not exist`() {
        assertResourceNotFound { invoker.deleteAppointment(33L) }
    }

    @Test
    fun `update appointment which is not exist`() {
        val patient = invoker.getAllPatients().body?.first()!!
        val doctor = invoker.getAllDoctors().body?.first()!!

        val appointment = AppointmentDTO(33L,patient, doctor, DateTime.now().millis)
        assertEquals(HttpStatus.CREATED, invoker.updateAppointment(33L, appointment)?.statusCode)
    }


    @Test
    fun `save new appointment`() {
        val patient = invoker.getAllPatients().body?.first()!!
        val doctor = invoker.getAllDoctors().body?.first()!!

        val appointment = AppointmentDTO(patient, doctor, Date())
        assertEquals(HttpStatus.CREATED, invoker.saveAppointment(appointment)!!.statusCode)

        val appointments = invoker.getAllAppointments().body
        assertEquals(1, appointments?.size)
        val savedAppointment = appointments?.first()

        assertAppointment(appointment, savedAppointment)
    }

    @Test
    fun `save new appointment and patch date`() {
        val patient = invoker.getAllPatients().body?.first()!!
        val doctor = invoker.getAllDoctors().body?.first()!!

        val appointment = AppointmentDTO(patient, doctor, Date())
        assertEquals(HttpStatus.CREATED, invoker.saveAppointment(appointment)!!.statusCode)

        val savedAppointment = invoker.getAllAppointments().body?.first()
        assertAppointment(appointment, savedAppointment)

        appointment.date = now().plus(Period().withDays(10)).toDate().time
        val patchBody = """[ { "op": "replace", "path": "/date", "value": "${appointment.date}" }]"""
        val patchResponseEntity = invoker.patchAppointment(savedAppointment?.id!!, patchBody)
        assertEquals(HttpStatus.OK, patchResponseEntity?.statusCode)
        val actual = invoker.getAppointment(savedAppointment.id!!).body
        assertAppointment(appointment, actual)
    }

    @Test
    fun `save a few appointments and get them all`() {
        val patient = invoker.getAllPatients().body?.first()!!
        val doctors = invoker.getAllDoctors().body
        val iterator = doctors?.iterator()

        val appointments = listOf(
                AppointmentDTO(patient, iterator?.next()!!, Date()),
                AppointmentDTO(patient, iterator.next(), now().plus(Period().withDays(10)).toDate())
        )
        appointments.forEach {
            assertEquals(HttpStatus.CREATED, invoker.saveAppointment(it)!!.statusCode)
        }

        val getAllAppointmentsResponseEntity = invoker.getAllAppointments()
        assertEquals(HttpStatus.OK, getAllAppointmentsResponseEntity.statusCode)
        assertAppointments(appointments, getAllAppointmentsResponseEntity.body?.toList())
    }

    @Test
    fun `save new appointment and delete it`() {
        val patient = invoker.getAllPatients().body?.first()!!
        val doctor = invoker.getAllDoctors().body?.first()!!

        val appointment = AppointmentDTO(patient, doctor, Date())
        val responseEntity = invoker.saveAppointment(appointment)
        assertEquals(HttpStatus.CREATED, responseEntity!!.statusCode)

        val resourceURI = hostWithPort + responseEntity.headers.location.toString()
        val getAppointmentResponseEntity = invoker.get(resourceURI, AppointmentDTO::class.java)
        assertAppointment(appointment, getAppointmentResponseEntity.body)

        val appointmentId = getAppointmentResponseEntity.body?.id!!
        val deleteAppointmentResponseEntity = invoker.deleteAppointment(appointmentId)

        assertEquals(HttpStatus.OK, deleteAppointmentResponseEntity.statusCode)
        assertResourceNotFound { invoker.getAppointment(appointmentId) }
    }

    @Test
    fun `save a few appointments for one patient get them all`() {
        val patient = invoker.getAllPatients().body?.first()!!
        val doctors = invoker.getAllDoctors().body
        val doctorsIterator = doctors?.iterator()

        val appointments = listOf(
                AppointmentDTO(patient, doctorsIterator?.next()!!, Date()),
                AppointmentDTO(patient, doctorsIterator.next(), now().plus(Period().withDays(10)).toDate()),
                AppointmentDTO(patient, doctorsIterator.next(), now().plus(Period().withDays(16)).toDate())

        )

        appointments.forEach {
            assertEquals(HttpStatus.CREATED, invoker.saveAppointment(it)!!.statusCode)
        }

        val getAllAppointmentsResponseEntity = invoker.getAllAppointmentsForPatient(patient.id!!)
        assertEquals(HttpStatus.OK, getAllAppointmentsResponseEntity.statusCode)
        assertAppointments(appointments, getAllAppointmentsResponseEntity.body?.toList()!!)
    }
}
