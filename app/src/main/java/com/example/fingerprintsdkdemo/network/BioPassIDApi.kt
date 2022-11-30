package com.example.fingerprintsdkdemo.network

import com.example.fingerprintsdkdemo.model.EnrollPersonRequest
import com.example.fingerprintsdkdemo.model.EnrollPersonResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface BioPassIDApi {
    @Headers("Content-Type: application/json", "Ocp-Apim-Subscription-Key: your-api-key")
    @POST("multibiometrics/enroll")
    fun enrollPerson(@Body enrollPersonRequest: EnrollPersonRequest) : Call<EnrollPersonResponse>
}