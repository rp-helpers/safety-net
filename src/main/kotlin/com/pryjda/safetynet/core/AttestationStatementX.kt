/*
package com.pryjda.safetynet.core

import com.google.api.client.json.webtoken.JsonWebToken
import com.google.api.client.util.Base64
import com.google.api.client.util.Key

class AttestationStatementX : JsonWebToken.Payload() {

    */
/**
     * Embedded nonce sent as part of the request.
     *//*

    @Key
    private val nonce: String? = "XPB7VTdRXbD3Mf0CoLQyeRPrP9f23aofTpmUgzriko0="

    */
/**
     * Timestamp of the request.
     *//*

    @Key
    private val timestampMs: Long = 1540651684093

    */
/**
     * Package name of the APK that submitted this request.
     *//*

    @Key
    private val apkPackageName: String? = "com.google.android.gms"

    */
/**
     * Digest of certificate of the APK that submitted this request.
     *//*

    @Key
    private var apkCertificateDigestSha256: Array<String>? = arrayOf("8P1sW0EPJcslw7UzRsiXL64w+O50Ed+RBICtay1g24M=")
//    private val apkCertificateDigestSha256: Array<ByteArray?> = resolve()

    */
/**
     * Digest of the APK that submitted this request.
     *//*

    @Key
    private val apkDigestSha256: String? = "eQc+vzUcdx0FVNLvXHuGpD0+R807sUEvp+JeleYZsiA="

    */
/**
     * The device passed CTS and matches a known profile.
     *//*

    @Key
    private val ctsProfileMatch: Boolean = true


    */
/**
     * The device has passed a basic integrity test, but the CTS profile could not be verified.
     *//*

    @Key
    private val basicIntegrity: Boolean = true

    fun getNonce(): ByteArray {
        return Base64.decodeBase64(nonce)
    }

    fun getTimestampMs(): Long {
        return timestampMs
    }

    fun getApkPackageName(): String? {
        return apkPackageName
    }

    fun getApkDigestSha256(): ByteArray {
        return Base64.decodeBase64(apkDigestSha256)
    }

    fun getApkCertificateDigestSha256(): Array<ByteArray?> {
        val certs = arrayOfNulls<ByteArray>(apkCertificateDigestSha256!!.size)
        for (i in apkCertificateDigestSha256.indices) {
            certs[i] = Base64.decodeBase64(apkCertificateDigestSha256[i])
        }
        return certs
    }

    fun isCtsProfileMatch(): Boolean {
        return ctsProfileMatch
    }

    fun hasBasicIntegrity(): Boolean {
        return basicIntegrity
    }

  */
/*  fun resolve(): Array<ByteArray?> {
        val apkCertificateDigestSha256 = arrayOf("8P1sW0EPJcslw7UzRsiXL64w+O50Ed+RBICtay1g24M=")
        val certs = arrayOfNulls<ByteArray>(apkCertificateDigestSha256!!.size)
        for (i in apkCertificateDigestSha256.indices) {
            certs[i] = Base64.decodeBase64(apkCertificateDigestSha256[i])
        }
        return certs
    }*//*

}*/
