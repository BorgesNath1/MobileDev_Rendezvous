package com.borgesnath1

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.borgesnath1.ui.theme.GoldPrimary
import com.borgesnath1.ui.theme.RendezvousTheme
import com.borgesnath1.ui.theme.TextGray

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RendezvousTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterPage(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun RegisterPage(modifier: Modifier = Modifier) {
    var nome by rememberSaveable { mutableStateOf("")}
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var repeatpassword by rememberSaveable { mutableStateOf("") }
    var aceitouTermos by rememberSaveable { mutableStateOf(true) }
    val activity = LocalActivity.current as Activity

    Column(
        modifier = modifier.padding(24.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Column(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Criar Conta",
                fontSize = 28.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Vamos tomar um café?",
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                color = GoldPrimary
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = nome,
                label = { Text(text = "NOME COMPLETO") },
                placeholder = { Text(text = "Seu nome", color = TextGray) },
                modifier = Modifier.fillMaxWidth(0.9f),
                onValueChange = { nome = it }
            )
            OutlinedTextField(
                value = email,
                label = { Text(text = "E-MAIL") },
                placeholder = { Text(text = "seu@email.com", color = TextGray) },
                modifier = Modifier.fillMaxWidth(0.9f),
                onValueChange = { email = it }
            )
            OutlinedTextField(
                value = password,
                label = { Text(text = "SENHA") },
                placeholder = { Text(text = "Mín. 8 caracteres", color = TextGray) },
                modifier = Modifier.fillMaxWidth(0.9f),
                onValueChange = { password = it },
                visualTransformation = PasswordVisualTransformation()
            )
            OutlinedTextField(
                value = repeatpassword,
                label = { Text(text = "CONFIRMAR SENHA") },
                placeholder = { Text(text = "Repita a senha", color = TextGray) },
                modifier = Modifier.fillMaxWidth(0.9f),
                onValueChange = { repeatpassword = it },
                visualTransformation = PasswordVisualTransformation()
            )

            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = aceitouTermos,
                    onCheckedChange = { aceitouTermos = it }
                )
                Text(
                    text = "Aceito os Termos de Uso e a Política de Privacidade",
                    fontSize = 12.sp,
                    color = GoldPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                Toast.makeText(activity, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show()
                activity.finish()
            },
            enabled = nome.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && password == repeatpassword && aceitouTermos,
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Text("Criar Conta")
        }

        Row(
            modifier = Modifier.padding(top = 12.dp).fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ){
            Button(
                onClick = {
                    nome = ""; email = ""; password = ""; repeatpassword = ""
                },
                enabled = nome.isNotEmpty() || email.isNotEmpty() || password.isNotEmpty(),
                modifier = Modifier.weight(1f)
            ) {
                Text("Limpar")
            }

            Button(
                onClick = {
                    activity.finish()
                },
                enabled = true,
                modifier = Modifier.weight(1f)
            ) {
                Text("Voltar")
            }
        }
    }
}
