package com.xvesa.gpt2mobile

import android.content.res.AssetManager

class GPT2 {
    external fun loadGPT2(mgr: AssetManager?, vocab: String?): Boolean
    external fun chat(s: String?): String?

    companion object {
        init {
            System.loadLibrary("gpt2chat")
        }
    }
}
