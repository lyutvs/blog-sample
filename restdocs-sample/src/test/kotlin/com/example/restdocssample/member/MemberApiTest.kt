package com.example.restdocssample.member

import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import com.example.restdocssample.ErrorResponse
import com.example.restdocssample.SpringWebTestSupport
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

//@Disabled
class MemberApiTest : SpringWebTestSupport() {

//    @Test
//    fun member_page_test() {
//        mockMvc.perform(
//            get("/api/members")
//                .param("size", "10")
//                .param("page", "0")
//                .contentType(MediaType.APPLICATION_JSON)
//        )
//            .andExpect(MockMvcResultMatchers.status().isOk())
//            .andDo(
//                write.document(
//                    requestParameters(
//                        parameterWithName("size").optional().description("size"),
//                        parameterWithName("page").optional().description("page")
//                    )
//                )
//            )
//    }

    @Test
    fun member_get() {
        // 조회 API -> 대상의 데이터가 있어야 합니다.
        mockMvc.perform(
            get("/api/members/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(
                writeOpenApi(
                    ResourceSnippetParameters
                        .builder()
                        .tag("members")
                        .summary("Member 조회")
                        .description(
                            """
                            * 블라블라
                            * 블라
                            """.trimIndent()
                        )
                        .responseSchema(Schema.schema(MemberResponse::class.java.simpleName))
                        .pathParameters(
                            parameterWithName("id").description("Member ID")
                        )
                        .responseFields(
                            fieldWithPath("id").description("ID").type(JsonFieldType.NUMBER).fieldValidation(MemberResponse::id),
                            fieldWithPath("name").description("asd").type(JsonFieldType.STRING).fieldValidation(MemberResponse::name),
                            fieldWithPath("email").description("email").type(JsonFieldType.STRING).fieldValidation(MemberResponse::email),
                            fieldWithPath("address").description("address").type(JsonFieldType.STRING).fieldValidation(MemberResponse::address),
                            fieldWithPath("status").description("status").type("enum").fieldValidation(MemberResponse::status),
                        )
                        .build()
                )
            )
    }

    @Test
    fun member_get_bad_request() {
        // 잘못된 요청 시뮬레이션 -> 예를 들어, 잘못된 ID나 누락된 필수 정보 등을 전달
        mockMvc.perform(
            get("/api/members/{id}", 5L)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andDo(
                writeOpenApi(
                    ResourceSnippetParameters
                        .builder()
                        .tag("members")
                        .summary("Member 조회 - 잘못된 요청")
                        .description("잘못된 ID 또는 필요한 정보가 누락된 경우에 대한 응답 예시.")
                        .responseSchema(Schema.schema(ErrorResponse::class.java.simpleName))  // 여기에 오류 응답의 스키마를 지정
                        .responseFields(
                            fieldWithPath("message").description("Error Message").type(JsonFieldType.STRING),
                            fieldWithPath("status").description("HTTP Status Code").type(JsonFieldType.NUMBER),
                            fieldWithPath("code").description("Error Code").type(JsonFieldType.STRING),
                            fieldWithPath("errors").description("Error").type(JsonFieldType.ARRAY),
//                            fieldWithPath("errors.field").description("Error field").type(JsonFieldType.STRING),
//                            fieldWithPath("errors.value").description("Error value").type(JsonFieldType.STRING),
//                            fieldWithPath("errors.reason").description("Error reason").type(JsonFieldType.STRING),
                            fieldWithPath("timestamp").description("Error Timestamp").type(JsonFieldType.STRING),
                        )
                        .build()
                )
            )
    }

    @Test
    fun member_create() {
        mockMvc.perform(
            post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(readJson("/json/member-api/member-create.json"))
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(
                writeOpenApi(
                    ResourceSnippetParameters
                        .builder()
                        .tag("members")
                        .summary("Member 생성")
                        .description(
                            """
                            * 블라블라
                            * 블라
                            """.trimIndent()
                        )
                        .requestSchema(Schema.schema(MemberSignUpRequest::class.java.simpleName))
                        .requestFields(
                            fieldWithPath("name").description("asd").type(JsonFieldType.STRING).fieldValidation(MemberSignUpRequest::name),
                            fieldWithPath("email").description("email").type(JsonFieldType.STRING).fieldValidation(MemberSignUpRequest::email),
                            fieldWithPath("status").description("status").type("enum").fieldValidation(MemberSignUpRequest::status),
                        )
                        .build()
                )
            )
    }

//    @Test
//    fun member_modify() {
//        mockMvc.perform(
//            RestDocumentationRequestBuilders.put("/api/members/{id}", 1)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(readJson("/json/member-api/member-modify.json"))
//        )
//            .andExpect(MockMvcResultMatchers.status().isOk())
//            .andDo(
//                write.document(
//                    pathParameters(
//                        parameterWithName("id").description("Member ID")
//                    ),
//                    requestFields(
//                        fieldWithPath("name").description("name").attributes(field("length", "10"))
//                    )
//                )
//            )
//    }
//
//
//    @Test
//    fun member_create_글자_length_실패() {
//        mockMvc.perform(
//            RestDocumentationRequestBuilders.post("/api/members")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(readJson("/json/member-api/member-create-invalid.json"))
//        )
//            .andExpect(MockMvcResultMatchers.status().isBadRequest())
//    }
//
//    @Test
//    fun member_modify_글자_length_실패() {
//        mockMvc.perform(
//            RestDocumentationRequestBuilders.put("/api/members/{id}", 1)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(readJson("/json/member-api/member-modify-invalid.json"))
//        )
//            .andExpect(MockMvcResultMatchers.status().isBadRequest())
//    }


}