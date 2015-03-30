package com.chiwanpark.push.util

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

import org.apache.commons.codec.binary.Base64

object Crypto {
  val ALGORITHM = "AES"

  private def getCipher(mode: Int, secret: String): Cipher = {
    val key = new SecretKeySpec(secret.getBytes("UTF-8"), ALGORITHM)
    val cipher = Cipher.getInstance(ALGORITHM + "/ECB/PKCS5Padding")
    cipher.init(mode, key)
    cipher
  }

  def encrypt(data: String, secret: String): Array[Byte] = encrypt(data.getBytes("UTF-8"), secret)

  def encrypt(data: Array[Byte], secret: String): Array[Byte] = getCipher(Cipher.ENCRYPT_MODE, secret).doFinal(data)

  def decrypt(data: Array[Byte], secret: String): Array[Byte] = getCipher(Cipher.DECRYPT_MODE, secret).doFinal(data)

  def encodeBase64(data: Array[Byte]): String = Base64.encodeBase64String(data)

  def decodeBase64(data: String): Array[Byte] = Base64.decodeBase64(data)

  def hash(data: String, secret: String): String = {
    import com.roundeights.hasher.Implicits._
    data.salt(secret).sha384.hash.hex
  }
}
