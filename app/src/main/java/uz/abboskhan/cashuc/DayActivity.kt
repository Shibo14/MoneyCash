package uz.abboskhan.cashuc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.abboskhan.cashuc.databinding.ActivityDayBinding
import uz.abboskhan.cashuc.databinding.FragmentDayBonusBinding

class DayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDayBinding
    private val database = FirebaseDatabase.getInstance()
    private val bonusReference = database.getReference("bonus")
    private val coinsReference = database.getReference("User")

    private var currentCoins = 0
    private var lastBonusTime = 0L
    private var isBonusAvailable = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getBonusDay()

    }

    private fun getBonusDay() {


        coinsReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                currentCoins = (dataSnapshot.getValue(Long::class.java) ?: 0).toInt()
                updateButton()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        binding.dayBtn.setOnClickListener {
            if (isBonusAvailable) {
                CoroutineScope(Dispatchers.IO).launch {
                    val currentTime = System.currentTimeMillis()
                    val newBonus = currentCoins + 50
                    coinsReference.setValue(newBonus)

                    bonusReference.setValue(currentTime)

                    lastBonusTime = currentTime
                    isBonusAvailable = false

                    updateButton()
                    delay(86400000) // Wait 24 hours
                    isBonusAvailable = true
                }
            }
        }

        updateButton()


    }

    private fun updateButton() {
        if (isBonusAvailable) {
            binding.dayBtn.isEnabled = true
            binding.dayBtn.text = "Claim Bonus"
         binding.dayTv.text = "Bonus is available now"
        } else {
            binding?.dayBtn?.isEnabled = false
            val timeSinceLastBonus = System.currentTimeMillis() - lastBonusTime
            val remainingTime = 86400000 - timeSinceLastBonus
            val remainingTimeString = String.format(
                "%d:%d:%d",
                remainingTime / 3600000,
                remainingTime / 60000 % 60,
                remainingTime / 1000 % 60
            )
            binding?.dayBtn?.text = "Bonus Available in: $remainingTimeString"
          binding.dayTv.text = "Bonus will be available in: $remainingTimeString"
        }
    }
}