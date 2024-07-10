package com.xvesa.gpt2mobile

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream

class MainActivity : Activity(), SurfaceHolder.Callback {
    private val gpt2 = GPT2()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSend: Button
    private val messages = mutableListOf<Message>()

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        recyclerView = findViewById(R.id.recycler_view_chat)
        editTextMessage = findViewById(R.id.edit_text_message)
        buttonSend = findViewById(R.id.button_send)

        adapter = ChatAdapter(messages)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        buttonSend.setOnClickListener {
            val messageText = editTextMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val message = Message(messageText, true)
                messages.add(message)
                adapter.notifyItemInserted(messages.size - 1)
                recyclerView.scrollToPosition(messages.size - 1)
                editTextMessage.text.clear()

                val output = gpt2.chat(messageText)

                // Simulate a received message (for testing purposes)
                val receivedMessage = output?.let { it1 -> Message(it1, false) }!!
                messages.add(receivedMessage)
                adapter.notifyItemInserted(messages.size - 1)
                recyclerView.scrollToPosition(messages.size - 1)
            }
        }

        reload()
    }

    private fun reload() {
        val path = this@MainActivity.filesDir.absolutePath
        val name = "vocab.txt"
        copy(this@MainActivity, name, path, name)
        val file = path + File.separator + name

        val retInit = gpt2.loadGPT2(assets, file)
        if (!retInit) {
            Log.e("MainActivity", "loadModel failed")
        }
    }

    private fun copy(myContext: Context, s: String, savePath: String, name: String) {
        val filename = "$savePath/$name"
        val dir = File(savePath)
        if (!dir.exists()) dir.mkdir()
        try {
            if (!File(filename).exists()) {
                val inputStream = myContext.resources.assets.open(s)
                val fos = FileOutputStream(filename)
                val buffer = ByteArray(7168)
                var count: Int
                while ((inputStream.read(buffer).also { count = it }) > 0) {
                    fos.write(buffer, 0, count)
                }
                fos.close()
                inputStream.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }

    public override fun onResume() {
        super.onResume()
    }

    public override fun onPause() {
        super.onPause()
    }
}
