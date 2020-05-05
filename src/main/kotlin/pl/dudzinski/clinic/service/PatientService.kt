package pl.dudzinski.clinic.service

import pl.dudzinski.clinic.handler.PatientDTO
import java.util.*

interface PatientService {
    fun savePatient(patientDTO: PatientDTO): PatientDTO
    fun getPatient(id: Long): PatientDTO
    fun getAllPatients(): List<PatientDTO>
    fun deletePatient(id: Long)
    fun updatePatient(id: Long, patientDTO: PatientDTO)
    fun existPatient(id: Long): Boolean
}
