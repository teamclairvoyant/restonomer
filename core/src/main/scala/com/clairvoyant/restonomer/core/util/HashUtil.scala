package com.clairvoyant.restonomer.core.util

import java.math.BigInteger
import java.security.MessageDigest

object HashUtil {

  val DEFAULT_STRING_FORMAT = "%032x"
  val DEFAULT_CHARSET_NAME = "UTF-8"

  val MD5_ALGORITHM_NAME = "MD5"

  def getMD5EncryptedHash(
      text: String,
      format: String = DEFAULT_STRING_FORMAT,
      charsetName: String = DEFAULT_CHARSET_NAME
  ): String = {
    String.format(
      format,
      new BigInteger(MessageDigest.getInstance(MD5_ALGORITHM_NAME).digest(text.getBytes(charsetName)))
    )
  }

}
