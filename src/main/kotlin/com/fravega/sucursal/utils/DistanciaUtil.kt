package com.fravega.sucursal.utils

import kotlin.math.pow
import kotlin.math.sqrt

fun obtenerDistancia(latitud1: Double?, longitud1: Double?, latitud2: Double?, longitud2: Double?) : Double {
    return sqrt((latitud1!! - latitud2!!).pow(2.0) +  (longitud1!! - longitud2!!).pow(2.0))
}