package io.garyforster.whatsthatkanji

import android.accessibilityservice.AccessibilityService
import android.graphics.PixelFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import org.atilika.kuromoji.Token
import org.atilika.kuromoji.Tokenizer
import se.fekete.furiganatextview.furiganaview.FuriganaTextView

private const val TAG = "TextAnalyzerService"

class TextAnalyzerService : AccessibilityService() {
    private val tokenizer = Tokenizer.builder().build()
    private var overlayView: View? = null
    private var wm: WindowManager? = null
    private var layoutParams: WindowManager.LayoutParams? = null
    private var overlayInView = false

    override fun onCreate() {
        super.onCreate()
        wm = applicationContext.getSystemService(WINDOW_SERVICE) as WindowManager
        val view: View = LayoutInflater.from(applicationContext).inflate(R.layout.overlay, null)
        view.setOnClickListener(View.OnClickListener { removeLayout() })
        Log.d("init", "here")
        overlayView = view
        layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
    }

    override fun onInterrupt() {}

    override fun onAccessibilityEvent(event: AccessibilityEvent) {

        Log.i("Files", filesDir.absolutePath)

        val tokens = getAllTokens(event.source)
        if (tokens.isEmpty()) {
            Log.d(TAG, "No tokens found")
            return
        }
        for (token in tokens) {
            if (token != null) {
                Log.i("Token", token.surfaceForm)
            }
        }

        if (!overlayInView) {
            addLayout()
        }

        val output: String = generateRuby(tokens)
        val furiganaTextView = overlayView?.findViewById<FuriganaTextView>(R.id.overlayText)
        furiganaTextView?.setFuriganaText(output)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (overlayView != null) {
            if (overlayInView) {
                wm?.removeView(overlayView)
            }
            overlayView = null
        }
    }

    private fun addLayout() {
        wm?.addView(overlayView, layoutParams)
        overlayInView = true;
    }

    private fun removeLayout() {
        wm?.removeView(overlayView)
        overlayInView = false;
    }

    private fun getAllTokens(
        node: AccessibilityNodeInfo?
    ): Array<Token?> {
        var tokens = arrayOfNulls<Token>(0)
        if (node === null) {
            return tokens
        }
        if (node.text !== null) {
            tokens += tokenizer.tokenize(node.text.toString()).toTypedArray()
        }
        if (node.childCount > 0) {
            for (i in 0 until node.childCount) {
                tokens += getAllTokens(node.getChild(i))
            }
        }
        return tokens
    }

    private fun generateRuby(tokens: Array<Token?>): String {
        return tokens
            .map {
                if (it === null) {
                    ""
                } else if(it.reading === null || it.reading === it.surfaceForm) {
                    it.surfaceForm
                } else {
                    "<ruby>${it.surfaceForm}<rt>${it.reading}</rt></ruby>"
                }

            }
            .joinToString("")

    }
}
