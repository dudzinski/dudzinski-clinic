package pl.dudzinski.clinic.handler

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pl.dudzinski.clinic.AbstractSpringIntegrationTest

internal class PatientControllerTest : AbstractSpringIntegrationTest() {

    @Test
    fun `save new patient without address`() {
        val patientDTO = PatientDTO("Franek", "Kowalski", null)

        val responseEntity = invoker.savePatient(patientDTO)
        assertEquals(HttpStatus.CREATED, responseEntity!!.statusCode)
    }

    @Test
    fun `save new patient with two addresses`() {
        val addresses = listOf(
                AddressDTO("Poznań", "67-798", "oś Jana Sobieskiego", "16E/122"),
                AddressDTO("Białogard", "78-200", "Nowowiejskiego", "3A/2")
        )
        val patientDTO = PatientDTO("Franek", "Kowalski", addresses)

        assertEquals(HttpStatus.CREATED, invoker.savePatient(patientDTO)!!.statusCode)

        val responseEntity = invoker.getAllPatients()
        assertEquals(HttpStatus.OK, responseEntity.statusCode)

        val savedPatient = responseEntity.body?.firstOrNull()
        assertNotNull(savedPatient)
        assertNotNull(savedPatient!!.id)
        assertEquals(patientDTO.name, savedPatient.name)
        assertEquals(patientDTO.surname, savedPatient.surname)
        assertNotNull(savedPatient.address)
        assertEquals(addresses.size, savedPatient.address?.size)
    }

    @Test
    fun `save three patients and get them all`() {
        assertEquals(HttpStatus.CREATED, invoker.savePatient(PatientDTO("Franek", "Kowalski", null))!!.statusCode)
        assertEquals(HttpStatus.CREATED, invoker.savePatient(PatientDTO("Kazimierz", "Kuligowski", null))!!.statusCode)
        assertEquals(HttpStatus.CREATED, invoker.savePatient(PatientDTO("Mariusz", "Ławiński", null))!!.statusCode)

        val responseEntity = invoker.getAllPatients()
        assertEquals(HttpStatus.OK, responseEntity.statusCode)

        val allPatients = responseEntity.body
        assertNotNull(allPatients)
        assertEquals(3, allPatients?.size)
    }

    @Test
    fun `save one patient and delete him`() {
        val responseEntity = invoker.savePatient(PatientDTO("Franek", "Kowalski", null))
        assertEquals(HttpStatus.CREATED, responseEntity?.statusCode)

        val responseEntityBeforeDelete = invoker.getAllPatients()
        assertEquals(HttpStatus.OK, responseEntityBeforeDelete.statusCode)
        val allPatientsBeforeDelete = invoker.getAllPatients().body
        assertEquals(1, allPatientsBeforeDelete?.size)

        val deletePatientResponseEntity = invoker.deletePatient(allPatientsBeforeDelete?.first()?.id!!)
        assertEquals(HttpStatus.OK, deletePatientResponseEntity.statusCode)

        val responseEntityAfterDelete = invoker.getAllPatients()
        assertEquals(HttpStatus.OK, responseEntityAfterDelete.statusCode)
        assertEquals(0, responseEntityAfterDelete.body?.size)
    }

    @Test
    fun `save one patient and update him`() {
        val responseEntity = invoker.savePatient(PatientDTO("Franek", "Kowalski", null))
        assertEquals(HttpStatus.CREATED, responseEntity?.statusCode)

        val resourceURI = hostWithPort + responseEntity?.headers?.location.toString();

        val savedPatientEntityResponse =
                invoker.get(resourceURI, PatientDTO::class.java)
        assertEquals(HttpStatus.OK, savedPatientEntityResponse.statusCode)

        val patient = savedPatientEntityResponse.body
        invoker.updatePatient(patient?.id!!, PatientDTO("Marek","Dobrzycki",null))

        val updatedPatientEntiyResponse =
                invoker.get(resourceURI, PatientDTO::class.java)
        assertEquals(HttpStatus.OK, updatedPatientEntiyResponse.statusCode)
        val updatedPatient = updatedPatientEntiyResponse.body

        assertEquals("Marek",updatedPatient?.name)
        assertEquals("Dobrzycki",updatedPatient?.surname)
    }
}

