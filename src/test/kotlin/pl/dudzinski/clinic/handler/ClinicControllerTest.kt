package pl.dudzinski.clinic.handler

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pl.dudzinski.clinic.AbstractSpringIntegrationTest
import pl.dudzinski.clinic.assertClinic
import pl.dudzinski.clinic.assertClinics

internal class ClinicControllerTest : AbstractSpringIntegrationTest() {

    @Test
    fun `get clinic which is not exist`() {
        assertResourceNotFound { invoker.getClinic(33L) }
    }

    @Test
    fun `delete clinic which is not exist`() {
        assertResourceNotFound { invoker.deleteClinic(33L) }
    }

    @Test
    fun `update clinic which is not exist`() {
        val clinic = ClinicDTO("Medical Magnus Clinic",
                AddressDTO("Łódź", "90-552", "Kopernika", "38"))
        assertEquals(HttpStatus.CREATED, invoker.updateClinic(33L, clinic)?.statusCode)
    }

    @Test
    fun `save new clinic and get him by id`() {
        val clinic = ClinicDTO("Medical Magnus Clinic",
                AddressDTO("Łódź", "90-552", "Kopernika", "38"))

        val responseEntity = invoker.saveClinic(clinic)
        assertEquals(HttpStatus.CREATED, responseEntity!!.statusCode)

        val resourceURI = hostWithPort + responseEntity.headers.location.toString()
        val savedClinicEntityResponse = invoker.get(resourceURI, ClinicDTO::class.java)
        assertClinic(clinic, savedClinicEntityResponse.body)
    }

    @Test
    fun `save three clinics and get them all`() {
        val clinics = listOf(
                ClinicDTO("Medical Magnus Clinic",
                        AddressDTO("Łódź", "90-552", "Kopernika", "38")),
                ClinicDTO("Medical Magnus Clinic",
                        AddressDTO("Łódź", "90-552", "Kopernika", "38")),
                ClinicDTO("Medical Magnus Clinic",
                        AddressDTO("Łódź", "90-552", "Kopernika", "38"))
        )
        clinics.forEach { assertEquals(HttpStatus.CREATED, invoker.saveClinic(it)!!.statusCode) }

        val responseEntity = invoker.getAllClinics()
        assertEquals(HttpStatus.OK, responseEntity.statusCode)
        assertClinics(clinics, responseEntity.body?.toList())
    }

    @Test
    fun `save one clinic and delete him`() {
        val clinic =  ClinicDTO("Medical Magnus Clinic",
                AddressDTO("Łódź", "90-552", "Kopernika", "38"))

        val responseEntity = invoker.saveClinic(clinic)
        assertEquals(HttpStatus.CREATED, responseEntity!!.statusCode)

        val resourceURI = hostWithPort + responseEntity.headers.location.toString()
        val getClinicEntityResponse = invoker.get(resourceURI, ClinicDTO::class.java)
        assertClinic(clinic, getClinicEntityResponse.body)

        val clinicId = getClinicEntityResponse.body?.id!!
        val deleteclinicResponseEntity = invoker.deleteClinic(clinicId)

        assertEquals(HttpStatus.OK, deleteclinicResponseEntity.statusCode)
        assertResourceNotFound { invoker.getClinic(clinicId) }
    }

    @Test
    fun `save one clinic and update him`() {
        val address = AddressDTO("Łódź", "90-552", "Kopernika", "38")
        val clinic =  ClinicDTO("Medical Magnus Clinic", address)
        val responseEntity = invoker.saveClinic(clinic)
        assertEquals(HttpStatus.CREATED, responseEntity!!.statusCode)

        val resourceURI = hostWithPort + responseEntity.headers.location.toString()
        val getClinicEntityResponse = invoker.get(resourceURI, ClinicDTO::class.java)
        assertClinic(clinic, getClinicEntityResponse.body)

        val clinicId = getClinicEntityResponse.body?.id!!
        val clinicWithNewData =  ClinicDTO("Nowa Klinika", address)
        invoker.updateClinic(clinicId, clinicWithNewData)

        val updateClinicEntityResponse = invoker.get(resourceURI, ClinicDTO::class.java)
        assertEquals(HttpStatus.OK, updateClinicEntityResponse.statusCode)
        assertClinic(clinicWithNewData, updateClinicEntityResponse.body)
    }
}


