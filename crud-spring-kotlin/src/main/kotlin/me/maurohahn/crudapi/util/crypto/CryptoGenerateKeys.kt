package me.maurohahn.crudapi.util.crypto

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException


class CryptoGenerateKeys {

    @Throws(NoSuchAlgorithmException::class, NoSuchProviderException::class)
    fun genKeyPair() {

        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(CryptoConstantes.GENERIC_KEY_SIZE)

        val pair: KeyPair = generator.generateKeyPair()

        writeToFile("/keys/" + CryptoConstantes.GENERIC_PRIVATE_KEY,pair.private.encoded)

        writeToFile("/keys/" + CryptoConstantes.GENERIC_PUBLIC_KEY,pair.public.encoded)

    }

    @Throws(IOException::class)
    private fun writeToFile(path: String, key: ByteArray) {
        val file = File(path)
        file.parentFile.mkdirs()

        val fos = FileOutputStream(file)
        fos.write(key)
        fos.flush()
        fos.close()
    }

}

//fun main(){
//    CryptoGenerateKeys().genKeyPair()
//}