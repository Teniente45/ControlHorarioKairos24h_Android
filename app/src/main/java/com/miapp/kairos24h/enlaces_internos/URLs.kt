/*
 * Copyright (c) 2025 Juan López
 * Todos los derechos reservados.
 *
 * Este archivo forma parte de la aplicación Kairos24h.
 * Proyecto académico de desarrollo Android.
 */

package com.miapp.kairos24h.enlaces_internos

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import com.miapp.kairos24h.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.widget.ImageView
import coil.load
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.res.painterResource
import com.miapp.kairos24h.sesionesYSeguridad.AuthManager

// Este objeto centraliza el acceso a los recursos gráficos usados en la aplicación
object ImagenesMovil {
    // Imagen del logo principal que se muestra en la pantalla de login
    @DrawableRes
    val logoCliente = R.drawable.kairos24h
    fun getLogoClienteXPrograma(context: Context): String? {
        val tLogo = AuthManager.getUserCredentials(context).tLogo
        return if (tLogo.isNotBlank() && tLogo != "null") tLogo else null
    }
    val lodoDesarrolladora = R.drawable.logo_i3data



    // Centraliza las características del logo de empresa cliente
    val logoBoxModifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 5.dp)
    // Estilo aplicado al logo (tamaño y proporción)
    val logoModifier = Modifier
        .width(356.dp)
        .height(100.dp)


    // Centraliza las características del contenedor del logo de desarrolladora
    val logoBoxModifierDev = Modifier
        .fillMaxWidth()
    // Estilo aplicado al logo de desarrolladora (tamaño y proporción)
    val logoModifierDev = Modifier
        .width(200.dp)
        .height(75.dp)

    @Composable
    fun LogoClienteRemoto(modifier: Modifier = Modifier) {
        val context = LocalContext.current
        val logoUrl = getLogoClienteXPrograma(context)
        val painter = rememberAsyncImagePainter(
            model = logoUrl ?: R.drawable.kairos24h,
            contentScale = ContentScale.Fit,
            placeholder = painterResource(id = R.drawable.kairos24h),
            error = painterResource(id = R.drawable.kairos24h)
        )

        Image(
            painter = painter,
            contentDescription = "Logo del cliente",
            modifier = modifier
        )
    }
}



// Estás son las URL que se nos mostrarán en el WebView, se usa sólo para logearse desde la APK
object WebViewURL {
    const val HOST = "https://controlhorario.kairos24h.es"
    const val ENTRY_POINT = "/index.php"
    const val URL_USADA = "$HOST$ENTRY_POINT"

    const val ACTION_LOGIN = "r=wsExterno/loginExterno"

    const val LOGINAPK = "$URL_USADA?$ACTION_LOGIN"
}

// Esta será la URL que construiremos cuando desde el login de nuestra APK introduzcamos el Usuario y la Contraseña
object BuildURLmovil {
    // Remove HOST constant and use function instead
    fun getHost(context: Context): String {
        val tUrlCPP = AuthManager.getUserCredentials(context).tUrlCPP
        val hostFinal = if (tUrlCPP.isNotBlank() && tUrlCPP != "null") tUrlCPP else WebViewURL.HOST
        android.util.Log.d("BuildURLmovil", "Host seleccionado: $hostFinal")
        return hostFinal
    }
    const val ENTRY_POINT = "/index.php"
    fun getURLUsada(context: Context): String = getHost(context) + ENTRY_POINT + "?"

    const val ACTION_FORGOTPASS = "r=site/solicitudRestablecerClave"

    const val ACTION_LOGIN = "r=site/index"
    const val ACTION_FICHAJE = "r=explotacion/creaFichaje"
    const val ACTION_CONSULTAR = "r=explotacion/consultarExplotacion"

    const val ACTION_CONSULTHORARIO = "r=wsExterno/consultarHorarioExterno"
    const val ACTION_CONSULTFIC_DIA = "r=wsExterno/consultarFichajesExterno"
    const val ACTION_CONSULT_ALERTAS = "r=wsExterno/consultarAlertasExterno"

    fun getIndex(context: Context): String = getURLUsada(context) + ACTION_LOGIN
    fun getForgotPassword(context: Context): String = getURLUsada(context) + ACTION_FORGOTPASS

    fun getFichaje(context: Context): String {
        val url = getURLUsada(context) + ACTION_CONSULTAR + "&cTipExp=FICHAJE"
        android.util.Log.d("URL_Fichaje", "URL generada: $url")
        return url
    }

