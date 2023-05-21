package com.example.hw2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridView
import android.widget.SimpleAdapter
import android.widget.TextView
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        //OkHttp調用
        //Post api
        val client = OkHttpClient()
        val urlBuilder = "http://10.0.2.2/app/read.php".toHttpUrlOrNull()
            ?.newBuilder()
            ?.addQueryParameter("title", "st")
            ?.addQueryParameter("limit", "20")
        val url = urlBuilder?.build().toString()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    val responseBody = response.body?.string()
                    //println("-------${responseBody}")

                    val jsonObject = JSONObject(responseBody)
                    val dataArray = jsonObject.getJSONArray("data")


                    // 將以 Read api 讀取到的資料用到 gridView 的方式排列，使其更像備忘錄
                    // 並設定當 gridView 按下時，可跳轉畫面至 MainActivity (代表編輯頁面)
                    // 找到欄位序列，將資料以 Hashmap 的形式傳送，再到 MainActivity 做處理資料
                    runOnUiThread {
                        val gridView = findViewById<GridView>(R.id.gridView)
                        val grid = ArrayList<HashMap<String, Any>>()
                        val list: MutableList<String> = ArrayList()
                        for (i in 0 until dataArray.length()){
                            val chatObject = dataArray.getJSONObject(i)
                            val no = chatObject.getString("no")
                            val timestamp = chatObject.getString("timestamp")
                            val date = chatObject.getString("date")
                            val title = chatObject.getString("title")
                            val content = chatObject.getString("content")
                            list.add("${date}\n ${title} ${content}\n")
                        }
                        for (i in list.indices){
                            val map = HashMap<String, Any>()
                            map["title"] = list[i]
                            grid.add(map)
                        }
                        val fromData = arrayOf("title")
                        val toData = intArrayOf(R.id.textView6)
                        val simpleAdapter = SimpleAdapter(this@MainActivity2, grid, R.layout.grid_item, fromData, toData)
                        gridView.adapter = simpleAdapter
                        gridView.setOnItemClickListener { adapterView, view, i, l ->
                            val intent = Intent()
                            intent.setClass(this@MainActivity2, MainActivity::class.java)
                            intent.putExtra("date",grid[i])
                            startActivity(intent)
                        }

                    }
                }
                else{
                    println("Request failed")
                    runOnUiThread {
                        findViewById<TextView>(R.id.textView6).text = "資料錯誤"
                    }
                }
            }
        })


        val timer = Timer()
        val timerTask = object : TimerTask(){
            override fun run(){
                val client = OkHttpClient()
                val urlBuilder = "http://10.0.2.2/app/read.php".toHttpUrlOrNull()
                    ?.newBuilder()
                    ?.addQueryParameter("title", "st")
                    ?.addQueryParameter("limit", "20")
                val request = Request.Builder()
                    .url(url)
                    .build()
                client.newCall(request).enqueue(object : Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }
                    override fun onResponse(call: Call, response: Response) {
                        if(response.isSuccessful){
                            val responseBody = response.body?.string()
                            //println("-------${responseBody}")

                            val jsonObject = JSONObject(responseBody)
                            val dataArray = jsonObject.getJSONArray("data")


                            runOnUiThread {
                                val gridView = findViewById<GridView>(R.id.gridView)
                                val grid = ArrayList<HashMap<String, Any>>()
                                val list: MutableList<String> = ArrayList()
                                for (i in 0 until dataArray.length()){
                                    val chatObject = dataArray.getJSONObject(i)
                                    val no = chatObject.getString("no")
                                    val timestamp = chatObject.getString("timestamp")
                                    val date = chatObject.getString("date")
                                    val title = chatObject.getString("title")
                                    val content = chatObject.getString("content")
                                    list.add("${date}\n ${title} ${content}\n")
                                }

                                for (i in list.indices){
                                    val map = HashMap<String, Any>()
                                    map["title"] = list[i]
                                    grid.add(map)
                                }
                                val fromData = arrayOf("title")

                                val toData = intArrayOf(R.id.textView6)
                                val simpleAdapter = SimpleAdapter(this@MainActivity2, grid, R.layout.grid_item, fromData, toData)
                                gridView.adapter = simpleAdapter
                                gridView.setOnItemClickListener { adapterView, view, i, l ->
                                    val intent = Intent()
                                    intent.setClass(this@MainActivity2, MainActivity::class.java)
                                    intent.putExtra("date",grid[i])
                                    startActivity(intent)
                                }
                            }
                        }
                        else{
                            println("Request failed")
                            runOnUiThread {
                                findViewById<TextView>(R.id.textView6).text = "資料錯誤"
                            }
                        }
                    }
                })
            }
        }
        timer.schedule(timerTask, 0, 5000)






    }
}