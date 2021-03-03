package com.fravega.sucursal.controller.advice

import com.fravega.sucursal.exceptions.ParametrosIncompletosException
import com.fravega.sucursal.exceptions.DetalleError
import com.fravega.sucursal.exceptions.SinSucursalesParaCompararException
import com.fravega.sucursal.exceptions.SucursalInexistenteException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*

@ControllerAdvice
class ControllerAdviceRequestError : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [(SucursalInexistenteException::class)])
    fun handleSucursalInexistenteException(ex: SucursalInexistenteException,request: WebRequest): ResponseEntity<DetalleError> {
        val errorDetails = DetalleError(
                Date(),
                "Fallo la validacion del id",
                ex.message!!
        )
        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [(ParametrosIncompletosException::class)])
    fun handleParametrosIncompletosException(ex: ParametrosIncompletosException, request: WebRequest): ResponseEntity<DetalleError> {
        val errorDetails = DetalleError(
                Date(),
                "Fallo la validacion de los parametros ingresados.",
                ex.message!!
        )
        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [(SinSucursalesParaCompararException::class)])
    fun handleSinSucursalesParaCompararException(ex: SinSucursalesParaCompararException,request: WebRequest): ResponseEntity<DetalleError> {
        val errorDetails = DetalleError(
                Date(),
                "Fallo la comparacion de distancias",
                ex.message!!
        )
        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }


}