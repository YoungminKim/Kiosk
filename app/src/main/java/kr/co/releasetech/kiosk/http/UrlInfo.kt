package kr.co.releasetech.kiosk.http

object UrlInfo {

    private const val SSL: Boolean = true

    private const val IS_TEST = true

    private fun getHttp(): String {
        var result: String = if (SSL) "https://"
        else "http://"

        result += if (IS_TEST) TEST_SERVER
        else MAIN_SEVER

        return result
    }

    val URL_HEADER: String = getHttp()


    private const val MAIN_SEVER = "youngmin178.cafe24.com"
    private const val TEST_SERVER = "youngmin178.cafe24.com"

    const val GET_QR_DATA = "kiosk.php"
}