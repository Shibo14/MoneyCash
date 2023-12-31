package uz.abboskhan.cashuc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.abboskhan.cashuc.databinding.FragmentDayBonusBinding
import uz.abboskhan.cashuc.databinding.FragmentHomeBinding


class DayBonusFragment : Fragment() {
    private var _binding: FragmentDayBonusBinding? = null
    private val database = FirebaseDatabase.getInstance()
    private val bonusReference = database.getReference("bonus")
    private val coinsReference = database.getReference("User").child("coins")

    private var currentCoins = 0
    private var lastBonusTime = 0L
    private var isBonusAvailable = false
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDayBonusBinding.inflate(inflater, container, false)

        getBonusDay()

        return binding!!.root
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

        binding?.dayBtn?.setOnClickListener {
            if (isBonusAvailable) {
                CoroutineScope(IO).launch {
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
            binding?.dayBtn?.isEnabled = true
        binding?.dayBtn?.text = "Claim Bonus"
           // bonusTimeTextView.text = "Bonus is available now"
        } else {
            binding?.dayBtn?.isEnabled = false
            val timeSinceLastBonus = System.currentTimeMillis() - lastBonusTime
            val remainingTime = 86400000 - timeSinceLastBonus
            val remainingTimeString = String.format("%d:%d:%d", remainingTime / 3600000, remainingTime / 60000 % 60, remainingTime / 1000 % 60)
            binding?.dayBtn?.text = "Bonus Available in: $remainingTimeString"
           // bonusTimeTextView.text = "Bonus will be available in: $remainingTimeString"
        }
    }
}



