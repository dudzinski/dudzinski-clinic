package pl.dudzinski.clinic.handler

import pl.dudzinski.clinic.model.*
import java.util.*

fun AppointmentDTO.toAppointment(): Appointment {
    return Appointment(patient.toPatient(), doctor.toDoctor(), clinic.toClinic(), Date(date))
}

fun Appointment.toAppointmentDTO(): AppointmentDTO {
    return AppointmentDTO(id, patient.toPatientDTO(), doctor.toDoctorDTO(), clinic.toClinicDTO(), date.time)
}

fun DoctorDTO.toDoctor(): Doctor {
    return Doctor(name, surname, specialization, emptyList())
}

fun Doctor.toDoctorDTO(): DoctorDTO {
    return DoctorDTO(id, name, surname, specialization)
}

fun PatientDTO.toPatient(): Patient {
    return Patient(name, surname, emptyList(), address?.toAddress())
}

fun Patient.toPatientDTO(): PatientDTO {
    return PatientDTO(id, name, surname, address?.toAddressDTO())
}

fun AddressDTO.toAddress(): Address {
    return Address(id, city, postcode, street, houseNo)
}

fun Address.toAddressDTO(): AddressDTO {
    return AddressDTO(id, city, postcode, street, houseNo)
}

fun Clinic.toClinicDTO(): ClinicDTO {
    return ClinicDTO(id, name, address?.toAddressDTO())
}

fun ClinicDTO.toClinic(): Clinic {
    return Clinic(id, name, address?.toAddress())
}
