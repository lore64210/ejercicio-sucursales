package com.fravega.sucursal.repository

import com.fravega.sucursal.domain.Sucursal
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface SucursalRepository : MongoRepository<Sucursal, String> {

    fun findById(id: ObjectId) : Sucursal

}