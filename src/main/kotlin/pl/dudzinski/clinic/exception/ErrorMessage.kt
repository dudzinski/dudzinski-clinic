package pl.dudzinski.clinic.exception

import org.springframework.web.server.ResponseStatusException

class ErrorMessage {
    var statusCode: Int? = null
    var message: String? = null

    constructor() {}
    constructor(message: String?) {
        this.message = message
    }

    constructor(statusCode: Int?, message: String?) {
        this.statusCode = statusCode
        this.message = message
    }

    override fun toString(): String {
        return "ErrorMessage [errorCode=$statusCode, message=$message]"
    }

    companion object {
        fun fromResponseStatusException(e: ResponseStatusException): ErrorMessage {
            return ErrorMessage(e.status.value(), e.message)
        }
    }
}
