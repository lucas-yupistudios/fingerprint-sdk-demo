package com.example.fingerprintsdkdemo.model

import com.google.gson.annotations.SerializedName

data class EnrollPersonRequest(
    @SerializedName("Person") val person: PersonRequest
)

data class PersonRequest(
    @SerializedName("CustomID") val customID: String,
    @SerializedName("Fingers") val fingers: List<FingerPersonRequest>?
)

data class FingerPersonRequest(
    @SerializedName("Finger-1") val finger1: String,
    @SerializedName("Finger-2") val finger2: String,
    @SerializedName("Finger-3") val finger3: String,
    @SerializedName("Finger-4") val finger4: String,
)