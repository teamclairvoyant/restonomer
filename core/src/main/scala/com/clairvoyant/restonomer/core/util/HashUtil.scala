package com.clairvoyant.restonomer.core.util

import java.math.BigInteger
import java.security.MessageDigest

object HashUtil {

  val DEFAULT_STRING_FORMAT = "%032x"
  val DEFAULT_CHARSET_NAME = "UTF-8"

  val MD5_ALGORITHM_NAME = "MD5"
  val SHA256_ALGORITHM_NAME = "SHA-256"

  def getEncryptedHash(
      text: String,
      algorithmName: String,
      format: String,
      charsetName: String
  ): String =
    String.format(
      format,
      new BigInteger(MessageDigest.getInstance(algorithmName).digest(text.getBytes(charsetName)))
    )

  def getMD5EncryptedHash(
      text: String,
      format: String = DEFAULT_STRING_FORMAT,
      charsetName: String = DEFAULT_CHARSET_NAME
  ): String = getEncryptedHash(text, MD5_ALGORITHM_NAME, format, charsetName)

  def getSHA256EncryptedHash(
      text: String,
      format: String = DEFAULT_STRING_FORMAT,
      charsetName: String = DEFAULT_CHARSET_NAME
  ): String = getEncryptedHash(text, SHA256_ALGORITHM_NAME, format, charsetName)

}
