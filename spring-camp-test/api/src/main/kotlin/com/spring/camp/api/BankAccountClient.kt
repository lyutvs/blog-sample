package com.spring.camp.api

interface BankAccountClient {
    fun isMatchedAccount(
        accountNumber: String,
        accountHolder: String,
        bankCode: String,
    ): Boolean
}

class BankAccountClientImpl:BankAccountClient {
    override fun isMatchedAccount(
        accountNumber: String,
        accountHolder: String,
        bankCode: String
    ): Boolean {
        // HTTP 통신 이후 응답
        return false
    }
}

class BankAccountClientMock: BankAccountClient {
    override fun isMatchedAccount(
        accountNumber: String,
        accountHolder: String,
        bankCode: String
    ): Boolean {
        // HTTP 통신 진행 X
        return true
    }
}