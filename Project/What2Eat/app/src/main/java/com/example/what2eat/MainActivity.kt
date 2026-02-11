package com.example.what2eat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.what2eat.ui.theme.What2EatTheme

// 1. เพิ่มตัวแปร mood เข้าไปในโครงสร้างข้อมูลอาหาร
data class Food(val name: String, val meatType: String, val moods: List<String>)

val foodList = listOf(
    // อารมณ์ดี: กินอะไรที่สดใส มีความสุข หรือของอร่อยจัดเต็ม
    Food("สเต็กเนื้อชั้นดี", "เนื้อ", listOf("อารมณ์ดี")),
    Food("บิงซูผลไม้รวม", "อื่นๆ", listOf("อารมณ์ดี")),
    Food("กุ้งแม่น้ำเผา", "กุ้ง", listOf("อารมณ์ดี")),

    // อารมณ์เหงา: กินอะไรที่อุ่นๆ หรือกินง่ายๆ เป็นเพื่อน
    Food("ราเมงร้อนๆ", "หมู", listOf("อารมณ์เหงา")),
    Food("ชาบูหม้อเดี่ยว", "หมู", listOf("อารมณ์เหงา")),
    Food("กาแฟและแซนวิช", "อื่นๆ", listOf("อารมณ์เหงา")),

    // อารมณ์ไม่ดี (หงุดหงิด): ต้องกินของแซ่บๆ หรือของหวานตัดเลี่ยนแก้เครียด
    Food("ส้มตำปูปลาร้า", "อื่นๆ", listOf("อารมณ์ไม่ดี")),
    Food("หมึกผัดไข่เค็ม", "ปลาหมึก", listOf("อารมณ์ไม่ดี")),
    Food("ไก่ทอดซอสเผ็ด", "ไก่", listOf("อารมณ์ไม่ดี")),

    // อารมณ์เศร้า: กินอะไรที่ปลอบประโลมใจ (Comfort Food)
    Food("ข้าวต้มปลาร้อนๆ", "ปลา", listOf("อารมณ์เศร้า")),
    Food("ไอศกรีมช็อกโกแลต", "อื่นๆ", listOf("อารมณ์เศร้า")),
    Food("ซุปกิมจิ", "หมู", listOf("อารมณ์เศร้า")),

    // เมนูทั่วไปที่กินได้หลายอารมณ์
    Food("ข้าวมันไก่", "ไก่", listOf("อารมณ์ดี", "อารมณ์เหงา")),
    Food("กะเพราหมูสับ", "หมู", listOf("อารมณ์ไม่ดี", "อารมณ์ดี"))
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            What2EatTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    What2EatScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun What2EatScreen(modifier: Modifier = Modifier) {
    var resultFood by remember { mutableStateOf("อาหารที่ใช่สำหรับคุณ") }

    // เก็บอารมณ์ที่เลือก (เลือกได้ 1 อารมณ์)
    val moodOptions = listOf("อารมณ์ดี", "อารมณ์เหงา", "อารมณ์ไม่ดี", "อารมณ์เศร้า")
    var selectedMood by remember { mutableStateOf("อารมณ์ดี") }

    // เก็บวัตถุดิบที่เลือก
    val meatOptions = listOf("ไก่", "หมู", "เนื้อ", "กุ้ง", "ปลาหมึก", "ปลา")
    var selectedMeats by remember { mutableStateOf(meatOptions.toSet()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(text = "What2Eat", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(24.dp))

        // ส่วนเลือกอารมณ์ (Mood Selection)
        Text(text = "ตอนนี้คุณรู้สึกอย่างไร?", style = MaterialTheme.typography.titleMedium)

        // ใช้ Scrollable Row สำหรับเลือกอารมณ์
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            moodOptions.forEach { mood ->
                FilterChip(
                    selected = selectedMood == mood,
                    onClick = { selectedMood = mood },
                    label = { Text(mood) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ส่วนเลือกวัตถุดิบ
        Text(text = "อยากทานวัตถุดิบไหน?", style = MaterialTheme.typography.titleMedium)
        Box(modifier = Modifier.height(150.dp)) {
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(meatOptions) { meat ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedMeats.contains(meat),
                            onCheckedChange = { isChecked ->
                                selectedMeats = if (isChecked) selectedMeats + meat else selectedMeats - meat
                            }
                        )
                        Text(text = meat)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // กรองตามอารมณ์ที่เลือก และ วัตถุดิบที่เลือก
                val possibleFoods = foodList.filter { food ->
                    food.moods.contains(selectedMood) &&
                            (selectedMeats.contains(food.meatType) || food.meatType == "อื่นๆ")
                }

                resultFood = if (possibleFoods.isNotEmpty()) {
                    "เมนูสำหรับคน${selectedMood}: ${possibleFoods.random().name}"
                } else if (selectedMeats.isEmpty()) {
                    "กรุณาเลือกวัตถุดิบอย่างน้อย 1 อย่าง"
                } else {
                    "ขออภัย ไม่พบเมนูที่เข้ากับอารมณ์และวัตถุดิบนี้"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("สุ่มเมนูตามอารมณ์")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = resultFood,
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFF673AB7), // สีม่วงเข้มดูมีอารมณ์
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}