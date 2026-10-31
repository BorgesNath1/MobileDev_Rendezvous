package com.borgesnath1

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.borgesnath1.ui.theme.RendezvousTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RendezvousTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginPage(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun LoginPage( modifier: Modifier = Modifier) {
    var email by rememberSaveable { mutableStateOf("")}
    var password by rememberSaveable { mutableStateOf("")}
    val activity = LocalActivity.current as Activity

    Column(
        modifier = modifier.padding(24.dp).fillMaxSize().fillMaxWidth(fraction = 0.9f),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_fundo_preto),
                contentDescription = "Logo Rendezvous",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Fit
            )
            Image(
                painter = painterResource(id = R.drawable.nome_sem_fundo),
                contentDescription = "Nome Rendezvous",
                modifier = Modifier.fillMaxWidth(0.7f).height(48.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Encontro entre amigos",
                fontSize = 18.sp
            )

        }
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                label = { Text(text = "Digite seu e-mail") },
                modifier = Modifier.fillMaxWidth(0.9f),
                onValueChange = { email = it }
            )
            OutlinedTextField(
                value = password,
                label = { Text(text = "Digite sua senha") },
                modifier = Modifier.fillMaxWidth(0.9f),
                onValueChange = { password = it },
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                onClick = {
                    activity.startActivity(
                        Intent(activity, MainActivity::class.java).setFlags(
                            FLAG_ACTIVITY_SINGLE_TOP
                        )
                    )
                    Toast.makeText(activity, "Login OK!", Toast.LENGTH_LONG).show()
                },
                enabled = email.isNotEmpty() && password.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(0.7f),
            ) {
                Text("Entrar")
            }
        }

        Row(
            modifier = Modifier.padding(24.dp).fillMaxWidth(fraction = 0.9f),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Button(
                onClick = { email = ""; password = "" },
                modifier = Modifier.weight(1f)
            ) {
                Text("Limpar")
            }
            Button(
                onClick = {
                    activity.startActivity(
                        Intent(activity, RegisterActivity::class.java).setFlags(
                            FLAG_ACTIVITY_SINGLE_TOP
                        )
                    )
                },
                enabled = true,
                modifier = Modifier.weight(1f)
            ) {
                Text("Registrar")
            }
        }
    }
}
