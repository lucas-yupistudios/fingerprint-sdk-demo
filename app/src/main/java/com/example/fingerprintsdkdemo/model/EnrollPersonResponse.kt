package com.example.fingerprintsdkdemo.model

import com.google.gson.annotations.SerializedName

data class EnrollPersonResponse(
    @SerializedName("Person") val person: PersonResponse?,
    @SerializedName("Message") val message: String?
)

data class PersonResponse(
    @SerializedName("ClientID") val clientID: String?,
    @SerializedName("CustomID") val customID: String?,
    @SerializedName("BioPassID") val bioPassID: String?
)