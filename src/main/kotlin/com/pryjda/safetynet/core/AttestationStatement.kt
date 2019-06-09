package com.pryjda.safetynet.core

import com.google.api.client.json.webtoken.JsonWebToken
import com.google.api.client.util.Base64
import com.google.api.client.util.Key

class AttestationStatement/*(
        @Key private var nonce: String?,
        @Key private var timestampMs: Long?,
        @Key private var apkPackageName: String?,
        @Key private var apkCertificateDigestSha256: Array<String>?,
        @Key private var apkDigestSha256: String?,
        @Key private var ctsProfileMatch: Boolean?,
        @Key private var basicIntegrity: Boolean?)*/ : JsonWebToken.Payload() {

    /*constructor() : this(null,
            null,
            null,
            null,
            null,
            null,
            null)*/

    @Key private var nonce: String? = null
    @Key private var timestampMs: Long? = null
    @Key private var apkPackageName: String? = null
    @Key private var apkCertificateDigestSha256: Array<String>? = null
    @Key private var apkDigestSha256: String? = null
    @Key private var ctsProfileMatch: Boolean? = null
    @Key private var basicIntegrity: Boolean? = null

    /**
     * Embedded nonce sent as part of the request.
     */


    /**
     * Timestamp of the request.
     */


    /**
     * Package name of the APK that submitted this request.
     */


    /**
     * Digest of certificate of the APK that submitted this request.
     */


    /**
     * Digest of the APK that submitted this request.
     */


    /**
     * The device passed CTS and matches a known profile.
     */


    /**
     * The device has passed a basic integrity test, but the CTS profile could not be verified.
     */

    fun getNonce(): ByteArray {
        return Base64.decodeBase64(nonce)
    }

    fun getTimestampMs(): Long? {
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
        for (i in apkCertificateDigestSha256!!.indices) {
            certs[i] = Base64.decodeBase64(apkCertificateDigestSha256!![i])
        }
        return certs
    }

    fun isCtsProfileMatch(): Boolean? {
        return ctsProfileMatch
    }

    fun hasBasicIntegrity(): Boolean? {
        return basicIntegrity
    }
}