    fun getIncidencia(context: Context): String {
        val url = getURLUsada(context) + ACTION_CONSULTAR + "&cTipExp=INCIDENCIA&cOpcionVisual=INCBAN"
        android.util.Log.d("URL_Incidencia", "URL generada: $url")
        return url
    }

    fun getHorarios(context: Context): String {
        val url = getURLUsada(context) + ACTION_CONSULTAR + "&cTipExp=HORARIO&cModoVisual=HORMEN"
        android.util.Log.d("URL_Horarios", "URL generada: $url")
        return url
    }

    fun getSolicitudes(context: Context): String {
        val url = getURLUsada(context) + ACTION_CONSULTAR + "&cTipExp=SOLICITUD"
        android.util.Log.d("URL_Solicitudes", "URL generada: $url")
        return url
    }

    const val X_GRUPO = ""
    const val C_KIOSKO = ""
    const val C_FIC_ORI = "APP"

    fun getStaticParams(context: Context): String {
        val creds = AuthManager.getUserCredentials(context)
        val xEntidad = creds.xEntidad ?: ""
        val xEmpleado = creds.xEmpleado ?: ""
        return "&xGrupo=$X_GRUPO" +
                "&xEntidad=$xEntidad" +
                "&xEmpleado=$xEmpleado" +
                "&cKiosko=$C_KIOSKO" +
                "&cFicOri=$C_FIC_ORI"
    }

    fun getCrearFichaje(context: Context): String = getURLUsada(context) + ACTION_FICHAJE + getStaticParams(context)
    fun getMostrarHorarios(context: Context): String = getURLUsada(context) + ACTION_CONSULTHORARIO + getStaticParams(context)
    fun getMostrarFichajes(context: Context): String = getURLUsada(context) + ACTION_CONSULTFIC_DIA + getStaticParams(context)
    fun getMostrarAlertas(context: Context): String = getURLUsada(context) + ACTION_CONSULT_ALERTAS + getStaticParams(context)
}




object BuildURLtablet {
    fun getHost(context: Context): String {
        val tUrlCPP = AuthManager.getUserCredentials(context).tUrlCPP
        val hostFinal = if (tUrlCPP.isNotBlank() && tUrlCPP != "null") tUrlCPP else WebViewURL.HOST
        android.util.Log.d("BuildURLtablet", "Host seleccionado: $hostFinal")
        return hostFinal
    }

    const val ACTION = "index.php?r=wsExterno/crearFichajeExterno"

    fun getParams(context: Context): String {
        val credenciales = AuthManager.getUserCredentials(context)
        val xEntidad = credenciales.xEntidad
        return "&xEntidad=$xEntidad" +
                "&cKiosko=TABLET1" +
                "&cEmpCppExt=" +
                "&cTipFic=" +
                "&cFicOri=PUEFIC"
    }

    fun getSetFichaje(context: Context): String = getHost(context) + "/" + ACTION + getParams(context)
}

object ImagenesTablet {
    // Permite reutilizar el valor de tLogo desde cualquier otro archivo
    fun getLogoCliente(context: Context): String? {
        val tLogo = AuthManager.getUserCredentials(context).tLogo
        return if (tLogo.isNotBlank()) tLogo else null
    }
    // Nombres de recursos en drawable
    const val LOGO_DESARROLLADORA = "logo_desarrolladora"

    data class PropiedadesImagen(
        val width: String,
        val height: String,
        val gravity: String,
        val marginTop: String = "0sp",
        val marginBottom: String = "0sp"
    )

    object Vertical {
        val LOGO_CLIENTE = PropiedadesImagen(
            width = "80%",
            height = "10%",
            gravity = "center_horizontal",
        )
        val LOGO_DESARROLLADORA = PropiedadesImagen("match_parent", "wrap_content", "center_horizontal")
    }

    object Horizontal {
        val LOGO_CLIENTE = PropiedadesImagen(
            width = "80%",
            height = "10%",
            gravity = "center_horizontal",
        )
        val LOGO_DESARROLLADORA = PropiedadesImagen(
            width = "60%",
            height = "60dp",
            gravity = "center_horizontal",
        )
    }

    fun cargarLogoClienteEnImageView(context: Context, imageView: ImageView) {
        val tLogo = getLogoCliente(context)
        imageView.load(if (!tLogo.isNullOrBlank() && tLogo != "null") tLogo else R.drawable.kairos24h) {
            placeholder(R.drawable.kairos24h)
            error(R.drawable.kairos24h)
        }
    }
}