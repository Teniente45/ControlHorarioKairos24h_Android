/*
 * Copyright (c) 2025 Juan López
 * Todos los derechos reservados.
 *
 * Este archivo forma parte de la aplicación Kairos24h.
 * Proyecto académico de desarrollo Android.
 */

package com.miapp.kairos24h

// ──────────────────────────────── GUÍA DE LOGS ────────────────────────────────
// SesionDebug         → Muestra las credenciales almacenadas localmente (usuario, password).
// Redireccion         → Indica hacia qué pantalla se redirige al usuario (TABLET o EMPLEADO).
//                      También muestra el valor de cTipEmp devuelto desde el backend.
// ─────────────────────────────────────────────────────────────────────────────

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.miapp.kairos24h.enlaces_internos.BuildURLmovil
import com.miapp.kairos24h.enlaces_internos.ImagenesMovil
import com.miapp.kairos24h.sesionesYSeguridad.AuthManager
import com.miapp.kairos24h.movilAPK.Fichar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Obtenemos las credenciales almacenadas (usuario, password y xEmpleado)
        val (storedUser, storedPassword, _) = AuthManager.getUserCredentials(this)
        android.util.Log.d("SesionDebug", "storedUser='$storedUser' storedPassword='$storedPassword'")

        if (storedUser.isNotBlank() && storedPassword.isNotBlank()) {
            // Si existen credenciales, redirigimos según cTipEmp guardado en las credenciales
            val cTipEmp = AuthManager.getUserCredentials(this).cTipEmp.uppercase()
            android.util.Log.d("Redireccion", "Valor de cTipEmp: $cTipEmp")
            if (cTipEmp == "TABLET") {
                android.util.Log.d("Redireccion", "Iniciando MainActivity (modo TABLET)")
                val intent = Intent(this@MainActivity, com.miapp.kairos24h.tabletAPK.MainActivityTablet::class.java)
                startActivity(intent)
            } else {
                android.util.Log.d("Redireccion", "Iniciando Fichar (modo APK)")
                navigateToFichar(storedUser, storedPassword)
            }
        } else {
            android.util.Log.d("SesionDebug", "Credenciales vacías: se mostrará la pantalla de login")
            // Mostrar pantalla de inicio de sesión
            setContent {
                MaterialTheme {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            DisplayLogo(
                                onSubmit = { usuario: String, password: String ->
                                    if (!isInternetAvailable(this@MainActivity)) {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Compruebe su conexión a Internet",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        return@DisplayLogo
                                    }

                                    if (usuario.isNotEmpty() && password.isNotEmpty()) {
                                        lifecycleScope.launch(Dispatchers.IO) {
                                            try {
                                                val (success, xEmpleado) = AuthManager.authenticateUser(
                                                    usuario,
                                                    password
                                                )
                                                runOnUiThread {
                                                    if (success && xEmpleado != null) {
                                                        AuthManager.saveUserCredentials(
                                                            this@MainActivity,
                                                            xEmpleado.usuario,
                                                            xEmpleado.password,
                                                            xEmpleado.xEmpleado,
                                                            xEmpleado.lComGPS,
                                                            xEmpleado.lComIP,
                                                            xEmpleado.lBotonesFichajeMovil,
                                                            xEmpleado.xEntidad,
                                                            xEmpleado.sEmpleado,
                                                            xEmpleado.tUrlCPP,
                                                            xEmpleado.tLogo,
                                                            xEmpleado.cTipEmp
                                                        )
                                                    val cTipEmp = xEmpleado.cTipEmp.uppercase()
                                                    android.util.Log.d("Redireccion", "Valor de cTipEmp: $cTipEmp")
                                                    if (cTipEmp == "TABLET") {
                                                        android.util.Log.d("Redireccion", "Iniciando MainActivity (modo TABLET)")
                                                        val intent = Intent(this@MainActivity, com.miapp.kairos24h.tabletAPK.MainActivityTablet::class.java)
                                                        startActivity(intent)
                                                    } else if (cTipEmp == "EMPLEADO" || cTipEmp.isBlank()) {
                                                        android.util.Log.d("Redireccion", "Iniciando Fichar (modo APK)")
                                                        navigateToFichar(xEmpleado.usuario, xEmpleado.password)
                                                    }
                                                    } else {
                                                        Toast.makeText(
                                                            this@MainActivity,
                                                            "Usuario o contraseña incorrectos",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                runOnUiThread {
                                                    Toast.makeText(
                                                        this@MainActivity,
                                                        "Error de autenticación: ${e.message}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }
                                    } else {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Por favor, completa ambos campos",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                onForgotPassword = {
                                    val url = BuildURLmovil.getForgotPassword(this@MainActivity)
                                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                    startActivity(intent)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Inicia la actividad Fichar pasando usuario y contraseña como extras
    private fun navigateToFichar(usuario: String, password: String) {
        val intent = Intent(this, Fichar::class.java)
        intent.putExtra("usuario", usuario)
        intent.putExtra("password", password)
        startActivity(intent)
        finish() // Finaliza la actividad actual para evitar volver atrás
    }


    // Verifica si el dispositivo tiene conexión a Internet activa, ya sea por WiFi o datos móviles.
    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}

// Composable que representa la pantalla de login, mostrando el formulario de acceso, gestión de permisos y logo de la app.
@Composable
fun DisplayLogo(
    onSubmit: (String, String) -> Unit,
    onForgotPassword: () -> Unit
) {
    // Estado que almacena el texto ingresado en el campo de usuario
    val usuario = remember { mutableStateOf("") }
    // Estado que almacena el texto ingresado en el campo de contraseña
    val password = remember { mutableStateOf("") }

    // Controla si la contraseña se muestra en texto plano o se oculta
    var passwordVisible by remember { mutableStateOf(false) }
    // Mensaje de error a mostrar si los datos de acceso son incorrectos
    val errorMessage by remember { mutableStateOf("") }
    // Obtiene el contexto actual de la aplicación
    val context = LocalContext.current
    // Indica si el usuario ha concedido el permiso de ubicación
    var isLocationChecked by remember { mutableStateOf(false) }
    // Lleva la cuenta de las veces que el permiso de ubicación ha sido denegado
    var locationPermissionDeniedCount by remember { mutableIntStateOf(0) }
    // Lanza la solicitud de permiso de ubicación y maneja su resultado
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        isLocationChecked = isGranted
        if (!isGranted) {
            locationPermissionDeniedCount++
            if (locationPermissionDeniedCount >= 3) {
                Toast.makeText(context, "Debe habilitar los permisos de GPS manualmente en los ajustes.", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Debe aceptar los permisos de GPS para continuar.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Contenedor principal del contenido de login, desplazado hacia arriba
        Box(
            modifier = Modifier
                .padding(4.dp)
                .offset(y = (-40).dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Imagen del logo de la empresa o aplicación
                Image(
                    painter = painterResource(id = ImagenesMovil.logoCliente),
                    contentDescription = "logo del cliente",
                    modifier = ImagenesMovil.logoModifier
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Campo de texto para el nombre de usuario
                OutlinedTextField(
                    value = usuario.value,
                    onValueChange = { newValue ->
                        usuario.value = newValue.filter { it != ' ' } // Elimina espacios en blanco
                    },
                    label = { Text("Usuario") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    textStyle = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Campo de texto para la contraseña
                OutlinedTextField(
                    value = password.value,
                    onValueChange = { newValue ->
                        password.value = newValue.filter { it != ' ' } // Elimina espacios en blanco
                    },
                    label = { Text("Contraseña") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    textStyle = MaterialTheme.typography.bodyLarge
                )

                // Contenedor centralizado para las opciones
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Opción para mostrar u ocultar la contraseña introducida
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Checkbox(
                            checked = passwordVisible,
                            onCheckedChange = { passwordVisible = it }
                        )
                        Text(
                            text = "Mostrar contraseña",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    // Solicita al usuario que acepte el permiso de ubicación
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Checkbox(
                            checked = isLocationChecked,
                            onCheckedChange = { checked ->
                                if (checked) {
                                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                } else {
                                    isLocationChecked = false
                                }
                            }
                        )
                        Text(
                            text = "Acepto que la app acceda a la ubicación donde ficho",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                // Muestra un mensaje de error si existe
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Botón para enviar los datos de acceso
                Button(
                    onClick = {
                        val trimmedUsuario = usuario.value.trim()
                        val trimmedPassword = password.value.trim()

                        // Si los campos están completos, codifica las credenciales y las envía
                        if (trimmedUsuario.isNotEmpty() && trimmedPassword.isNotEmpty()) {
                            val encodedUsuario = URLEncoder.encode(trimmedUsuario, StandardCharsets.UTF_8.toString())
                            val encodedPassword = URLEncoder.encode(trimmedPassword, StandardCharsets.UTF_8.toString())
                            onSubmit(encodedUsuario, encodedPassword)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7599B6)),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = usuario.value.isNotEmpty() && password.value.isNotEmpty() && isLocationChecked
                ) {
                    Text("Acceso", color = Color.White, style = MaterialTheme.typography.bodyLarge)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Texto interactivo para recuperar la contraseña
                Text(
                    text = "¿Olvidaste la contraseña?",
                    color = Color(0xFF7599B6),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onForgotPassword() },
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Mensaje informativo sobre la trazabilidad y seguridad del sistema
                Text(
                    text = """
                        Para control de calidad y aumentar la seguridad de nuestro sistema, todos los accesos, acciones, consultas o cambios (Trazabilidad) que realice dentro de Kairos24h serán almacenados.
                        Les recordamos que la Empresa podrá auditar los medios técnicos que pone a disposición del Trabajador para el desempeño de sus funciones.
                    """.trimIndent(),
                    color = Color(0xFF447094),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 25.dp)
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}

// Vista previa para Android Studio
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        DisplayLogo(onSubmit = { _: String, _: String -> }, onForgotPassword = {})
    }
}

