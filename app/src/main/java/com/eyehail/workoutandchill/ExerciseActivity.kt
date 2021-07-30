package com.eyehail.workoutandchill

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var textToSpeechIsInitialized = false


    private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        //findViewById<ConstraintLayout>(R.id.llstart)

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar_exercise_activity))
        /*val actionbar = this.supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }*/
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        findViewById<Toolbar>(R.id.toolbar_exercise_activity).setNavigationOnClickListener {
            //onBackPressed()
            customDialogForBackButton()
        }


        tts =  TextToSpeech(this, this)

        exerciseList = Constants.defaultExerciseList()
        setupRestView()


        setupExerciseStatusRecyclerView()

    }
    override fun onDestroy() {
        if(restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }

        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        if (player != null) {
            player!!.stop()
        }


        super.onDestroy()
    }


        private fun setRestProgressBar() {
            findViewById<ConstraintLayout>(R.id.llRestView).visibility = View.VISIBLE
            findViewById<TextView>(R.id.textview1).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tvUpcoming).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tvUpcomingExerciseName).visibility = View.VISIBLE

            findViewById<ConstraintLayout>(R.id.llExerciseView).visibility = View.GONE
            findViewById<TextView>(R.id.tvExerciseName).visibility = View.GONE
            findViewById<ImageView>(R.id.ivImage).visibility = View.GONE

            findViewById<ProgressBar>(R.id.progressBar).progress = restProgress
            restTimer = object : CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    restProgress++
                    findViewById<ProgressBar>(R.id.progressBar).progress = 10 - restProgress
                    findViewById<TextView>(R.id.tvTimer).text = (10 - restProgress).toString()
                }

                override fun onFinish() {
                   /* Toast.makeText(
                            this@ExerciseActivity,
                            "Here now we will start the exercise.",
                            Toast.LENGTH_SHORT
                    ).show()*/

                    currentExercisePosition++

                    exerciseList!![currentExercisePosition].setIsSelected(true)
                    exerciseAdapter!!.notifyDataSetChanged()

                    setupExerciseView()
                }
            }.start()
        }
    private fun setupRestView() {
        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }
        try {
            player = MediaPlayer.create(applicationContext, Uri.parse("android.resource://com.eyehail.workoutandchill/raw/press_start"))
            player!!.isLooping = false
            player!!.start()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        findViewById<TextView>(R.id.tvUpcomingExerciseName).text = exerciseList!![currentExercisePosition + 1].getName()

        setRestProgressBar()
    }

    private fun setExerciseProgressBar() {
        findViewById<ProgressBar>(R.id.progressBarExercise).progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                findViewById<ProgressBar>(R.id.progressBarExercise).progress = 60 - exerciseProgress
                findViewById<TextView>(R.id.tvTimerExerciseView).text = (60 - exerciseProgress).toString()
            }

            override fun onFinish() {
                if (currentExercisePosition < exerciseList?.size!! - 1) {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()

                } else {
                   /* Toast.makeText(
                            this@ExerciseActivity,
                            "Congratulations! You have completed the 7 minutes workout.",
                            Toast.LENGTH_SHORT
                    ).show()*/
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }


            }
        }.start()
    }

    private fun setupExerciseView() {
        findViewById<ConstraintLayout>(R.id.llRestView).visibility = View.GONE
        findViewById<TextView>(R.id.textview1).visibility = View.GONE
        findViewById<TextView>(R.id.tvUpcoming).visibility = View.GONE
        findViewById<TextView>(R.id.tvUpcomingExerciseName).visibility = View.GONE


        findViewById<ConstraintLayout>(R.id.llExerciseView).visibility = View.VISIBLE
        findViewById<TextView>(R.id.tvExerciseName).visibility = View.VISIBLE
        findViewById<ImageView>(R.id.ivImage).visibility = View.VISIBLE



        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        speakOut(exerciseList!![currentExercisePosition].getName())
        setExerciseProgressBar()
        findViewById<ImageView>(R.id.ivImage).setImageResource(exerciseList!![currentExercisePosition].getImage())
        //findViewById<ImageView>(R.id.ivImage).drawable as AnimatedVectorDrawable
        findViewById<TextView>(R.id.tvExerciseName).text = exerciseList!![currentExercisePosition].getName()

    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeechIsInitialized = true
            val result = tts!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            }
        } else {
            Log.e("TTS", "Initialization failed!")
        }
        }

   /* Careful - TextToSpeech.OnInitListener is Asynchronous
    10
    10 upvotes
    Mike · Lecture 147 · a year ago
    I ran into an interesting problem with the challenge to hook up TextToSpeech.
    I wanted to have the speech on the Rest timer, and say something like "Your next exercise is Jumping Jacks",
    which is a little different than what you did here.

    It all worked fine for me, except for the very first exercise (Jumping Jacks) would not speak.
    I discovered that the onInit callback happened very late -- after I had already started the Rest timer --
    therefore the TextToSpeech was not ready to use when I first needed it.

    I got around this by moving setupRestView() out of onCreate() and into the onInit() call back.
    This way the exercise sequence will not begin until TextToSpeech is ready.

    I think it worked for your project here because you are speaking in the Exercise mode
    (not the Rest mode) and that allows many seconds for the asynchronous initialization of TTS*/

    private fun speakOut(text: String) {
        if (textToSpeechIsInitialized) {
            tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        } else {
            Log.e("TTS", "textToSpeechIsInitialized not initialized")
        }
    }

    private fun setupExerciseStatusRecyclerView() {

        findViewById<RecyclerView>(R.id.rvExerciseStatus).layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false)// programmatically creating recyclerview
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!, this)
        findViewById<RecyclerView>(R.id.rvExerciseStatus).adapter = exerciseAdapter
    }

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_custom_back_confirmation)
        customDialog.findViewById<Button>(R.id.tvYes).setOnClickListener {
            finish()
            customDialog.dismiss()
        }
        customDialog.findViewById<Button>(R.id.tvNo).setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }


}
