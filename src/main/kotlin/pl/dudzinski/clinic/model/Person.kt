package pl.dudzinski.clinic.model

import javax.persistence.*

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
abstract class Person(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long?,

        @Column(nullable = false)
        var name: String,

        @Column(nullable = false)
        var surname: String
)
