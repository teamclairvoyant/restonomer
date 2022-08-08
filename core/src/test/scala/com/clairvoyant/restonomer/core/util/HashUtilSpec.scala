package com.clairvoyant.restonomer.core.util

import com.clairvoyant.restonomer.core.CoreSpec
import com.clairvoyant.restonomer.core.util.HashUtil.getMD5EncryptedHash
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class HashUtilSpec extends CoreSpec {

  val text = "sample_plain_text"

  "getMD5EncryptedHash" should "return expected md5 hash value" in {
    getMD5EncryptedHash(text) shouldBe "2410846faa22f2f5b8450c02f69a624c"
  }

  "getMD5EncryptedHash - with format" should "return expected md5 hash value" in {
    getMD5EncryptedHash(
      text = text,
      format = "%064x"
    ) shouldBe "000000000000000000000000000000002410846faa22f2f5b8450c02f69a624c"
  }

  "getMD5EncryptedHash - with charset name" should "return expected md5 hash value" in {
    getMD5EncryptedHash(
      text = text,
      charsetName = "US-ASCII"
    ) shouldBe "2410846faa22f2f5b8450c02f69a624c"
  }

  "getMD5EncryptedHash - with format and charset name" should "return expected md5 hash value" in {
    getMD5EncryptedHash(
      text = text,
      format = "%064x",
      charsetName = "ISO-8859-1"
    ) shouldBe "000000000000000000000000000000002410846faa22f2f5b8450c02f69a624c"
  }

}
