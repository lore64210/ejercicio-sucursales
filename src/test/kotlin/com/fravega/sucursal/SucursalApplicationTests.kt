package com.fravega.sucursal

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.TestPropertySource

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
class SucursalApplicationTests() {

	protected val restTemplate : TestRestTemplate = TestRestTemplate()

	@LocalServerPort
	protected var port: Int = 0

	protected fun getRootUrl(): String? = "http://localhost:$port"

}
