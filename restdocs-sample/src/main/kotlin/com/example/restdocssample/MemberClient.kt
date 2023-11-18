package com.example.restdocssample

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.lang.IllegalStateException

@Service
class MemberClient(
    private val restTemplate: RestTemplate
) {

    fun getMember(memberId: Long): ResponseEntity<Member> {
        val url = "http://example.com/api/members/$memberId"
        // GET 요청을 보내고 ResponseEntity로 응답을 받음
        return restTemplate.getForEntity(url, Member::class.java)
    }

    fun getMember2(memberId: Long): Member {
        val url = "http://example.com/api/members/$memberId"
        // GET 요청을 보내고 ResponseEntity로 응답을 받음
        return restTemplate.getForObject(url, Member::class.java)!!
    }
}

class MemberKtorClient() {

    val client = HttpClient {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json()
        }
    }


    fun getMember(memberId: Long): ResponseResult<Member> {
        return runBlocking {
            client
                .get("http://example.com/api/members/$memberId")
                .responseResult<Member>()
        }
    }



}

fun xxx() {
    val memberClient = MemberClient(RestTemplate())
    val response = memberClient.getMember(1L) // 1번 회원 조회를 가정


    if (response.statusCode.is2xxSuccessful) {
        // 비즈니스 로직
    } else {
        // 2xx가 아닌 경우의 처리 로직
    }

    val success = Result.success("asd")

    Result.failure<String>(IllegalStateException())

}


data class Member(
    val id: Long,
    val name: String,
    val email: String
)