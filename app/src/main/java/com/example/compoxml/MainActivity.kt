package com.example.compoxml

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.compoxml.ui.ExampleScreen
import com.example.compoxml.ui.theme.CompoxmlTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContent {
            CompoxmlTheme {
                ExampleScreen()
            }
        }
    }
}

