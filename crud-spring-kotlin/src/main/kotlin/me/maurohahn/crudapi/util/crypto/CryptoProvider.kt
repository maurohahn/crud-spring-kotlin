package me.maurohahn.crudapi.util.crypto

import java.io.File
import java.io.UnsupportedEncodingException
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random


object CryptoProvider {

    private var privateKeys = HashMap<String, PrivateKey>()

    private var publicKeys = HashMap<String, PublicKey>()

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
    private fun encryptTextRSA(msg: String, pk: PrivateKey): String {
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
    private fun decryptTextRSA(encryptedMsg: String, pk: PublicKey): String {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, pk)

        val decodedMsgBytes = Base64.getDecoder().decode(encryptedMsg)
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

    fun encryptText(msg: String): String {
        val pk = getPrivateKey(CryptoConstantes.KEY_PATH + CryptoConstantes.GENERIC_PRIVATE_KEY)
        val encryptedMsg = encryptTextRSA(msg, pk)

        return encodePath(encryptedMsg)

    }

    fun decryptText(encryptedMsg: String): String {
        return try {
            val pk = getPublicKey(CryptoConstantes.KEY_PATH + CryptoConstantes.GENERIC_PUBLIC_KEY)
            val decodedMsg = decodePath(encryptedMsg)
            decryptTextRSA(decodedMsg, pk)
        } catch (e: Exception) {
            e.printStackTrace()
            encryptedMsg
        }
    }

    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class
    )
    private fun encryptTextAES(msg: String, secretKey: String): String {

        val randomBytes = Random.nextBytes(16)
        val initVector = IvParameterSpec(randomBytes)

        val keySpec = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "AES")

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, initVector)

        val cipherText = cipher.doFinal(msg.toByteArray())

        return Base64.getEncoder()
            .encodeToString(cipherText)

    }

    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class
    )
    private fun decryptTextAES(msg: String, secretKey: String): String {

        val randomBytes = Random.nextBytes(16)
        val initVector = IvParameterSpec(randomBytes)

        val keySpec = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "AES")

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, keySpec, initVector)

        val plainText = cipher.doFinal(
            Base64.getDecoder()
                .decode(msg)
        )
        return String(plainText)
    }

    fun encryptLargeText(msg: String): String {
        return encryptTextAES(msg, CryptoConstantes.AES_KEY)
    }

    fun decryptLargeText(encryptedMsg: String): String {
        return decryptTextAES(encryptedMsg, CryptoConstantes.AES_KEY)
    }

}

//fun main (){
//
//    val txt = "ola meu nome é Mauro"
//
//    val etxt = CryptoProvider.encryptLargeText(txt)
//
//    val dtxt = CryptoProvider.decryptLargeText(etxt)
//
//    val z = true
//
//}