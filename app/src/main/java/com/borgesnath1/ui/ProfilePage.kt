package com.borgesnath1.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.borgesnath1.MainViewModel
import com.borgesnath1.R
import com.borgesnath1.ui.theme.GoldPrimary
import com.borgesnath1.ui.theme.StatusRefusedRed
import com.borgesnath1.ui.theme.TextGray

@Composable
fun ProfilePage(modifier : Modifier = Modifier.Companion,
             viewModel: MainViewModel
) {
    val usuario = viewModel.user
    val activity = LocalActivity.current as Activity

    var nome by rememberSaveable { mutableStateOf(usuario?.nome ?: "Ana Souza") }
    var sobre by rememberSaveable { mutableStateOf(usuario?.sobre ?: "Amante de cafés especiais!") }

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.9f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.pessoa),
                contentDescription = "Ícone pessoa",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, GoldPrimary, CircleShape),
                contentScale = ContentScale.Crop
            )

            Text(
                text = nome,
                fontSize = 24.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = sobre,
                fontSize = 14.sp,
                color = TextGray
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(0.9f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Seus dados Cadastrais:",
                fontSize = 18.sp,
                color = GoldPrimary,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = nome,
                label = { Text(text = "NOME COMPLETO") },
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { nome = it }
            )
            OutlinedTextField(
                value = sobre,
                label = { Text(text = "SOBRE VOCÊ") },
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { sobre = it }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        usuario?.nome = nome
                        usuario?.sobre = sobre
                        Toast.makeText(activity, "Perfil atualizado!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Salvar")
                }

                OutlinedButton(
                    onClick = {
                        Toast.makeText(activity, "Saindo da conta...", Toast.LENGTH_SHORT).show()
                        activity.finish()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusRefusedRed)
                ) {
                    Text("Sair (Logout)")
                }
            }
        }
    }
}
