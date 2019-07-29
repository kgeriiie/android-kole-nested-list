package hu.kole.nestedlist

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import hu.kole.nestedlist.adapter.SectionAdapter
import hu.kole.nestedlist.databinding.ActivityMainBinding
import hu.kole.nestedlist.repo.SectionRepository

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.recyclerView.adapter = SectionAdapter().apply {
            submitList(SectionRepository().getSections())
        }
    }
}
