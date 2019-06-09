package com.pryjda.safetynet.core

import com.google.api.client.json.webtoken.JsonWebToken
import com.google.api.client.util.Base64
import com.google.api.client.util.Key

class AttestationStatementV2X : JsonWebToken.Payload() {

    /**
     * Embedded nonce sent as part of the request.
     */
    @Key
    private val nonce: String? = null

    /**
     * Timestamp of the request.
     */
    @Key
    private val timestampMs: Long = 0

    /**
     * Package name of the APK that submitted this request.
     */
    @Key
    private val apkPackageName: String? = null

    /**
     * Digest of certificate of the APK that submitted this request.
     */
    @Key
    private val apkCertificateDigestSha256: Array<String>? = null

    /**
     * Digest of the APK that submitted this request.
     */
    @Key
    private val apkDigestSha256: String? = null

    /**
     * The device passed CTS and matches a known profile.
     */
    @Key
    private val ctsProfileMatch: Boolean = false


    /**
     * The device has passed a basic integrity test, but the CTS profile could not be verified.
     */
    @Key
    private val basicIntegrity: Boolean = false

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
}