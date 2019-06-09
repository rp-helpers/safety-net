/*
package com.pryjda.safetynet.core

import sun.jvm.hotspot.HelloWorld.e
import java.io.BufferedReader
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.KeyStoreException
import org.springframework.lang.NonNull


class AndroidDeviceVerifier {

    private val TAG = AndroidDeviceVerifier::class.java.simpleName

    //used to verifiy the safety net response - 10,000 requests/day free
    private val GOOGLE_VERIFICATION_URL = "https://www.googleapis.com/androidcheck/v1/attestations/verify?key="

    private var apiKey: String? = null
    private var signatureToVerify: String? = null
    private var callback: AndroidDeviceVerifierCallback? = null

    interface AndroidDeviceVerifierCallback {
        fun error(s: String)
        fun success(isValidSignature: Boolean)
    }

    fun AndroidDeviceVerifier(@NonNull apiKey: String, @NonNull signatureToVerify: String){
        this.apiKey = apiKey
        this.signatureToVerify = signatureToVerify
    }

    fun verify(androidDeviceVerifierCallback: AndroidDeviceVerifierCallback) {
        callback = androidDeviceVerifierCallback
        val task = AndroidDeviceVerifierTask()
        task.execute()
    }

    */
/**
     * Provide the trust managers for the URL connection. By Default this uses the system defaults plus the GoogleApisTrustManager (SSL pinning)
     * @return array of TrustManager including system defaults plus the GoogleApisTrustManager (SSL pinning)
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     *//*

    @Throws(KeyStoreException::class, NoSuchAlgorithmException::class)
    protected fun getTrustManagers(): Array<TrustManager> {
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        //init with the default system trustmanagers
        trustManagerFactory.init(null as KeyStore?)
        val defaultTrustManagers = trustManagerFactory.getTrustManagers()
        val trustManagers = Arrays.copyOf(defaultTrustManagers, defaultTrustManagers.size + 1)
        //add our Google APIs pinning TrustManager for extra security
        trustManagers[defaultTrustManagers.size] = GoogleApisTrustManager()
        return trustManagers
    }


    private inner class AndroidDeviceVerifierTask : AsyncTask<Void, Void, Boolean>() {

        private var error: Exception? = null

        protected fun doInBackground(vararg params: Void): Boolean? {

            //Log.d(TAG, "signatureToVerify:" + signatureToVerify);

            try {
                val verifyApiUrl = URL(GOOGLE_VERIFICATION_URL + apiKey)

                val sslContext = SSLContext.getInstance("TLS")
                sslContext.init(null, getTrustManagers(), null)

                val urlConnection = verifyApiUrl.openConnection() as HttpsURLConnection
                urlConnection.setSSLSocketFactory(sslContext.getSocketFactory())

                urlConnection.setRequestMethod("POST")
                urlConnection.setRequestProperty("Content-Type", "application/json")

                //build post body { "signedAttestation": "<output of getJwsResult()>" }
                val requestJsonBody = "{ \"signedAttestation\": \"$signatureToVerify\"}"
                val outputInBytes = requestJsonBody.toByteArray(charset("UTF-8"))
                val os = urlConnection.getOutputStream()
                os.write(outputInBytes)
                os.close()

                urlConnection.connect()

                //resp ={ “isValidSignature”: true }
                val `is` = urlConnection.getInputStream()
                val sb = StringBuilder()
                val rd = BufferedReader(InputStreamReader(`is`))
                var line: String
                while ((line = rd.readLine()) != null) {
                    sb.append(line)
                }
                val response = sb.toString()
                val responseRoot = JSONObject(response)
                if (responseRoot.has("isValidSignature")) {
                    return responseRoot.getBoolean("isValidSignature")
                }
            } catch (e: Exception) {
                //something went wrong requesting validation of the JWS Message
                error = e
                Log.e(TAG, "problem validating JWS Message :" + e.message, e)
                return false
            }

            return false
        }

        protected fun onPostExecute(aBoolean: Boolean?) {
            if (error != null) {
                callback!!.error(error!!.message)
            } else {
                callback!!.success(aBoolean!!)
            }
        }
    }
}*/
