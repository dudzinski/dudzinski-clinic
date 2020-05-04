package pl.dudzinski.clinic.service

import pl.dudzinski.clinic.handler.DoctorDTO
import java.util.*

interface DoctorService {
    fun saveDoctor(doctorDTO: DoctorDTO): DoctorDTO
    fun getDoctor(id: Long): Optional<DoctorDTO>
    fun getAllDoctors(): List<DoctorDTO>
    fun deleteDoctor(id: Long)
    fun updateDoctor(id: Long, doctorDTO: DoctorDTO)
    fun existDoctor(id: Long): Boolean
}
