package me.maurohahn.crudapi.util.crypto

import java.io.File
import java.io.UnsupportedEncodingException
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import kotlin.collections.HashMap

object CryptoProvider {

    private var privateKeys = HashMap<String, PrivateKey>()

    private var publicKeys = HashMap<String, PublicKey>()

    private var keyPath: String = "/keys/"

    @Throws(Exception::class)
    private fun getPrivateKey(filename: String): PrivateKey {

        return when {
            privateKeys.containsKey(filename) -> {
                privateKeys.getValue(filename)
            }

            else -> {
                val keyBytes = File(filename).readBytes()
                val spec = PKCS8EncodedKeySpec(keyBytes)
                val kf = KeyFactory.getInstance("RSA")
                val privateKey = kf.generatePrivate(spec)
                privateKeys[filename] = privateKey
                privateKey
            }
        }
    }

    @Throws(Exception::class)
    private fun getPublicKey(filename: String): PublicKey {

        return when {
            publicKeys.containsKey(filename) -> {
                publicKeys.getValue(filename)
            }

            else -> {
                val keyBytes = File(filename).readBytes()
                val spec = X509EncodedKeySpec(keyBytes)
                val kf = KeyFactory.getInstance("RSA")
                val publicKey = kf.generatePublic(spec)
                publicKeys[filename] = publicKey
                publicKey
            }
        }
    }

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        UnsupportedEncodingException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class,
        InvalidKeyException::class
    )
    private fun encryptText(msg: String, pk: PrivateKey): String {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, pk)

        val msgBytes = msg.toByteArray(Charsets.UTF_8)
        val encryptedMsgBytes = cipher.doFinal(msgBytes)

        return Base64.getEncoder().encodeToString(encryptedMsgBytes)
    }

    @Throws(
        InvalidKeyException::class,
        UnsupportedEncodingException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    private fun decryptText(msg: String, pk: PublicKey): String {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, pk)

        val decodedMsgBytes = Base64.getDecoder().decode(msg)
        val decryptedMsgBytes = cipher.doFinal(decodedMsgBytes)

        return String(decryptedMsgBytes, Charsets.UTF_8)
    }

    private fun encodePath(msg: String): String {
        return msg.replace("=", CryptoConstantes.HTTP_CHAR_EQUAL)
            .replace("/", CryptoConstantes.HTTP_CHAR_SLASH)
            .replace("+", CryptoConstantes.HTTP_CHAR_PLUS)
    }

    private fun decodePath(msg: String): String {
        return msg.replace(CryptoConstantes.HTTP_CHAR_EQUAL, "=")
            .replace(CryptoConstantes.HTTP_CHAR_SLASH, "/")
            .replace(CryptoConstantes.HTTP_CHAR_PLUS, "+")
    }

    fun encryptGen(msg: String): String {
        val pk = getPrivateKey(keyPath + CryptoConstantes.GENERIC_PRIVATE_KEY)
        val encryptedMsg = encryptText(msg, pk)

        return encodePath(encryptedMsg)

    }

    fun decryptGen(encryptedMsg: String): String {
        return try {
            val pk = getPublicKey(keyPath + CryptoConstantes.GENERIC_PUBLIC_KEY)
            val decodedMsg = decodePath(encryptedMsg)
            decryptText(decodedMsg, pk)
        } catch (e: Exception) {
            e.printStackTrace()
            encryptedMsg
        }
    }

}