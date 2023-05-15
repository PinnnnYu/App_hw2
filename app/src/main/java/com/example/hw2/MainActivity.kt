package com.example.hw2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //OkHttp調用
        //Get api
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
                    println("-------${responseBody}")

                    val jsonObject = JSONObject(responseBody)
                    val dataArray = jsonObject.getJSONArray("data")

                    runOnUiThread {
                        val textView = findViewById<TextView>(R.id.textView4)
                        textView.text = ""
                        for (i in 0 until dataArray.length()){
                            val chatObject = dataArray.getJSONObject(i)
                            val no = chatObject.getString("no")
                            val timestamp = chatObject.getString("timestamp")
                            val date = chatObject.getString("date")
                            val title = chatObject.getString("title")
                            val content = chatObject.getString("content")
                            textView.append("${date} ${timestamp}\n")
                            textView.append("${title} ${content}\n")
                        }
                    }
                }
                else{
                    println("Request failed")
                    runOnUiThread {
                        findViewById<TextView>(R.id.textView4).text = "資料錯誤"
                    }
                }
            }
        })


        //Post api
        val send = findViewById<Button>(R.id.button)
        val date = findViewById<TextView>(R.id.editTextTextPersonName)
        val title = findViewById<TextView>(R.id.editTextTextPersonName2)
        val content = findViewById<TextView>(R.id.editTextTextPersonName3)


        send.setOnClickListener {
            val postDate = date.text.toString()
            println("postDate--------${postDate}")
            val postTitle = title.text.toString()
            println("postTitle-------${postTitle}")
            val postContent = content.text.toString()
            println("postContent-----${postContent}")

            val clientPost = OkHttpClient()
            val responseBody = FormBody.Builder()
                .add("date", "${postDate}")
                .add("title", "${postTitle}")
                .add("content", "${postContent}")
                .build()
            val requestPost = Request.Builder()
                .url("http://10.0.2.2/app/insert.php")
                .post(responseBody)
                .build()

            clientPost.newCall(requestPost).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string()
                    println("POST--------${responseBody}")
                }
            })
            val intent = Intent()
            intent.setClass(this, MainActivity2::class.java)
            startActivity(intent)



        }


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
                            println("-------${responseBody}")

                            val jsonObject = JSONObject(responseBody)
                            val dataArray = jsonObject.getJSONArray("data")

                            runOnUiThread {
                                val textView = findViewById<TextView>(R.id.textView4)
                                textView.text = ""
                                for (i in 0 until dataArray.length()){
                                    val chatObject = dataArray.getJSONObject(i)
                                    val no = chatObject.getString("no")
                                    val timestamp = chatObject.getString("timestamp")
                                    val date = chatObject.getString("date")
                                    val title = chatObject.getString("title")
                                    val content = chatObject.getString("content")
                                    textView.append("${date} ${timestamp}\n")
                                    textView.append("${title} ${content}\n")
                                }
                            }
                        }
                        else{
                            println("Request failed")
                            runOnUiThread {
                                findViewById<TextView>(R.id.textView4).text = "資料錯誤"
                            }
                        }
                    }
                })
            }
        }
        timer.schedule(timerTask, 0, 5000)


    }
}