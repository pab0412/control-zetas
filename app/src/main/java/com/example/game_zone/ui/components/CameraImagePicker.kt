package com.example.game_zone.ui.components

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.game_zone.ui.utils.ImageUtils
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraImagePicker(
    currentImageUrl: String?,
    onImageSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    showOptions: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }

    // Permisos
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    // Launcher para la cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempPhotoUri != null) {
            val savedPath = ImageUtils.saveImageToInternalStorage(context, tempPhotoUri!!)
            if (savedPath != null) {
                // Eliminar imagen anterior si existe
                ImageUtils.deleteOldProfileImage(currentImageUrl)
                onImageSelected(savedPath)
                Toast.makeText(context, "Imagen guardada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error al guardar imagen", Toast.LENGTH_SHORT).show()
            }
        }
        onDismiss()
    }

    // Launcher para la galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val savedPath = ImageUtils.saveImageToInternalStorage(context, it)
            if (savedPath != null) {
                // Eliminar imagen anterior si existe
                ImageUtils.deleteOldProfileImage(currentImageUrl)
                onImageSelected(savedPath)
                Toast.makeText(context, "Imagen guardada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error al guardar imagen", Toast.LENGTH_SHORT).show()
            }
        }
        onDismiss()
    }

    if (showOptions) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Seleccionar imagen") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Opción de cámara
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (cameraPermissionState.status.isGranted) {
                                    // Crear archivo temporal para la foto
                                    val photoFile = ImageUtils.createImageFile(context)
                                    tempPhotoUri = FileProvider.getUriForFile(
                                        context,
                                        "${context.packageName}.fileprovider",
                                        photoFile
                                    )
                                    cameraLauncher.launch(tempPhotoUri!!)
                                } else {
                                    cameraPermissionState.launchPermissionRequest()
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CameraAlt,
                                contentDescription = "Cámara",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Tomar foto")
                        }
                    }

                    // Opción de galería
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                galleryLauncher.launch("image/*")
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PhotoLibrary,
                                contentDescription = "Galería",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Elegir de galería")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        )
    }
}