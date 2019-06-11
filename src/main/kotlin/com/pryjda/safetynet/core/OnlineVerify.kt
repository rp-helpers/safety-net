package com.pryjda.safetynet.core

import java.io.IOException
import java.io.ByteArrayInputStream
import com.google.api.client.http.json.JsonHttpContent
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpRequest
import com.google.api.client.json.JsonObjectParser
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.util.Key
import java.util.*
import com.google.api.client.util.Base64

fun main(args: Array<String>) {
//    if (args.size != 1) {
//        System.err.println("Usage: OfflineVerify <signed attestation statement>")
//        return
//    }
    OnlineVerify().process(JWT_TEST.replace(System.getProperty("line.separator"), ""))
}

class OnlineVerify {
    // Please use the Google Developers Console (https://console.developers.google.com/)
    // to create a project, enable the Android Device Verification API, generate an API key
    // and add it here.
    private val API_KEY = "AIqrqtCqc1_-btSSt0vHQXV141255ERrWJY4"

    private val HTTP_TRANSPORT = NetHttpTransport()

    private val JSON_FACTORY = JacksonFactory()
    private val URL = "https://www.googleapis.com/androidcheck/v1/attestations/verify?key=$API_KEY"

    /**
     * Class for parsing JSON data.
     */
    class VerificationRequest(@field:Key var signedAttestation: String)

    /**
     * Class for parsing JSON data.
     */
    class VerificationResponse {
        @Key
        var isValidSignature: Boolean = false

        /**
         * Optional field that is only set when the server encountered an error processing the
         * request.
         */
        @Key
        var error: String? = null
    }

    private fun onlineVerify(request: VerificationRequest): VerificationResponse? {
        // Prepare a request to the Device Verification API and set a parser for JSON data.
        val requestFactory = HTTP_TRANSPORT.createRequestFactory(object : HttpRequestInitializer {
            override fun initialize(request: HttpRequest) {
                request.setParser(JsonObjectParser(JSON_FACTORY))
            }
        })
        val url = GenericUrl(URL)
        val httpRequest: HttpRequest
        try {
            // Post the request with the verification statement to the API.
            httpRequest = requestFactory.buildPostRequest(url, JsonHttpContent(JSON_FACTORY,
                    request))
            // Parse the returned data as a verification response.
            return httpRequest.execute().parseAs(VerificationResponse::class.java)
        } catch (e: IOException) {
            System.err.println(
                    "Failure: Network error while connecting to the Google Service $URL.")
            System.err.println("Ensure that you added your API key and enabled the Android device " + "verification API.")
            return null
        }

    }

    /**
     * Extracts the data part from a JWS signature.
     */
    private fun extractJwsData(jws: String): ByteArray? {
        // The format of a JWS is:
        // <Base64url encoded header>.<Base64url encoded JSON data>.<Base64url encoded signature>
        // Split the JWS into the 3 parts and return the JSON data part.
        val parts = jws.split("[.]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (parts.size != 3) {
            System.err.println("Failure: Illegal JWS signature format. The JWS consists of "
                    + parts.size + " parts instead of 3.")
            return null
        }
        return Base64.decodeBase64(parts[1])
    }

    private fun parseAndVerify(signedAttestationStatment: String): AttestationStatement? {
        // Send the signed attestation statement to the API for verification.
        val request = VerificationRequest(signedAttestationStatment)
        val response = onlineVerify(request) ?: return null

        if (response.error != null) {
            System.err.println(
                    "Failure: The API encountered an error processing this request: " + response.error!!)
            return null
        }

        if (!response.isValidSignature) {
            System.err.println(
                    "Failure: The cryptographic signature of the attestation statement couldn't be " + "verified.")
            return null
        }

        println("Sucessfully verified the signature of the attestation statement.")

        // The signature is valid, extract the data JSON from the JWS signature.
        val data = extractJwsData(signedAttestationStatment)

        // Parse and use the data JSON.
        try {
            return JSON_FACTORY.fromInputStream(ByteArrayInputStream(data!!),
                    AttestationStatement::class.java)
        } catch (e: IOException) {
            System.err.println("Failure: Failed to parse the data portion of the JWS as valid " + "JSON.")
            return null
        }

    }

    fun process(signedAttestationStatement: String) {
        val stmt = parseAndVerify(signedAttestationStatement)
        if (stmt == null) {
            System.err.println("Failure: Failed to parse and verify the attestation statement.")
            return
        }

        println("The content of the attestation statement is:")

        // Nonce that was submitted as part of this request.
        System.out.println("Nonce: " + Arrays.toString(stmt.getNonce()))
        // Timestamp of the request.
        println("Timestamp: " + stmt.getTimestampMs() + " ms")

        if (stmt.getApkPackageName() != null && stmt.getApkDigestSha256() != null) {
            // Package name and digest of APK that submitted this request. Note that these details
            // may be omitted if the API cannot reliably determine the package information.
            println("APK package name: " + stmt.getApkPackageName()!!)
            System.out.println("APK digest SHA256: " + Arrays.toString(stmt.getApkDigestSha256()))
        }
        // Has the device a matching CTS profile?
        println("CTS profile match: " + stmt.isCtsProfileMatch())
        // Has the device passed CTS (but the profile could not be verified on the server)?
        println("Basic integrity match: " + stmt.hasBasicIntegrity())

        println("\n** This sample only shows how to verify the authenticity of an "
                + "attestation response. Next, you must check that the server response matches the "
                + "request by comparing the nonce, package name, timestamp and digest.")
    }

    /* @JvmStatic
     fun main(args: Array<String>) {
         if (args.size != 1) {
             System.err.println("Usage: OnlineVerify <signed attestation statement>")
             return
         }
         process(args[0])
     }*/
}