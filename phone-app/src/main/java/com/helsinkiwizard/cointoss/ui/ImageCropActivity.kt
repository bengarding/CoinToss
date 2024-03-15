package com.helsinkiwizard.cointoss.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.core.content.FileProvider
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.cointoss.Repository
import com.helsinkiwizard.cointoss.data.ThemeMode
import com.helsinkiwizard.cointoss.theme.CoinTossTheme
import com.helsinkiwizard.cointoss.theme.backgroundDark
import com.helsinkiwizard.cointoss.utils.parcelable
import com.helsinkiwizard.cointoss.utils.toImageBitmap
import com.helsinkiwizard.core.theme.Twenty
import com.smarttoolfactory.cropper.ImageCropper
import com.smarttoolfactory.cropper.model.AspectRatio
import com.smarttoolfactory.cropper.model.OutlineType
import com.smarttoolfactory.cropper.model.OvalCropShape
import com.smarttoolfactory.cropper.settings.CropDefaults
import com.smarttoolfactory.cropper.settings.CropOutlineProperty
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class ImageCropActivity : ComponentActivity() {

    companion object {
        const val EXTRA_URI = "extraUri"
        const val TAG = "ImageCropActivity"

        fun createIntent(context: Context, uri: Uri): Intent {
            return Intent(context, ImageCropActivity::class.java).apply {
                putExtra(EXTRA_URI, uri)
            }
        }
    }

    @Inject
    lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val initialThemeMode: ThemeMode
            val initialMaterialYou: Boolean
            runBlocking {
                initialThemeMode = repository.getThemeMode.filterNotNull().first()
                initialMaterialYou = repository.getMaterialYou.filterNotNull().first()
            }
            CoinTossTheme(repository, initialThemeMode, initialMaterialYou) {
                val uri = intent.extras?.parcelable<Uri>(EXTRA_URI)
                if (uri != null) {
                    ImageCrop(uri.toImageBitmap(this))
                } else {
                    finish()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class) // CenterAlignedTopBar
    @Composable
    private fun ImageCrop(imageBitmap: ImageBitmap?) {
        var crop by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.crop_image),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { finish() }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = stringResource(id = R.string.back)
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { crop = true }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = stringResource(id = R.string.confirm)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        scrolledContainerColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                var croppedImage by remember { mutableStateOf<ImageBitmap?>(null) }
                val handleSize: Float = LocalDensity.current.run { Twenty.toPx() }

                val cropProperties by remember {
                    mutableStateOf(
                        CropDefaults.properties(
                            cropOutlineProperty = CropOutlineProperty(
                                OutlineType.Oval,
                                OvalCropShape(0, "Circle")
                            ),
                            handleSize = handleSize,
                            aspectRatio = AspectRatio(1f),
                            fixedAspectRatio = true
                        )
                    )
                }
                var isCropping by remember { mutableStateOf(false) }

                if (imageBitmap != null) {
                    val contentDesc = stringResource(id = R.string.image_cropper)
                    ImageCropper(
                        modifier = Modifier.fillMaxSize(),
                        backgroundColor = backgroundDark,
                        imageBitmap = imageBitmap,
                        contentDescription = contentDesc,
                        cropStyle = CropDefaults.style(),
                        cropProperties = cropProperties,
                        crop = crop,
                        onCropStart = {
                            isCropping = true
                        },
                        onCropSuccess = {
                            croppedImage = it
                            isCropping = false

                            val newUri = storeBitmap(it)
                            val args = Intent().putExtra(EXTRA_URI, newUri)
                            setResult(Activity.RESULT_OK, args)
                            finish()
                        }
                    )
                } else {
                    finish()
                }
            }
        }
    }

    private fun storeBitmap(imageBitmap: ImageBitmap): Uri? {
        val bitmap = imageBitmap.asAndroidBitmap()
        val compressFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Bitmap.CompressFormat.WEBP_LOSSY
        } else {
            Bitmap.CompressFormat.JPEG
        }
        val fileType = if (compressFormat == Bitmap.CompressFormat.JPEG) "jpg" else "webp"
        val imageName = Random.nextInt()
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$imageName.$fileType")

        return try {
            FileOutputStream(file).use { out ->
                bitmap.compress(compressFormat, 100, out)
            }
            FileProvider.getUriForFile(this, "$packageName.file-provider", file)
        } catch (e: IOException) {
            Log.e(TAG, "storeBitmap", e)
            null
        }
    }
}
