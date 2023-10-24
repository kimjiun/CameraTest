package com.jiun.cameratest

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.jiun.cameratest.ui.theme.CameraTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    var pictureUri: Uri? = null
    val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CameraTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

    private val getTakePicture2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            Log.d("INFO", "RESULT_OK")
            val bitmap = it.data?.extras?.get("data") as Bitmap?
            bitmap?.let {
                Log.d("JIUN", "bitmap.width : ${bitmap.width}")
                Log.d("JIUN", "bitmap.height : ${bitmap.height}")
                Log.d("JIUN", "bitmap.byteCount : ${bitmap.byteCount}")
                mainViewModel.pictureArray.add(bitmap)
            }
        }
    }

    private val getTakePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if(it) {
            Log.d("JIUN", "pictureUri : ${pictureUri}")
        }
    }

    // 카메라를 실행하며 결과로 비트맵 이미지를 얻음
    private val getTakePicturePreview = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        Log.d("JIUN", "bitmap : ${bitmap?.height}")
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val screenWidth = configuration.screenWidthDp.dp

        Column {
            Button(onClick = { getTakePicture2.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE)) }) {
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            LazyColumn(){
                itemsIndexed(items = mainViewModel.pictureArray) {index, item ->
                    GlideImage(modifier = Modifier.fillMaxWidth().height(screenWidth), model = item, contentDescription = null)
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }

    }
}