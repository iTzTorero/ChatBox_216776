package valenzuela.juan.chatbox.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import valenzuela.juan.chatbox.R
import valenzuela.juan.chatbox.data.Message
import valenzuela.juan.chatbox.utils.BotResponse
import valenzuela.juan.chatbox.utils.Constans.OPEN_GOOGLE
import valenzuela.juan.chatbox.utils.Constans.OPEN_SEARCH
import valenzuela.juan.chatbox.utils.Constans.RECEIVE_ID
import valenzuela.juan.chatbox.utils.Constans.SEND_ID
import valenzuela.juan.chatbox.utils.Time


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var adapter: MessangingAdapter
    private val messagesList = mutableListOf<Message>()
    private val botList = listOf("Peter", "Francesca", "Luigi", "Igor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView()

        clickEvents()

        val random= (0..3).random()
        customBotMessage("Hello! Today you're speaking with ${botlist[random]}, how may i help you?")
    }

    private fun clickEvents(){
        btn_send.setOnClickListener {
            sendMessage()
        }
        et_message.setOnClickListener{
            GlobalScope.launch{
                delay(100)
                withContext(Dispatchers.Main){
                    rv_messages.scrollToPosition(adapter.getItemCount()-1)
                }

            }
        }
    }

    private fun recyclerView(){
        adapter = MessangingAdapter()
        rv_messages.adapter = adapter
        rv_messages.layoutManager = LinearLayoutManager(applicationContext)
    }
    override fun onStart(){
        super.onStart()
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main){
                rv_messages.scrollToPosition(adapter.getItemCount()-1)
            }
        }
    }
    private fun sendMessage(){
        val message = et_message.text.toString()
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty()){
            messagesList.add(Message(message, SEND_ID, timeStamp))
            et_message.setText("")

            adapter.insertMessage(Message(message, SEND_ID, timeStamp))
            rv_messages.scrollToPosition(adapter.getItemCount()-1)
            botResponse(message)
        }
    }
    private fun botResponse(message:String){
        val timeStamp = Time.timeStamp()
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){
                val response = BotResponse.basicResponses(message)

                messagesList.add(Message(response, RECEIVE_ID, timeStamp))

                adapter.insertMessage(Message(response,RECEIVE_ID,timeStamp))

                rv_messages.scrollToPosition(adapter.getItemCount()-1)

                when (response){
                    OPEN_GOOGLE-> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://wwww.google.com/")
                        startActivity(site)
                    }
                    OPEN_SEARCH->{
                        val site = Intent(Intent.ACTION_VIEW)
                        val seachTerm:String?= message.substringAfterLast("search")
                        site.data = Uri.parse("https://wwww.google.com/search?&q=$seachTerm")
                        startActivity(site)
                    }
                }
            }
        }
    }
    private fun customBotMessage(message:String){

        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){
                val timeStamp = Time.timeStamp()
                messagesList.add(Message(message, RECEIVE_ID,timeStamp))
                adapter.inserMessage(Message(message, RECEIVE_ID, timeStamp))

                rv_messages.scrollToPosition(adapter.getItemCount()-1)
            }
        }
    }

}