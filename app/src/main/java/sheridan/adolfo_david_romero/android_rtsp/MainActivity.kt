package sheridan.adolfo_david_romero.android_rtsp

import android.media.browse.MediaBrowser
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
                    // Start streaming video using Compose
                    RTSPVideoStream(rtspUrl = "rtsp://Becky:Becky123@10.24.0.102/live")
                    //RTSPVideoStream(rtspUrl = "rtsp://username:password@camera-ip/live") //TODO: FLASH RTSP ON CAMERA
                    //test stream link --> "rtsp://rtspstream:ad4b60f0997496bc959695eff382b136@zephyr.rtsp.stream/movie"
                }
            }
        }
    }
}

@Composable
fun RTSPVideoStream(rtspUrl: String) {
    // Create an ExoPlayer instance and release it when done
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            // Set up the media item using the RTSP URL
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

    // Display the PlayerView using AndroidView inside Compose
    AndroidView(
        factory = {
            PlayerView(context).apply {
                this.player = player
                // Set player controls visibility (optional)
                useController = true
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    Android_RTSPTheme {
        MainActivity()
    }
}