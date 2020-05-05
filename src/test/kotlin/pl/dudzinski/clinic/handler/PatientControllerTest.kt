package pl.dudzinski.clinic.handler

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pl.dudzinski.clinic.AbstractSpringIntegrationTest
import pl.dudzinski.clinic.assertPatient
import pl.dudzinski.clinic.assertPatients

internal class PatientControllerTest : AbstractSpringIntegrationTest() {

    @Test
    fun `get patient which is not exist`() {
        assertResourceNotFound { invoker.getPatient(33L) }
    }

    @Test
    fun `delete patient which is not exist`() {
        assertResourceNotFound { invoker.deletePatient(33L) }
    }

    @Test
    fun `update patient which is not exist`() {
        val patient = PatientDTO("Franek", "Barańczuk")
        assertEquals(HttpStatus.CREATED, invoker.updatePatient(33L, patient)?.statusCode)
    }

    @Test
    fun `save new patient without address`() {
        val patientDTO = PatientDTO("Franek", "Kowalski", null)
        val responseEntity = invoker.savePatient(patientDTO)
        assertEquals(HttpStatus.CREATED, responseEntity!!.statusCode)
    }

    @Test
    fun `save new patient with two addresses and get him`() {
        val patient = PatientDTO("Franek", "Kowalski",
                listOf(AddressDTO("Poznań", "67-798", "oś Jana Sobieskiego", "16E/122"),
                        AddressDTO("Białogard", "78-200", "Nowowiejskiego", "3A/2"))
        )
        assertEquals(HttpStatus.CREATED, invoker.savePatient(patient)!!.statusCode)
        val responseEntity = invoker.getAllPatients()
        assertEquals(HttpStatus.OK, responseEntity.statusCode)

        val savedPatient = responseEntity.body?.firstOrNull()
        assertPatient(patient, savedPatient)
    }

    @Test
    fun `save three patients and get them all`() {
        val patients = listOf(
                PatientDTO("Franek", "Kowalski"),
                PatientDTO("Kazimierz", "Kuligowski"),
                PatientDTO("Mariusz", "Ławiński")
        )

        patients.forEach {
            assertEquals(HttpStatus.CREATED, invoker.savePatient(it)!!.statusCode)
        }

        val responseEntity = invoker.getAllPatients()
        assertEquals(HttpStatus.OK, responseEntity.statusCode)
        assertPatients(patients, responseEntity.body?.toList())
    }

    @Test
    fun `save one patient and delete him by id`() {
        val patient = PatientDTO("Franek", "Kowalski")
        val patientList = listOf(patient)
        val responseEntity = invoker.savePatient(patient)
        assertEquals(HttpStatus.CREATED, responseEntity?.statusCode)

        val responseEntityBeforeDelete = invoker.getAllPatients()
        assertEquals(HttpStatus.OK, responseEntityBeforeDelete.statusCode)
        val allPatientsBeforeDelete = responseEntityBeforeDelete.body?.toList()
        assertPatients(patientList, allPatientsBeforeDelete)

        val deletePatientResponseEntity = invoker.deletePatient(allPatientsBeforeDelete?.first()?.id!!)
        assertEquals(HttpStatus.OK, deletePatientResponseEntity.statusCode)

        val responseEntityAfterDelete = invoker.getAllPatients()
        assertEquals(HttpStatus.OK, responseEntityAfterDelete.statusCode)
        assertEquals(0, responseEntityAfterDelete.body?.size)
    }

    @Test
    fun `save one patient and update him by id`() {
        val patient = PatientDTO("Franek", "Kowalski")
        val responseEntity = invoker.savePatient(patient)
        assertEquals(HttpStatus.CREATED, responseEntity?.statusCode)

        val resourceURI = hostWithPort + responseEntity?.headers?.location.toString()
        val savedPatientEntityResponse = invoker.get(resourceURI, PatientDTO::class.java)
        assertEquals(HttpStatus.OK, savedPatientEntityResponse.statusCode)

        val savedPatient = savedPatientEntityResponse.body
        val patientWithNewData = PatientDTO("Marek", "Dobrzycki")
        invoker.updatePatient(savedPatient?.id!!, patientWithNewData)

        val updatedPatientEntityResponse =
                invoker.get(resourceURI, PatientDTO::class.java)
        assertEquals(HttpStatus.OK, updatedPatientEntityResponse.statusCode)
        val updatedPatient = updatedPatientEntityResponse.body

        assertPatient(patientWithNewData, updatedPatient)
    }
}
