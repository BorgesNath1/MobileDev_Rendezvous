package com.borgesnath1.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.borgesnath1.MainViewModel
import com.borgesnath1.enums.Status
import com.borgesnath1.ui.theme.BorderSubtle
import com.borgesnath1.ui.theme.GoldBadgeBg
import com.borgesnath1.ui.theme.GoldPrimary
import com.borgesnath1.ui.theme.TextGray
import com.borgesnath1.ui.theme.TextWhite

@Composable
fun ListPage(
    modifier: Modifier = Modifier.Companion,
    viewModel: MainViewModel,
    onNavigateToDetails: () -> Unit = {},
    onNavigateToCreate: () -> Unit = {}
) {
    val activity = LocalActivity.current as Activity
    var filtroSelecionado by rememberSaveable { mutableStateOf("Todos") }

    val eventosFiltrados = when (filtroSelecionado) {
        "Confirmados" -> viewModel.eventos.filter { it.status == Status.CONFIRMADO }
        "Aguardando" -> viewModel.eventos.filter { it.status == Status.AGUARDANDO }
        else -> viewModel.eventos
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Meus Encontros",
            fontSize = 28.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )
        Text(
            text = "Seus cafés marcados",
            fontSize = 15.sp,
            fontStyle = FontStyle.Italic,
            color = GoldPrimary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Todos", "Confirmados", "Aguardando").forEach { filtro ->
                val ativo = filtroSelecionado == filtro
                OutlinedButton(
                    onClick = { filtroSelecionado = filtro },
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, if (ativo) GoldPrimary else BorderSubtle),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (ativo) GoldBadgeBg else androidx.compose.ui.graphics.Color.Transparent,
                        contentColor = if (ativo) GoldPrimary else TextGray
                    )
                ) {
                    Text(text = filtro, fontSize = 12.sp, maxLines = 1)
                }
            }
        }

        OutlinedButton(
            onClick = onNavigateToCreate,
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, GoldPrimary),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = GoldBadgeBg,
                contentColor = GoldPrimary
            )
        ) {
            Text(text = "+  Marcar Novo Café", fontWeight = FontWeight.Bold)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = eventosFiltrados,
                key = { evento -> evento.id }
            ) { evento ->
                EventoItem(
                    evento = evento,
                    onClose = {
                        viewModel.remove(evento)
                        Toast.makeText(
                            activity, "Encontro '${evento.nome}' removido",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onClick = {
                        viewModel.eventoSelecionado = evento
                        onNavigateToDetails()
                    }
                )
            }
        }
    }
}
