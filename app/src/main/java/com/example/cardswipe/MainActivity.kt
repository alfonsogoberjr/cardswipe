package com.example.cardswipe

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout.LayoutParams
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {
    private var windowWidth = 0
    private var screenCenter = 0
    private var xCord = 0
    private var yCord: Int = 0
    private var x: Int = 0
    private var y: Int = 0
    private var likes = 0
    private var parentView: RelativeLayout? = null
    private var alphaValue = 0f
    private var context: Context? = null

    private var userDataModelArrayList: ArrayList<DataModel?>? = null

    @SuppressLint("NewApi", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        context = this@MainActivity
        parentView = findViewById<RelativeLayout>(R.id.main_layoutview)
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        windowWidth = size.x
        screenCenter = windowWidth / 2
        userDataModelArrayList = ArrayList<DataModel?>()
        getArrayData()
        for (i in userDataModelArrayList!!.indices) {
            val inflate =
                (context as MainActivity).getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val containerView: View =
                inflate.inflate(R.layout.card_view, null)
            val userIMG =
                containerView.findViewById<View>(R.id.userIMG) as ImageView
            val relativeLayoutContainer =
                containerView.findViewById<View>(R.id.relative_container) as RelativeLayout
            val layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT
            )
            containerView.layoutParams = layoutParams
            containerView.tag = i
            userDataModelArrayList!![i]?.getPhoto()?.let { userIMG.setBackgroundResource(it) }

            val layoutTvParams = LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
            )

            val textViewY = TextView(context)
            textViewY.layoutParams = layoutTvParams
            textViewY.setPadding(10, 10, 10, 10)
            textViewY.background = resources.getDrawable(R.drawable.btnlikeback)
            textViewY.text = "Yes"
            textViewY.gravity = Gravity.CENTER
            textViewY.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            textViewY.textSize = 25f
            textViewY.setTextColor(ContextCompat.getColor(this.context as MainActivity, R.color.colorGreen))
            textViewY.x = 20f
            textViewY.y = 100f
            textViewY.rotation = -25f
            textViewY.alpha = this.alphaValue
            relativeLayoutContainer.addView(textViewY)

            val textViewN = TextView(context)
            textViewN.layoutParams = layoutTvParams
            textViewN.setPadding(10, 10, 10, 10)
            textViewN.background = resources.getDrawable(R.drawable.btnlikeback)
            textViewN.text = "No"
            textViewN.gravity = Gravity.CENTER
            textViewN.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            textViewN.textSize = 25f
            textViewN.setTextColor(ContextCompat.getColor(this.context as MainActivity, R.color.colorRed))
            textViewN.x = 800f
            textViewN.y = 100f
            textViewN.rotation = 25f
            textViewN.alpha = this.alphaValue
            relativeLayoutContainer.addView(textViewN)
            val tvName =
                containerView.findViewById<View>(R.id.tvName) as TextView
            val tvTotLikes =
                containerView.findViewById<View>(R.id.tvTotalLikes) as TextView
            tvName.text = userDataModelArrayList!![i]?.getName()
            tvTotLikes.text = userDataModelArrayList!![i]?.getTotalLikes()

            relativeLayoutContainer.setOnTouchListener { _, event ->
                xCord = event.rawX.toInt()
                yCord = event.rawY.toInt()
                containerView.x = 0f
                containerView.y = 0f
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x = event.x.toInt()
                        y = event.y.toInt()
                        Log.v("On touch", "$x $y")
                    }
                    MotionEvent.ACTION_MOVE -> {
                        xCord = event.rawX.toInt()
                        // smoother animation.
                        yCord = event.rawY.toInt()
                        containerView.x = xCord - x.toFloat()
                        containerView.y = yCord - y.toFloat()
                        if (xCord >= screenCenter) {
                            containerView.rotation =
                                ((xCord - screenCenter) * (Math.PI / 32)).toFloat()
                            if (xCord > screenCenter + screenCenter / 2) {
                                textViewY.alpha = 1f
                                likes = if (xCord > windowWidth - screenCenter / 4) {
                                    2
                                } else {
                                    0
                                }
                            } else {
                                likes = 0
                                textViewY.alpha = 0f
                            }
                            textViewN.alpha = 0f
                        } else {
                            // rotate image while moving
                            containerView.rotation =
                                ((xCord - screenCenter) * (Math.PI / 32)).toFloat()
                            if (xCord < screenCenter / 2) {
                                textViewN.alpha = 1f
                                likes = if (xCord < screenCenter / 4) {
                                    1
                                } else {
                                    0
                                }
                            } else {
                                likes = 0
                                textViewN.alpha = 0f
                            }
                            textViewY.alpha = 0f
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        xCord = event.rawX.toInt()
                        yCord = event.rawY.toInt()
                        Log.e("X Point", "$xCord , Y $yCord")
                        textViewN.alpha = 0f
                        textViewY.alpha = 0f
                        if (likes == 0) {
                            Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                            Log.e("Action: ", "Nothing")
                            containerView.x = 0f
                            containerView.y = 0f
                            containerView.rotation = 0f
                        } else if (likes == 1) {
                            Toast.makeText(context, "No", Toast.LENGTH_SHORT).show()
                            Log.e("Action: ", "No")
                            parentView!!.removeView(containerView)
                        } else if (likes == 2) {
                            Toast.makeText(context, "Yes", Toast.LENGTH_SHORT).show()
                            Log.e("Action: ", "Yes")
                            parentView!!.removeView(containerView)
                        }
                    }
                    else -> {
                    }
                }
                true
            }
            parentView!!.addView(containerView)
        }
    }

    private fun getArrayData() {
        val model = DataModel()
        model.setName("Card 1")
        model.setTotalLikes("3 M")
        model.setPhoto(R.drawable.image1)
        userDataModelArrayList!!.add(model)
        val model2 = DataModel()
        model2.setName("Card 2")
        model2.setTotalLikes("2 M")
        model2.setPhoto(R.drawable.image2)
        userDataModelArrayList!!.add(model2)
        val model3 = DataModel()
        model3.setName("Card 3")
        model3.setTotalLikes("3 M")
        model3.setPhoto(R.drawable.image3)
        userDataModelArrayList!!.add(model3)
        userDataModelArrayList!!.reverse()
    }
}