package com.borgesnath1.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.LocalCafe
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.borgesnath1.MainViewModel
import com.borgesnath1.R
import com.borgesnath1.enums.Status
import com.borgesnath1.model.Evento
import com.borgesnath1.ui.theme.AppDimens
import com.borgesnath1.ui.theme.BorderSubtle
import com.borgesnath1.ui.theme.DarkSurface
import com.borgesnath1.ui.theme.GoldBadgeBg
import com.borgesnath1.ui.theme.GoldDarkBorder
import com.borgesnath1.ui.theme.GoldPrimary
import com.borgesnath1.ui.theme.TextDarkOnGold
import com.borgesnath1.ui.theme.TextGray
import com.borgesnath1.ui.theme.TextWhite
import java.time.format.DateTimeFormatter

@Composable
fun HomePage(
    modifier: Modifier = Modifier.Companion,
    viewModel: MainViewModel,
    onNavigateToDetails: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    val eventosList = viewModel.eventos
    val proximoEvento = eventosList.firstOrNull()
    val confirmadosCount = eventosList.count { it.status == Status.CONFIRMADO }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(AppDimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "OLÁ,",
                    fontSize = 12.sp,
                    color = TextGray
                )
                Text(
                    text = viewModel.user?.nome ?: "Ana Souza",
                    fontSize = 24.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }
            Image(
                painter = painterResource(id = R.drawable.pessoa),
                contentDescription = "Foto de Perfil",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, GoldPrimary, CircleShape)
                    .clickable { onNavigateToProfile() },
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ResumoCard(valor = "${eventosList.size}", legenda = "marcados", modifier = Modifier.weight(1f))
            ResumoCard(valor = "$confirmadosCount", legenda = "confirmados", modifier = Modifier.weight(1f))
            ResumoCard(valor = "3d", legenda = "dias restantes", modifier = Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PRÓXIMO ENCONTRO",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary
            )
        }

        if (proximoEvento != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.eventoSelecionado = proximoEvento
                        onNavigateToDetails()
                    },
                shape = RoundedCornerShape(AppDimens.CardCornerRadius),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = BorderStroke(1.dp, GoldDarkBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(GoldBadgeBg, RoundedCornerShape(50))
                                .border(1.dp, GoldDarkBorder, RoundedCornerShape(50))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("Encontro", fontSize = 10.sp, color = GoldPrimary, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            modifier = Modifier
                                .background(GoldPrimary, RoundedCornerShape(50))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("ADMIN", fontSize = 10.sp, color = TextDarkOnGold, fontWeight = FontWeight.Bold)
                        }
                    }

                    Text(
                        text = proximoEvento.nome,
                        fontSize = 22.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = proximoEvento.descricao ?: "",
                        fontSize = 13.sp,
                        color = GoldPrimary
                    )
                }
            }
        }

        if (eventosList.size > 1) {
            Text(
                text = "OUTROS ENCONTROS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary
            )
            eventosList.drop(1).forEach { evento ->
                EventoItem(
                    evento = evento,
                    onClick = {
                        viewModel.eventoSelecionado = evento
                        onNavigateToDetails()
                    },
                    onClose = { viewModel.remove(evento) }
                )
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(AppDimens.CardCornerRadius),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = BorderStroke(1.dp, GoldDarkBorder)
            ) {
                Text(
                    text = "Não há outros encontros agendados",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ResumoCard(valor: String, legenda: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(AppDimens.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = valor,
                fontSize = 22.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary
            )
            Text(
                text = legenda,
                fontSize = 11.sp,
                color = TextGray
            )
        }
    }
}

@Composable
fun AcaoRapidaButton(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(54.dp),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, BorderSubtle),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = DarkSurface,
            contentColor = TextWhite
        )
    ) {
        Text(text = texto, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun EventoItem(
    evento: Evento,
    onClick: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
){
    val dataFormatada = evento.data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy • HH:mm"))
    val qtdConvidados = evento.convidados?.size ?: 1

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(AppDimens.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, GoldDarkBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Rounded.LocalCafe,
                    contentDescription = "Café",
                    tint = GoldPrimary
                )
                Spacer(modifier = Modifier.size(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = evento.nome,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "📅 $dataFormatada",
                        fontSize = 12.sp,
                        color = TextGray
                    )
                    Text(
                        text = "📍 ${evento.descricao ?: "Local a definir"}",
                        fontSize = 13.sp,
                        color = GoldPrimary
                    )
                }
                IconButton(onClick = onClose){
                    Icon(Icons.Filled.Close, contentDescription = "Remover", tint = TextGray)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(GoldBadgeBg, RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${evento.status ?: Status.AGUARDANDO} • $qtdConvidados convidados",
                        fontSize = 11.sp,
                        color = GoldPrimary
                    )
                }

                OutlinedButton(
                    onClick = onClick,
                    border = BorderStroke(1.dp, GoldPrimary),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldPrimary)
                ) {
                    Text("Detalhes / Convite", fontSize = 12.sp)
                }
            }
        }
    }
}
