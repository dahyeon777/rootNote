package com.dada.rootnote

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.dada.rootnote.AppDatabase
import com.dada.rootnote.Memo
import com.dada.rootnote.R
import com.dada.rootnote.databinding.ActivityWriteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class WriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWriteBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_write)

        binding.saveBtn.setOnClickListener {
            saveMemoToDatabase()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveMemoToDatabase() {

        val localDateTime: LocalDateTime = LocalDateTime.now()

        val localDate: LocalDate = LocalDate.now()
        val titleText = binding.titleTextView.text.toString()
        val contentText = binding.contentTextView.text.toString()
        val dateText = localDate.toString()

        // Memo 객체 생성
        val memo = Memo(title = titleText, content = contentText, date = dateText)

        // 데이터베이스에 Memo 객체 삽입
        val db = AppDatabase.getDatabase(application)
        val memoDao = db?.MemoDAO()

        if (memoDao != null) {
            GlobalScope.launch(Dispatchers.IO) {
                memoDao.insertMemos(memo)
            }
        } else {
            // memoDao가 null인 경우 처리
        }

        // 작업 완료 후 액티비티 종료
        finish()
    }
}
