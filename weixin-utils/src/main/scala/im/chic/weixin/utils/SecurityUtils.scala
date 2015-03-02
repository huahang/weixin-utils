package im.chic.weixin.utils

import com.google.common.base.Charsets
import im.chic.utils.crypto.DigestUtils

import scala.collection.immutable.TreeMap

/**
 * @author huahang
 */
object SecurityUtils {
  def generateNonce(): String = {
    DigestUtils.sha1Hex(
      ("" + System.currentTimeMillis() + "|" + Math.random()).getBytes(Charsets.UTF_8)
    )
  }

  def calculateSignature(url: String, jsTicket: String, timestamp: String, nonce: String): String = {
    val combinedString = TreeMap[String, String] (
      "url" -> url,
      "jsapi_ticket" -> jsTicket,
      "noncestr" -> nonce,
      "timestamp" -> timestamp
    ).toList.map(s => s._1 + "=" + s._2).reduceLeft((s1, s2) => s1 + "&" + s2)
    DigestUtils.sha1Hex(combinedString.getBytes(Charsets.UTF_8)).toLowerCase()
  }
}
