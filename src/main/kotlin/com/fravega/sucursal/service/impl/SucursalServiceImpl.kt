package com.fravega.sucursal.service.impl

import com.fravega.sucursal.domain.Sucursal
import com.fravega.sucursal.exceptions.ParametrosIncompletosException
import com.fravega.sucursal.exceptions.SinSucursalesParaCompararException
import com.fravega.sucursal.exceptions.SucursalInexistenteException
import com.fravega.sucursal.repository.SucursalRepository
import com.fravega.sucursal.service.SucursalService
import com.fravega.sucursal.utils.obtenerDistancia
import com.fravega.sucursal.vo.SucursalConDistanciaVo
import com.fravega.sucursal.vo.SucursalRequestVo
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.NoSuchElementException

@Service
class SucursalServiceImpl(private val sucursalRepository : SucursalRepository) : SucursalService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun buscarPorId(id: String) : Sucursal {
        logger.info("Iniciando busqueda de sucursal con id: $id.")
        try {
            val objectId = ObjectId(id)
            val sucursal : Sucursal = sucursalRepository.findById(objectId)
            logger.info("Se realizo con exito la busqueda de la sucursal con id: $id.")
            return sucursal
        } catch (ex : Exception) {
            when(ex) {
                is IllegalArgumentException, is NoSuchElementException -> {
                    lanzarExcepcion("El id no corresponde a una sucursal existente.") {
                        mensaje : String -> throw SucursalInexistenteException(mensaje)
                    }
                }
            }
            throw ex
        }
    }

    override fun buscarTodas() : List<Sucursal> {
        logger.info("Buscando todas las sucursales.")
        val listaSucursales : List<Sucursal> = sucursalRepository.findAll();
        logger.info("Se encontraron ${listaSucursales.size} sucursales.")
        return listaSucursales;
    }

    override fun buscarMasCercana(latitud: Double, longitud: Double): SucursalConDistanciaVo {
        logger.info("Buscando la sucursal mas cercana a estas cordenadas: ($latitud, $longitud)")
        val sucursalesConDistancia : MutableList<SucursalConDistanciaVo> = sucursalRepository.findAll()
                .map { it.mapearASucursalConDistanciaVo(obtenerDistancia(latitud, longitud, it.latitud, it.longitud)) }
                .toMutableList()
        if(sucursalesConDistancia.isEmpty()) {
            lanzarExcepcion("No existen sucursales en la base de datos") {
                mensaje : String -> throw SinSucursalesParaCompararException(mensaje)
            }
        }
        sucursalesConDistancia.sortBy { it.distancia }
        val sucursalMasCercana : SucursalConDistanciaVo = sucursalesConDistancia.first()
        logger.info("Se encontro la sucursal mas cercana con id: ${sucursalMasCercana.id}")
        return sucursalMasCercana;
    }

    override fun crear(sucursalRequestVo: SucursalRequestVo): Sucursal {
        logger.info("Creando sucursal con el Body: $sucursalRequestVo")
        if (sucursalRequestVo.esInvalido()) {
            lanzarExcepcion("Todos los campos del body de sucursal son obligatorios") {
                mensaje: String -> throw ParametrosIncompletosException(mensaje)
            }
        }
        val sucursal : Sucursal = sucursalRequestVo.mapearASucursal()
        val sucursalCreada : Sucursal = sucursalRepository.save(sucursal)
        logger.info("Se creo una nueva sucursal con id: ${sucursalCreada.id}")
        return sucursalCreada
    }

    @Throws(Exception::class)
    private fun lanzarExcepcion(mensajeError : String, excepcion : (String) -> Unit) {
        logger.error("Ocurrio un error: $mensajeError")
        excepcion(mensajeError)
    }

    private fun SucursalRequestVo.mapearASucursal() = Sucursal(
        direccion = direccion,
        latitud = latitud,
        longitud = longitud
    )

    private fun Sucursal.mapearASucursalConDistanciaVo(distancia : Double) = SucursalConDistanciaVo(
        id = id,
        direccion = direccion,
        latitud = latitud,
        longitud = longitud,
        distancia = distancia
    )

    private fun SucursalRequestVo.esInvalido() : Boolean { return direccion == "" || direccion == null || latitud == null || longitud == null }

}