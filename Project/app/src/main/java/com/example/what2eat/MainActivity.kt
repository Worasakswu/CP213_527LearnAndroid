package com.example.what2eat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.what2eat.ui.theme.What2EatTheme

// 1. สร้างโครงสร้างข้อมูลอาหาร (ไว้นอก Class)
data class Food(val name: String, val price: Int)

val foodList = listOf(
    Food("ข้าวมันไก่", 50),
    Food("กะเพราไข่ดาว", 60),
    Food("สเต็กเนื้อ", 250),
    Food("ราเมง", 180),
    Food("มาม่าต้ม", 15)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            What2EatTheme {
                // 2. ใช้ Scaffold จัดการพื้นที่หน้าจอ
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    What2EatScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun What2EatScreen(modifier: Modifier = Modifier) {
    // 3. สร้างตัวแปรสำหรับจำค่า (State)
    var budgetText by remember { mutableStateOf("") }
    var resultFood by remember { mutableStateOf("วันนี้กินอะไรดี?") }

    // 4. วาง Layout แบบ Column
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "What2Eat", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(24.dp))

        // ช่องกรอกงบประมาณ
        TextField(
            value = budgetText,
            onValueChange = { budgetText = it },
            label = { Text("งบประมาณของคุณ (บาท)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ปุ่มกดสุ่มอาหาร
        Button(
            onClick = {
                val myBudget = budgetText.toIntOrNull() ?: 0
                val possibleFoods = foodList.filter { it.price <= myBudget }

                resultFood = if (possibleFoods.isNotEmpty()) {
                    "เมนูที่แนะนำ: ${possibleFoods.random().name}"
                } else {
                    "งบไม่พอสำหรับเมนูในระบบครับ!"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ช่วยคิดหน่อย!")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // แสดงผลลัพธ์
        Text(
            text = resultFood,
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF4CAF50) // สีเขียว
        )
    }
}