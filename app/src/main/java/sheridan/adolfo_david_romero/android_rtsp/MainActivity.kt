package sheridan.adolfo_david_romero.android_rtsp

import android.media.browse.MediaBrowser
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import sheridan.adolfo_david_romero.android_rtsp.ui.theme.Android_RTSPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Android_RTSPTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   /*
                   * If your unplug the camera, the RTSP URL changes
                   * "rtsp://Test123:Test123@10.24.0.40/live"
                   */

                    val rtspUrls = listOf(
                        "rtsp://Becky:Becky123@10.24.0.94/live", //Cam_1
                        "rtsp://Test123:Test123@10.24.0.40/live" //Cam_2
                    )
                    MultipleRTSPStreams(rtspUrls = rtspUrls)
                    //test stream link --> "rtsp://rtspstream:ad4b60f0997496bc959695eff382b136@zephyr.rtsp.stream/movie"
                }
            }
        }
    }
}
@Composable
fun MultipleRTSPStreams(rtspUrls: List<String>) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        rtspUrls.forEach { rtspUrl ->
            RTSPVideoStream(rtspUrl = rtspUrl, modifier = Modifier.weight(1f))
        }
    }
}
@Composable
fun RTSPVideoStream(rtspUrl: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(rtspUrl)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(key1 = player) {
        onDispose {
            player.release()
        }
    }

    AndroidView(
        factory = {
            PlayerView(context).apply {
                this.player = player
                useController = true
            }
        },
        modifier = modifier.fillMaxSize()
    )
}


@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    Android_RTSPTheme {
        MainActivity()
    }
}