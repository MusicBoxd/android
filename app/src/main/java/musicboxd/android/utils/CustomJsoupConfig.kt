package musicboxd.android.utils

import org.jsoup.Connection

fun Connection.customConfig(): Connection {
    return this.userAgent("Mozilla")
        .header("Accept", "text/html")
        .header("Accept-Encoding", "gzip,deflate")
        .header(
            "Accept-Language",
            "it-IT,en;q=0.8,en-US;q=0.6,de;q=0.4,it;q=0.2,es;q=0.2"
        )
        .header("Connection", "keep-alive")
        .ignoreContentType(true).maxBodySize(0)
}