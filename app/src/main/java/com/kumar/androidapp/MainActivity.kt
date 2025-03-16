import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumar.androidapp.R
import com.kumar.androidapp.ui.adapter.CountryAdapter
import com.kumar.androidapp.viewmodel.CountryViewModel



class MainActivity : AppCompatActivity() {

    private val viewModel: CountryViewModel by viewModels()
    private lateinit var adapter: CountryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.countriesRecyclerView)
        adapter = CountryAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        val loadingProgressBar = findViewById<ProgressBar>(R.id.loadingProgressBar)
        val errorTextView = findViewById<TextView>(R.id.errorTextView)

        viewModel.countries.observe(this) { countries ->
            adapter.submitList(countries)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { errorMessage ->
            if (errorMessage != null) {
                errorTextView.text = errorMessage
                errorTextView.visibility = View.VISIBLE
            } else {
                errorTextView.visibility = View.GONE
            }
        }
    }
}