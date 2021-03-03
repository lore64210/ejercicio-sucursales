package com.fravega.sucursal.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fravega.sucursal.vo.SucursalRequestVo
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Sucursal (
    @Id
    val id : String = ObjectId.get().toString(),
    val direccion : String?,
    val latitud : Double?,
    val longitud : Double?
)
