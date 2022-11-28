package com.example.tictactoe

import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlin.system.exitProcess

var playerTurn= true
class GamePlayActivity : AppCompatActivity() {

    lateinit var player1TV: TextView
    lateinit var player2TV: TextView
    lateinit var box1Btn: Button
    lateinit var box2Btn: Button
    lateinit var box3Btn: Button
    lateinit var box4Btn: Button
    lateinit var box5Btn: Button
    lateinit var box6Btn: Button
    lateinit var box7Btn: Button
    lateinit var box8Btn: Button
    lateinit var box9Btn: Button
    lateinit var resetBtn: Button
    var player1count = 0
    var player2count = 0

    var player1 = ArrayList<Int>()
    var player2 = ArrayList<Int>()
    var emptycells = ArrayList<Int>()
    var activeUser = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)

        player1TV = findViewById(R.id.idTVPlayer1)
        player2TV = findViewById(R.id.idTVPlayer2)

        box1Btn = findViewById(R.id.idBtnBox1)
        box2Btn = findViewById(R.id.idBtnBox2)
        box3Btn = findViewById(R.id.idBtnBox3)
        box4Btn = findViewById(R.id.idBtnBox4)
        box5Btn = findViewById(R.id.idBtnBox5)
        box6Btn = findViewById(R.id.idBtnBox6)
        box7Btn = findViewById(R.id.idBtnBox7)
        box8Btn = findViewById(R.id.idBtnBox8)
        box9Btn = findViewById(R.id.idBtnBox9)
        resetBtn = findViewById(R.id.idBtnReset)

        resetBtn.setOnClickListener {
            reset()
        }
    }

    private fun reset() {
        player1.clear()
        player2.clear()
        emptycells.clear()
        activeUser = 1;

        for (i in 1..9) {
            var buttonSelected: Button?
            buttonSelected = when (i) {
                1 -> box1Btn
                2 -> box2Btn

                3 -> box3Btn
                4 -> box4Btn
                5 -> box5Btn
                6 -> box6Btn
                7 -> box7Btn
                8 -> box8Btn
                9 -> box1Btn
                else -> {
                    box1Btn
                }

            }
            buttonSelected.isEnabled = true
            buttonSelected.text = ""
            player1TV.text = "Player 1 : $player1count"
            player2TV.text = "player 2 : $player2count"
        }
    }

    fun buttonClick(view: View) {
        if (playerTurn) {
            val but = view as Button
            var cellID = 0

            when (but.id) {
                R.id.idBtnBox1 -> cellID = 1
                R.id.idBtnBox2 -> cellID = 2
                R.id.idBtnBox3 -> cellID = 3
                R.id.idBtnBox4 -> cellID = 4
                R.id.idBtnBox5 -> cellID = 5
                R.id.idBtnBox6 -> cellID = 6
                R.id.idBtnBox7 -> cellID = 7
                R.id.idBtnBox8 -> cellID = 8
                R.id.idBtnBox9 -> cellID = 9
            }
            playerTurn = false
            Handler().postDelayed({ playerTurn = true }, 600)
            playeNow(but, cellID)
        }
    }

    private fun playeNow(buttonSelected: Button, currCell: Int) {
        val audio = MediaPlayer.create(this, R.raw.udmclick)
        if (activeUser == 1) {
            buttonSelected.text = "X"
            buttonSelected.setTextColor(Color.parseColor("#EC0C0C"))
            player1.add(currCell)
            emptycells.add(currCell)
            audio.start()
            buttonSelected.isEnabled = false
            Handler().postDelayed({ audio.release() }, 200)
            val checkWinner = checkWinner()
            if (checkWinner == 1) {
                Handler().postDelayed({ reset() }, 2000)
            } else if (singleUser) {
                Handler().postDelayed({ robot() }, 500)
            } else {
                activeUser = 2
            }
        } else {
            buttonSelected.text = "O"
            audio.start()
            buttonSelected.setTextColor(Color.parseColor("D22BB8884"))
            activeUser = 1
            player2.add(currCell)

            emptycells.add(currCell)
            Handler().postDelayed({ audio.release() }, 200)
            buttonSelected.isEnabled = false
            val checkWinner = checkWinner()
            if (checkWinner == 1) {
                Handler().postDelayed({ reset() }, 4000)
            }
        }
    }

    private fun robot() {
        // This function automatically make a move at a random position.

            val rnd = (1..9).random()
            if(emptycells.contains(rnd))
                robot()
            else {
                val buttonselected : Button?
                buttonselected = when(rnd) {
                    1 -> box1Btn
                    2 -> box2Btn
                    3 -> box3Btn
                    4 -> box4Btn
                    5 -> box5Btn
                    6 -> box6Btn
                    7 -> box7Btn
                    8 -> box8Btn
                    9 -> box9Btn
                    else -> {box1Btn}
                }
                emptycells.add(rnd);
                // move audio
                val audio = MediaPlayer.create(this , R.raw.udmclick)
                audio.start()
                Handler().postDelayed(Runnable { audio.release() } , 500)
                buttonselected.text = "O"
                buttonselected.setTextColor(Color.parseColor("#D22BB804"))
                player2.add(rnd)
                buttonselected.isEnabled = false
                var checkWinner = checkWinner()
                if(checkWinner == 1)
                    Handler().postDelayed(Runnable { reset() } , 2000)
            }


    }

    private fun checkWinner(): Any {
        val audio = MediaPlayer.create(this, R.raw.udmin)

        if ((player1.contains(1) && player1.contains(2) && player1.contains(3)) ||
            (player1.contains(1) && player1.contains(4) && player1.contains(7)) ||
            (player1.contains(3) && player1.contains(6) && player1.contains(9)) ||
            (player1.contains(7) && player1.contains(8) && player1.contains(9)) ||
            (player1.contains(4) && player1.contains(5) && player1.contains(6)) ||
            (player1.contains(1) && player1.contains(5) && player1.contains(9)) ||
            (player1.contains(3) && player1.contains(5) && player1.contains(7)) ||
            (player1.contains(2) && player1.contains(5) && player1.contains(8))
        ) {
            player1count += 1
            buttonDisable()
            audio.start()
            disableReset()
            Handler().postDelayed({ audio.release() }, 4000)
            val build = AlertDialog.Builder(this)
            build.setTitle("Game Over")
            build.setMessage("Player 1 win do you wanna to play again")
            build.setPositiveButton("ok") { dialog, which ->
                reset()
                audio.release()

            }
            build.setNegativeButton("Exit") { dialog, which ->
                audio.release()
                exitProcess(1)

            }
            Handler().postDelayed({ build.show() }, 2000)

            return 1
        } else if ((player2.contains(1) && player2.contains(2) && player2.contains(3)) ||
            (player2.contains(1) && player2.contains(4) && player2.contains(7)) ||
            (player2.contains(3) && player2.contains(6) && player2.contains(9)) ||
            (player2.contains(7) && player2.contains(8) && player2.contains(9)) ||
            (player2.contains(4) && player2.contains(5) && player2.contains(6)) ||
            (player2.contains(1) && player2.contains(5) && player2.contains(9)) ||
            (player2.contains(3) && player2.contains(5) && player2.contains(7)) ||
            (player2.contains(2) && player2.contains(5) && player2.contains(8))
        ) {
            player2count += 1

            buttonDisable()
            audio.start()
            disableReset()
            Handler().postDelayed({ audio.release() }, 4000)
            val build = AlertDialog.Builder(this)
            build.setTitle("Game Over")
            build.setMessage("Player 2 win do you wanna to play again")
            build.setPositiveButton("ok") { dialog, which ->
                reset()
                audio.release()

            }
            build.setNegativeButton("Exit") { dialog, which ->
                audio.release()
                exitProcess(1)

            }
            Handler().postDelayed({ build.show() }, 2000)

            return 1
        } else if (emptycells.contains(1) && emptycells.contains(2) && emptycells.contains(3) && emptycells.contains(
                4
            ) && emptycells.contains(5) && emptycells.contains(6) && emptycells.contains(7) && emptycells.contains(
                8
            ) && emptycells.contains(9)
        ) {
            val build = AlertDialog.Builder(this)
            build.setTitle("Game Over")
            build.setMessage(" game draw")
            build.setPositiveButton("ok") { dialog, which ->
                reset()


            }
            build.setNegativeButton("Exit") { dialog, which ->

                exitProcess(1)

            }
            build.show()

            return 1
        }



        return 0
    }

    private fun disableReset() {
        resetBtn.isEnabled= false
        Handler().postDelayed( {resetBtn.isEnabled=true},2200)
    }

    private fun buttonDisable() {
        player1.clear()
        player2.clear()
        emptycells.clear()
        activeUser = 1



        for (i in 1..9) {
            var buttonSelected: Button?
            buttonSelected = when (i) {
                1 -> box1Btn
                2 -> box2Btn

                3 -> box3Btn
                4 -> box4Btn
                5 -> box5Btn
                6 -> box6Btn
                7 -> box7Btn
                8 -> box8Btn
                9 -> box9Btn
                else -> {
                    box1Btn
                }

            }
            buttonSelected.isEnabled= true
            buttonSelected.text= ""
            player1TV.text="Player 1:$player1count"
            player2TV.text= "Player 2: $player2count"
        }


    }}