package com.clairvoyant.restonomer.core.util

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.core.util.HashUtil._

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

  "getSHA256EncryptedHash" should "return expected sha256 hash value" in {
    getSHA256EncryptedHash(text) shouldBe "46d4590026dc4147fd6ba109b4ef607e7da72b12aa715e92657a711d149f3dc"
  }

  "getSHA256EncryptedHash - with format" should "return expected sha256 hash value" in {
    getSHA256EncryptedHash(
      text = text,
      format = "%064x"
    ) shouldBe "046d4590026dc4147fd6ba109b4ef607e7da72b12aa715e92657a711d149f3dc"
  }

  "getSHA256EncryptedHash - with charset name" should "return expected sha256 hash value" in {
    getSHA256EncryptedHash(
      text = text,
      charsetName = "US-ASCII"
    ) shouldBe "46d4590026dc4147fd6ba109b4ef607e7da72b12aa715e92657a711d149f3dc"
  }

  "getSHA256EncryptedHash - with format and charset name" should "return expected sha256 hash value" in {
    getSHA256EncryptedHash(
      text = text,
      format = "%064x",
      charsetName = "ISO-8859-1"
    ) shouldBe "046d4590026dc4147fd6ba109b4ef607e7da72b12aa715e92657a711d149f3dc"
  }

}
