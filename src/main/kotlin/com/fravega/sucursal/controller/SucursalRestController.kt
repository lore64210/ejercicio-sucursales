package com.fravega.sucursal.controller

import com.fravega.sucursal.domain.Sucursal
import com.fravega.sucursal.service.SucursalService
import com.fravega.sucursal.vo.SucursalConDistanciaVo
import com.fravega.sucursal.vo.SucursalRequestVo
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/sucursal")
class SucursalRestController(val sucursalService : SucursalService) {

    @GetMapping(produces=["application/json"])
    fun buscarTodas() : ResponseEntity<List<Sucursal>> {
        val sucursales : List<Sucursal> = sucursalService.buscarTodas()
        return ResponseEntity.ok(sucursales)
    }

    @GetMapping("/{id}", produces=["application/json"])
    fun buscarPorId(@PathVariable id : String) : ResponseEntity<Sucursal> {
        val sucursal : Sucursal = sucursalService.buscarPorId(id)
        return ResponseEntity.ok(sucursal)
    }

    @GetMapping("/buscarMasCercana" , produces=["application/json"])
    fun buscarMasCercana(@RequestParam latitud : Double, @RequestParam longitud : Double) : ResponseEntity<SucursalConDistanciaVo> {
        val sucursalConDistanciaVo : SucursalConDistanciaVo =  sucursalService.buscarMasCercana(latitud, longitud)
        return ResponseEntity.ok(sucursalConDistanciaVo)
    }

    @PostMapping(produces=["application/json"])
    @ResponseStatus(HttpStatus.CREATED)
    fun crear(@RequestBody sucursalRequestVo : SucursalRequestVo) : Sucursal {
        return sucursalService.crear(sucursalRequestVo)
    }

}