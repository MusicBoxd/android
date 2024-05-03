package musicboxd.android.ui.details.canvas

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class VideoCanvasVM : ViewModel() {
    val canvasUrl = mutableStateOf(emptyList<String>())
    fun loadCanvasUrls(trackUrls: List<String>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                trackUrls.forEach {
                    val rawHtml = Jsoup.connect(it)
                        .get().toString()
                    if (rawHtml.contains("<source src=\"https://")) {
                        rawHtml.substringAfter("<source src=\"https://")
                            .substringBefore(".mp4\" type=\"video/mp4\">").let { url ->
                                canvasUrl.value += ("https://$url.mp4")
                            }
                    } else {
                        canvasUrl.value += ""
                    }
                }
            }
        }
    }
}