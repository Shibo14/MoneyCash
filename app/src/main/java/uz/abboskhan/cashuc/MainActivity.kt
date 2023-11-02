package uz.abboskhan.cashuc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import uz.abboskhan.cashuc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.menuBottomNaw.setOnItemSelectedListener { i ->
            when (i.itemId) {
                R.id.home -> {
                    menuButton(HomeFragment())
                    true
                }

                R.id.pay -> {
                    menuButton(PayFragment())
                    true
                }

                R.id.history -> {
                    menuButton(HistoryFragment())
                    true
                }

                R.id.person -> {
                    menuButton(PersonFragment())
                    true
                }

                else -> false

            }

        }
        menuButton(HomeFragment())

    }

    private fun menuButton(fragment: Fragment) {

        supportFragmentManager.beginTransaction().replace(R.id.frm_layout, fragment).commit()

    }
}