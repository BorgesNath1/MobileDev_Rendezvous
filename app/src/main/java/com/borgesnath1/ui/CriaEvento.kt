package com.borgesnath1.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
import com.borgesnath1.model.Convidado
import com.borgesnath1.model.Usuario
import com.borgesnath1.ui.theme.GoldPrimary
import com.borgesnath1.ui.theme.TextGray
import com.borgesnath1.ui.theme.TextWhite
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime

@Composable
fun CriaEvento(
    modifier: Modifier = Modifier.Companion,
    viewModel: MainViewModel,
    onEventoCriado: () -> Unit = {}
) {
    val activity = LocalActivity.current as Activity

    var nomeEncontro by rememberSaveable { mutableStateOf("") }
    var dataHorario by rememberSaveable { mutableStateOf("18/10/2026 - 09:00") }
    var localCafe by rememberSaveable { mutableStateOf("") }
    var descricao by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column {
            Text(
                text = "Marcar um Café",
                fontSize = 28.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Text(
                text = "Simples e sem complicação",
                fontSize = 15.sp,
                fontStyle = FontStyle.Italic,
                color = GoldPrimary
            )
        }

        OutlinedTextField(
            value = nomeEncontro,
            onValueChange = { nomeEncontro = it },
            label = { Text("NOME DO ENCONTRO", color = GoldPrimary, fontSize = 11.sp) },
            placeholder = { Text("Ex: Café da manhã na Figueira", color = TextGray) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = dataHorario,
            onValueChange = { dataHorario = it },
            label = { Text("DATA E HORÁRIO", color = GoldPrimary, fontSize = 11.sp) },
            placeholder = { Text("dd/mm/aaaa - --:--", color = TextGray) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = localCafe,
            onValueChange = { localCafe = it },
            label = { Text("CAFÉ / LOCAL", color = GoldPrimary, fontSize = 11.sp) },
            placeholder = { Text("Ex: Café Bom Dia, Pinheiros", color = TextGray) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("DESCRIÇÃO", color = GoldPrimary, fontSize = 11.sp) },
            placeholder = { Text("Conta um pouco mais sobre o rolê...", color = TextGray) },
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
        )

        Button(
            onClick = {
                val criador = viewModel.user ?: Usuario("1", "Ana Souza", contatos = emptyList())
                val descFinal = buildString {
                    if (localCafe.isNotBlank()) append(" • $localCafe")
                    if (descricao.isNotBlank()) append(" — $descricao")
                }
                viewModel.add(
                    id = (viewModel.eventos.maxOfOrNull { it.id } ?: 0) + 1,
                    nome = nomeEncontro,
                    data = LocalDateTime.now().plusDays(2),
                    descricao = descFinal,
                    localizacao = LatLng(-8.0522, -34.8951),
                    status = Status.CONFIRMADO,
                    proprietario = criador,
                    convidados = listOf(Convidado(criador, Status.CONFIRMADO))
                )
                Toast.makeText(activity, "Encontro '$nomeEncontro' criado!", Toast.LENGTH_LONG).show()
                nomeEncontro = ""
                localCafe = ""
                descricao = ""
                onEventoCriado()
            },
            enabled = nomeEncontro.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Marcar Café", fontWeight = FontWeight.Bold)
        }
    }
}
