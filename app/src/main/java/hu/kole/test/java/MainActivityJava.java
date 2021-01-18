package hu.kole.test.java;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import hu.kole.test.R;
import hu.kole.test.databinding.ActivityMainBinding;
import hu.kole.test.java.adapter.SectionAdapter;
import hu.kole.test.repo.SectionRepository;

public class MainActivityJava extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private SectionRepository repository = new SectionRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        SectionAdapter adapter = new SectionAdapter();

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(adapter);

        adapter.submitList(repository.getSections());
    }
}