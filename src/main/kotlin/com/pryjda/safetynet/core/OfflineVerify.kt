package com.pryjda.safetynet.core

import javax.net.ssl.SSLException
import org.apache.http.conn.ssl.DefaultHostnameVerifier
import java.security.GeneralSecurityException
import java.io.IOException
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.json.webtoken.JsonWebSignature
import java.security.cert.X509Certificate
import java.util.*

fun main(args: Array<String>) {
//    if (args.size != 1) {
//        System.err.println("Usage: OfflineVerify <signed attestation statement>")
//        return
//    }

    OfflineVerify().process(JWT_TEST.replace(System.getProperty("line.separator"), ""))
}

class OfflineVerify {
    private val HOSTNAME_VERIFIER = DefaultHostnameVerifier()

    private fun parseAndVerify(signedAttestationStatement: String): AttestationStatement? {
        // Parse JSON Web Signature format.
        val jws: JsonWebSignature
        try {
            jws = JsonWebSignature.parser(JacksonFactory.getDefaultInstance())
                    .setPayloadClass(AttestationStatement::class.java).parse(signedAttestationStatement)
        } catch (e: IOException) {
            System.err.println("Failure: " + signedAttestationStatement + " is not valid JWS " +
                    "format.")
            return null
        }

        // Verify the signature of the JWS and retrieve the signature certificate.
        val cert: X509Certificate?
        try {
            cert = jws.verifySignature()
            if (cert == null) {
                System.err.println("Failure: Signature verification failed.")
                return null
            }
        } catch (e: GeneralSecurityException) {
            System.err.println(
                    "Failure: Error during cryptographic verification of the JWS signature.")
            return null
        }

        // Verify the hostname of the certificate.
        if (!verifyHostname("attest.android.com", cert)) {
            System.err.println("Failure: Certificate isn't issued for the hostname attest.android" + ".com.")
            return null
        }

        // Extract and use the payload data.
        return jws.payload as AttestationStatement
    }

    /**
     * Verifies that the certificate matches the specified hostname.
     * Uses the [DefaultHostnameVerifier] from the Apache HttpClient library
     * to confirm that the hostname matches the certificate.
     *
     * @param hostname
     * @param leafCert
     * @return
     */
    private fun verifyHostname(hostname: String, leafCert: X509Certificate): Boolean {
        try {
            // Check that the hostname matches the certificate. This method throws an exception if
            // the cert could not be verified.
            HOSTNAME_VERIFIER.verify(hostname, leafCert)
            return true
        } catch (e: SSLException) {
            e.printStackTrace()
        }

        return false
    }


     fun process(signedAttestationStatement: String) {
        val stmt = parseAndVerify(signedAttestationStatement)
        if (stmt == null) {
            System.err.println("Failure: Failed to parse and verify the attestation statement.")
            return
        }

        println("Successfully verified the attestation statement. The content is:")

        System.out.println("Nonce: " + Arrays.toString(stmt!!.getNonce()))
        System.out.println("Timestamp: " + stmt!!.getTimestampMs() + " ms")
        System.out.println("APK package name: " + stmt!!.getApkPackageName())
        System.out.println("APK digest SHA256: " + Arrays.toString(stmt!!.getApkDigestSha256()))
        System.out.println("APK certificate digest SHA256: " + Arrays.deepToString(stmt!!.getApkCertificateDigestSha256()))
        System.out.println("CTS profile match: " + stmt!!.isCtsProfileMatch())
        System.out.println("Has basic integrity: " + stmt!!.hasBasicIntegrity())

        println("\n** This sample only shows how to verify the authenticity of an "
                + "attestation response. Next, you must check that the server response matches the "
                + "request by comparing the nonce, package name, timestamp and digest.")
    }

//    @JvmStatic
//    fun main(args: Array<String>) {
//        if (args.size != 1) {
//            System.err.println("Usage: OfflineVerify <signed attestation statement>")
//            return
//        }
//        process(args[0])
//    }
}