import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.control_zetas.R // Asegúrate de que esto esté correcto, dependiendo de tu paquete.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompacto() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mi App Kotlin") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(text = "Bienvenido")
            Button(onClick = {}) {
                Text("Presioname")
            }

            // Cargar la imagen JPG desde drawable
                Image(
                    painter = painterResource(id = R.drawable.logo), // Referencia a tu logo.jpg
                    contentDescription = "Logo", // Descripción para accesibilidad
                    modifier = Modifier
                        .fillMaxWidth() // Ajusta el ancho al 100%
                        .height(150.dp), // Ajusta la altura que necesites
                    contentScale = ContentScale.Fit // Ajusta la imagen sin recortar
                )
            }
        }
    }

