package com.fravega.sucursal.service

import com.fravega.sucursal.domain.Sucursal
import com.fravega.sucursal.vo.SucursalConDistanciaVo
import com.fravega.sucursal.vo.SucursalRequestVo
import org.bson.types.ObjectId

interface SucursalService {

    fun buscarTodas() : List<Sucursal>

    fun buscarPorId(id : String) : Sucursal

    fun buscarMasCercana(latitud : Double, longitud : Double) : SucursalConDistanciaVo

    fun crear(sucursalRequestVo : SucursalRequestVo) : Sucursal

}