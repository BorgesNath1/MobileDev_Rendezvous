package com.borgesnath1.ui

import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.borgesnath1.MainViewModel
import com.borgesnath1.enums.Status
import com.borgesnath1.ui.theme.AppDimens
import com.borgesnath1.ui.theme.BorderSubtle
import com.borgesnath1.ui.theme.DarkSurface
import com.borgesnath1.ui.theme.GoldBadgeBg
import com.borgesnath1.ui.theme.GoldDarkBorder
import com.borgesnath1.ui.theme.GoldPrimary
import com.borgesnath1.ui.theme.StatusConfirmedGreen
import com.borgesnath1.ui.theme.StatusRefusedRed
import com.borgesnath1.ui.theme.TextGray
import com.borgesnath1.ui.theme.TextWhite
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.time.format.DateTimeFormatter

@Composable
fun DetalhesEventoPage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit = {}
) {
    val activity = LocalActivity.current as Activity
    val context = LocalContext.current
    val evento = viewModel.eventoSelecionado

    if (evento == null) {
        Column(
            modifier = modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Nenhum encontro selecionado.", color = TextWhite)
            OutlinedButton(onClick = onBack, modifier = Modifier.padding(top = 12.dp)) {
                Text("Voltar")
            }
        }
        return
    }

    var emailConvidado by rememberSaveable { mutableStateOf("") }
    val dataFormatada = evento.data.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    val horaFormatada = evento.data.format(DateTimeFormatter.ofPattern("HH:mm"))
    val coordenadaEvento = evento.localizacao ?: LatLng(-8.0522, -34.8951)

    val camPosState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(coordenadaEvento, 14f)
    }

    val hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(AppDimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            onClick = onBack,
            border = BorderStroke(1.dp, GoldDarkBorder),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldPrimary)
        ) {
            Text("← Voltar")
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(AppDimens.CardCornerRadius),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = BorderStroke(1.dp, GoldDarkBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(GoldBadgeBg, RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("Encontro", fontSize = 10.sp, color = GoldPrimary, fontWeight = FontWeight.Bold)
                }
                Text(
                    text = evento.nome,
                    fontSize = 24.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = "$dataFormatada • $horaFormatada",
                    fontSize = 13.sp,
                    color = GoldPrimary
                )
            }
        }

        Text(
            text = "SUA PRESENÇA",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextGray
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PresencaButton(
                texto = "✓ Confirmado",
                selecionado = evento.status == Status.CONFIRMADO,
                corDestaque = StatusConfirmedGreen,
                onClick = {
                    viewModel.atualizarStatus(evento, Status.CONFIRMADO)
                    Toast.makeText(activity, "Presença confirmada!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f)
            )
            PresencaButton(
                texto = "? Pendente",
                selecionado = evento.status == Status.AGUARDANDO,
                corDestaque = GoldPrimary,
                onClick = {
                    viewModel.atualizarStatus(evento, Status.AGUARDANDO)
                    Toast.makeText(activity, "Status alterado para Pendente", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f)
            )
            PresencaButton(
                texto = "✕ Recusar",
                selecionado = evento.status == Status.RECUSADO,
                corDestaque = StatusRefusedRed,
                onClick = {
                    viewModel.atualizarStatus(evento, Status.RECUSADO)
                    Toast.makeText(activity, "Convite recusado", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f)
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(AppDimens.CardCornerRadius),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Informações do Encontro", color = GoldPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                HorizontalDivider(color = BorderSubtle)
                InfoRow(label = "📅  DATA", valor = dataFormatada)
                HorizontalDivider(color = BorderSubtle)
                InfoRow(label = "⏰  HORÁRIO", valor = horaFormatada)
                HorizontalDivider(color = BorderSubtle)
                InfoRow(label = "📍  LOCAL", valor = evento.descricao ?: "Café Bom Dia, Pinheiros")
            }
        }

        Text(
            text = "MAPA DO LOCAL (Toque para ajustar o ponto)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = GoldPrimary
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(AppDimens.CardCornerRadius))
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = camPosState,
                onMapClick = { novaCoordenada ->
                    viewModel.atualizarLocalizacao(evento, novaCoordenada)
                    Toast.makeText(activity, "Local do encontro atualizado no mapa!", Toast.LENGTH_SHORT).show()
                },
                properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
                uiSettings = MapUiSettings(myLocationButtonEnabled = true, zoomControlsEnabled = true)
            ) {
                Marker(
                    state = MarkerState(position = coordenadaEvento),
                    title = evento.nome,
                    snippet = evento.descricao ?: "Local do Café"
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(AppDimens.CardCornerRadius),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = BorderStroke(1.dp, GoldDarkBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Chamar Amigos",
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = "CHAMAR POR E-MAIL OU NOME",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary
                )
                OutlinedTextField(
                    value = emailConvidado,
                    onValueChange = { emailConvidado = it },
                    placeholder = { Text("convidado@email.com", color = TextGray) },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        viewModel.adicionarConvidado(evento, emailConvidado)
                        Toast.makeText(activity, "Convite enviado para $emailConvidado!", Toast.LENGTH_SHORT).show()
                        emailConvidado = ""
                    },
                    enabled = emailConvidado.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enviar Convite", fontWeight = FontWeight.Bold)
                }

                HorizontalDivider(color = BorderSubtle)

                Text(
                    text = "LINK DE CONVITE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "rendezvous.app/cafe/${evento.id}",
                        fontSize = 12.sp,
                        color = TextGray,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedButton(
                        onClick = {
                            Toast.makeText(activity, "Link copiado!", Toast.LENGTH_SHORT).show()
                        },
                        border = BorderStroke(1.dp, GoldPrimary)
                    ) {
                        Text("Copiar", fontSize = 12.sp, color = GoldPrimary)
                    }
                }
            }
        }

        val convidados = evento.convidados ?: emptyList()
        Text(
            text = "CONVIDADOS (${convidados.size})",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = GoldPrimary
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(AppDimens.CardCornerRadius),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                convidados.forEachIndexed { index, convidado ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "👤  ${convidado.usuario.nome}",
                            fontSize = 14.sp,
                            color = TextWhite,
                            fontWeight = FontWeight.Medium
                        )
                        Box(
                            modifier = Modifier
                                .background(GoldBadgeBg, RoundedCornerShape(50))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (convidado.status == Status.CONFIRMADO) "Confirmado" else "Pendente",
                                fontSize = 11.sp,
                                color = if (convidado.status == Status.CONFIRMADO) GoldPrimary else TextGray
                            )
                        }
                    }
                    if (index < convidados.lastIndex) {
                        HorizontalDivider(color = BorderSubtle)
                    }
                }
            }
        }
    }
}

@Composable
private fun PresencaButton(
    texto: String,
    selecionado: Boolean,
    corDestaque: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        border = BorderStroke(1.dp, if (selecionado) corDestaque else BorderSubtle),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selecionado) GoldBadgeBg else DarkSurface,
            contentColor = if (selecionado) corDestaque else TextGray
        )
    ) {
        Text(text = texto, fontSize = 11.sp, maxLines = 1, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun InfoRow(label: String, valor: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(text = label, fontSize = 11.sp, color = TextGray)
        Text(text = valor, fontSize = 15.sp, color = TextWhite, fontWeight = FontWeight.SemiBold)
    }
}
