package pl.dudzinski.clinic.handler

import pl.dudzinski.clinic.model.Address
import pl.dudzinski.clinic.model.Appointment
import pl.dudzinski.clinic.model.Doctor
import pl.dudzinski.clinic.model.Patient
import java.util.*

fun AppointmentDTO.toAppointment(): Appointment {
    return Appointment(patient.toPatient(), doctor.toDoctor(), Date(date))
}

fun Appointment.toAppointmentDTO(): AppointmentDTO {
    return AppointmentDTO(id, patient.toPatientDTO(), doctor.toDoctorDTO(), date.time)
}

fun DoctorDTO.toDoctor(): Doctor {
    return Doctor(name, surname, specialization, null)
}

fun Doctor.toDoctorDTO(): DoctorDTO {
    return DoctorDTO(id, name, surname, specialization)
}

fun PatientDTO.toPatient(): Patient {
    return Patient(name, surname, null, addresses?.map { it.toAddress() })
}

fun Patient.toPatientDTO(): PatientDTO {
    return PatientDTO(id, name, surname, address?.map(Address::toAddressDTO))
}

fun AddressDTO.toAddress(): Address {
    return Address(id, city, postcode, street, houseNo)
}

fun Address.toAddressDTO(): AddressDTO {
    return AddressDTO(id, city, postcode, street, houseNo)
}
