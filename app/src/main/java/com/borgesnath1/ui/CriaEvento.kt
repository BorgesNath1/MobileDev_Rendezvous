package com.borgesnath1.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable // <- alterado aqui
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row // <- alterado aqui
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog // <- alterado aqui
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker // <- alterado aqui
import androidx.compose.material3.DatePickerDialog // <- alterado aqui
import androidx.compose.material3.ExperimentalMaterial3Api // <- alterado aqui
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults // <- alterado aqui
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton // <- alterado aqui
import androidx.compose.material3.TimePicker // <- alterado aqui
import androidx.compose.material3.rememberDatePickerState // <- alterado aqui
import androidx.compose.material3.rememberTimePickerState // <- alterado aqui
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
import com.borgesnath1.ui.theme.GoldPrimary
import com.borgesnath1.ui.theme.TextGray
import com.borgesnath1.ui.theme.TextWhite
import com.google.android.gms.maps.model.LatLng
import java.time.Instant // <- alterado aqui
import java.time.LocalDate // <- alterado aqui
import java.time.LocalDateTime
import java.time.LocalTime // <- alterado aqui
import java.time.ZoneOffset // <- alterado aqui
import java.time.format.DateTimeFormatter // <- alterado aqui

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriaEvento(
    modifier: Modifier = Modifier.Companion,
    viewModel: MainViewModel,
    onEventoCriado: () -> Unit = {}
) {
    val activity = LocalActivity.current as Activity

    var nomeEncontro by rememberSaveable { mutableStateOf("") }
    var dataSelecionada by rememberSaveable { mutableStateOf(LocalDate.now().plusDays(2)) } // data placeholder
    var horaSelecionada by rememberSaveable { mutableStateOf(LocalTime.of(9, 0)) } // hora placeholder
    var mostrarModalData by rememberSaveable { mutableStateOf(false) }
    var mostrarModalHora by rememberSaveable { mutableStateOf(false) }
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
        // alterado pra remover o placeholder
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = dataSelecionada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                onValueChange = {},
                enabled = false,
                label = { Text("DATA", color = GoldPrimary, fontSize = 11.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = TextWhite,
                    disabledBorderColor = TextGray
                ),
                modifier = Modifier
                    .weight(1f)
                    .clickable { mostrarModalData = true }
            )
            OutlinedTextField(
                value = horaSelecionada.format(DateTimeFormatter.ofPattern("HH:mm")),
                onValueChange = {},
                enabled = false,
                label = { Text("HORÁRIO", color = GoldPrimary, fontSize = 11.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = TextWhite,
                    disabledBorderColor = TextGray
                ),
                modifier = Modifier
                    .weight(1f)
                    .clickable { mostrarModalHora = true }
            )
        }

        if (mostrarModalData) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = dataSelecionada.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
            )
            DatePickerDialog(
                onDismissRequest = { mostrarModalData = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            dataSelecionada = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                        }
                        mostrarModalData = false
                    }) { Text("Confirmar") }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarModalData = false }) { Text("Cancelar") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (mostrarModalHora) {
            val timePickerState = rememberTimePickerState(
                initialHour = horaSelecionada.hour,
                initialMinute = horaSelecionada.minute,
                is24Hour = true
            )
            AlertDialog(
                onDismissRequest = { mostrarModalHora = false },
                confirmButton = {
                    TextButton(onClick = {
                        horaSelecionada = LocalTime.of(timePickerState.hour, timePickerState.minute)
                        mostrarModalHora = false
                    }) { Text("Confirmar") }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarModalHora = false }) { Text("Cancelar") }
                },
                text = { TimePicker(state = timePickerState) }
            )
        }

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
                val criador = viewModel.user ?: return@Button
                val descFinal = buildString {
                    if (localCafe.isNotBlank()) append(" • $localCafe")
                    if (descricao.isNotBlank()) append(" — $descricao")
                }
                viewModel.add(
                    nome = nomeEncontro,
                    data = LocalDateTime.of(dataSelecionada, horaSelecionada), //removido o placeholder
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
