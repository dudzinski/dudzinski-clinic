package pl.dudzinski.clinic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import pl.dudzinski.clinic.handler.AddressDTO
import pl.dudzinski.clinic.handler.AppointmentDTO
import pl.dudzinski.clinic.handler.DoctorDTO
import pl.dudzinski.clinic.handler.PatientDTO

fun assertAppointments(expectedAppointments: List<AppointmentDTO>?, actualAppointments: List<AppointmentDTO>?) {
    assertTrue(bothNullOrNotNull(expectedAppointments, actualAppointments))
    assertEquals(expectedAppointments!!.size, actualAppointments!!.size)
    val expectedIterator = expectedAppointments.iterator()
    val actualIterator = actualAppointments.iterator()
    while (expectedIterator.hasNext()) {
        assertAppointment(expectedIterator.next(), actualIterator.next())
    }
}

fun assertAppointment(expected: AppointmentDTO?, actual: AppointmentDTO?) {
    assertTrue(bothNullOrNotNull(expected, actual))
    assertEquals(expected!!.date, actual!!.date)
    assertDoctor(expected.doctor, actual.doctor)
    assertPatient(expected.patient, actual.patient)
}

fun assertDoctors(expectedDoctor: List<DoctorDTO>?, actualDoctors: List<DoctorDTO>?) {
    assertTrue(bothNullOrNotNull(expectedDoctor, actualDoctors))
    assertEquals(expectedDoctor!!.size, actualDoctors!!.size)
    val expectedIterator = expectedDoctor.iterator()
    val actualIterator = actualDoctors.iterator()
    while (expectedIterator.hasNext()) {
        assertDoctor(expectedIterator.next(), actualIterator.next())
    }
}

fun assertDoctor(expected: DoctorDTO?, actual: DoctorDTO?) {
    assertTrue(bothNullOrNotNull(expected, actual))
    assertEquals(expected!!.name, actual!!.name)
    assertEquals(expected.surname, actual.surname)
    assertEquals(expected.specialization, actual.specialization)
}

fun assertPatients(expectedPatients: List<PatientDTO>?, actualPatients: List<PatientDTO>?) {
    assertTrue(bothNullOrNotNull(expectedPatients, actualPatients))
    assertEquals(expectedPatients!!.size, actualPatients!!.size)
    val expectedIterator = expectedPatients.iterator()
    val actualIterator = actualPatients.iterator()
    while (expectedIterator.hasNext()) {
        assertPatient(expectedIterator.next(), actualIterator.next())
    }
}

fun assertPatient(expected: PatientDTO?, actual: PatientDTO?) {
    assertTrue(bothNullOrNotNull(expected, actual))
    assertEquals(expected!!.name, actual!!.name)
    assertEquals(expected.surname, actual.surname)
    assertAddresses(expected.addresses, actual.addresses)
}

fun assertAddresses(expectedAddresses: List<AddressDTO>?, actualAddresses: List<AddressDTO>?) {

    assertTrue(bothNullOrNotNull(expectedAddresses, actualAddresses))
    assertEquals(expectedAddresses!!.size, actualAddresses!!.size)
    val expectedIterator = expectedAddresses.iterator()
    val actualIterator = actualAddresses.iterator()
    while (expectedIterator.hasNext()) {
        assertAddress(expectedIterator.next(), actualIterator.next())
    }
}


fun assertAddress(expected: AddressDTO, actual: AddressDTO) {
    assertTrue(bothNullOrNotNull(expected, actual))
    assertEquals(expected.city, actual.city)
    assertEquals(expected.street, actual.street)
    assertEquals(expected.postcode, actual.postcode)
    assertEquals(expected.houseNo, actual.houseNo)
}

fun bothNullOrNotNull(first: Any?, second: Any?): Boolean {
    val firstIsNull = first == null
    val secondIsNull = second == null
    return !firstIsNull.xor(secondIsNull)
}

