package com.fravega.sucursal.restController

import com.fravega.sucursal.SucursalApplicationTests
import com.fravega.sucursal.domain.Sucursal
import com.fravega.sucursal.exceptions.DetalleError
import com.fravega.sucursal.repository.SucursalRepository
import com.fravega.sucursal.vo.SucursalConDistanciaVo
import com.fravega.sucursal.vo.SucursalRequestVo
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity


class SucursalRestControllerTests @Autowired constructor(val sucursalRepository : SucursalRepository) : SucursalApplicationTests() {

    @BeforeEach
    fun setUp() {
        sucursalRepository.deleteAll()
    }

    private val sucursalPath : String = "/api/sucursal"
    private fun guardarUnaSucursalGenerica() = sucursalRepository.save(Sucursal(ObjectId.get().toString(), "Avenida Siempreviva 123", 1000.0, 1000.0))

    private fun generarSucursalRequest() : HttpEntity<SucursalRequestVo> {
        val sucursalRequestVo = SucursalRequestVo("Nueva direccion", 2000.0, 2000.0)
        val headers = HttpHeaders()
        return HttpEntity(sucursalRequestVo, headers)
    }

    /*
    * ===================================================================================================
    * CASOS PEDIDOS
    * ===================================================================================================
     */

    // CREACION EXITOSA
    @Test
    fun crear_conDatosDeSucursalValidos_deberiaCrearUnaSucursalEnLaBaseDeDatosYDevolverla() {
        val url = getRootUrl() + sucursalPath
        val sucursalRequest: HttpEntity<SucursalRequestVo> = generarSucursalRequest()

        val response : ResponseEntity<Sucursal> = restTemplate.postForEntity(url, sucursalRequest, String::class)

        assertEquals(response.statusCodeValue, 201)
        assertNotNull(response.body)
        assertNotNull(response.body!!.id)
        assertEquals(response.body!!.direccion, sucursalRequest.body!!.direccion)
        assertEquals(response.body!!.latitud, sucursalRequest.body!!.latitud)
        assertEquals(response.body!!.longitud, sucursalRequest.body!!.longitud)
    }

    // CREACION FALLIDA
    @Test
    fun crear_conAlgunCampoDelBodyNull_retornaRespuestaDeError() {
        val url = getRootUrl() + sucursalPath
        val sucursalRequest: HttpEntity<SucursalRequestVo> = generarSucursalRequest()
        sucursalRequest.body?.direccion = null

        val response : ResponseEntity<DetalleError> = restTemplate.postForEntity(url, sucursalRequest, String::class)

        assertEquals(response.statusCodeValue, 400)
        assertNotNull(response.body?.mensaje)
        assertNotNull(response.body?.detalles)
    }

    // BUSQUEDA DE SUCURSAL MAS CERCANA EXITOSA
    @Test
    fun buscarMasCercana_conSucursalesExistentes_retornaLaSucursalMasCercana() {
        val url = getRootUrl() + sucursalPath
        val latitud = 100.0
        val longitud = 200.0
        val distancia = 10.0
        guardarUnaSucursalGenerica()
        val sucursalExistenteCercana : Sucursal = sucursalRepository.save(Sucursal(
                ObjectId.get().toString(),
                "Avenida Siempreviva 123",
                latitud + distancia,
                longitud
        ))

        val response : ResponseEntity<SucursalConDistanciaVo> = restTemplate.getForEntity("$url/buscarMasCercana?latitud=$latitud&longitud=$longitud")

        assertEquals(response.statusCodeValue, 200)
        assertEquals(response.body?.id, sucursalExistenteCercana.id)
        assertEquals(response.body?.distancia, distancia)
    }

    /*
    * ===================================================================================================
    * CASOS EXTRA AGREGADOS
    * ===================================================================================================
     */

    @Test
    fun buscarTodas_conDosSucursalesExistentes_retornaDosSucursales() {
        val url = getRootUrl() + sucursalPath
        guardarUnaSucursalGenerica()
        guardarUnaSucursalGenerica()

        val sucursalesResponse : ResponseEntity<List<Sucursal>> = restTemplate.getForEntity(url, String::class)

        assertEquals(sucursalesResponse.statusCodeValue, 200)
        assertEquals(sucursalesResponse.body?.size, 2)
    }

    @Test
    fun buscarTodas_sinSucursalesExistentes_retornaListaVacia() {
        val url = getRootUrl() + sucursalPath

        val sucursalesResponse : ResponseEntity<List<Sucursal>> = restTemplate.getForEntity(url, String::class)

        assertEquals(sucursalesResponse.statusCodeValue, 200)
        assertEquals(sucursalesResponse.body?.size, 0)
    }

    @Test
    fun buscarPorId_conIdDeSucursalExistente_retornaLaSucursal() {
        val url = getRootUrl() + sucursalPath
        val sucursalExistente : Sucursal = guardarUnaSucursalGenerica()

        val response : ResponseEntity<Sucursal> = restTemplate.getForEntity("$url/${sucursalExistente.id.toString()}", String::class)

        assertEquals(response.statusCodeValue, 200)
        assertEquals(response.body?.id, sucursalExistente.id)
        assertEquals(response.body?.direccion, sucursalExistente.direccion)
        assertEquals(response.body?.latitud, sucursalExistente.latitud)
        assertEquals(response.body?.longitud, sucursalExistente.longitud)
    }

    @Test
    fun buscarPorId_conIdDeSucursalInexistente_retornaRespuestaDeError() {
        val url = getRootUrl() + sucursalPath
        val idInvalido = "idInvalido"

        val response : ResponseEntity<DetalleError> = restTemplate.getForEntity("$url/$idInvalido", String::class)

        assertEquals(response.statusCodeValue, 400)
        assertNotNull(response.body?.mensaje)
        assertNotNull(response.body?.detalles)
    }

    @Test
    fun buscarMasCercana_sinSucursalesExistentes_retornaUnaExcepcion() {
        val url = getRootUrl() + sucursalPath
        val latitud = 100.0
        val longitud = 200.0

        val response : ResponseEntity<DetalleError> = restTemplate.getForEntity("$url/buscarMasCercana?latitud=$latitud&longitud=$longitud")

        assertEquals(response.statusCodeValue, 400)
        assertNotNull(response.body?.mensaje)
        assertNotNull(response.body?.detalles)
    }

}