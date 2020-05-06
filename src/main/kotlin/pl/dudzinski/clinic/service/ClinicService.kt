package pl.dudzinski.clinic.service

import pl.dudzinski.clinic.handler.ClinicDTO
import java.util.*

interface ClinicService {
    fun saveClinic(clinicDTO: ClinicDTO): ClinicDTO
    fun getClinic(id: Long): ClinicDTO
    fun getAllClinics(): List<ClinicDTO>
    fun deleteClinic(id: Long)
    fun updateClinic(id: Long, clinicDTO: ClinicDTO)
    fun existClinic(id: Long): Boolean
}
