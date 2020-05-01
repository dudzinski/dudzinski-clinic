package pl.dudzinski.clinic.handler

import org.mapstruct.Mapper
import pl.dudzinski.clinic.model.Address
import pl.dudzinski.clinic.model.Patient

@Mapper(componentModel = "spring")
interface ProtocolMapper {

    fun toPatientDTO(patient: Patient): PatientDTO
    fun toPatient(patientDTO: PatientDTO): Patient

    fun toAddressDTO(address: Address): AddressDTO
    fun toAddress(addressDTO: AddressDTO): Address

}
