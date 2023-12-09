package uz.abboskhan.cashuc

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User
import uz.abboskhan.cashuc.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private val database = FirebaseDatabase.getInstance().getReference("User")
    val firebaseAuth = FirebaseAuth.getInstance()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        getClick()


        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("User")
        val coinsReference = reference.child("coins")

        coinsReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // dataSnapshot orqali ma'lumotlarga kirishingiz mumkin
                val coins = dataSnapshot.getValue(Int::class.java)
                binding?.coins?.text = "$coins"
            }

            override fun onCancelled(error: DatabaseError) {
                // Xatolik yuz berdi
                println("Xatolik: ${error.message}")
            }
        })

        return binding!!.root
    }

    private fun userData() {


    }

    private fun getClick() {
        binding?.personImg?.setOnClickListener {

            firebaseAuth.signOut()
            startActivity(Intent(requireContext(), SignInActivity::class.java))
            activity?.finish()
        }

        binding?.dailyBonus?.setOnClickListener {
            startActivity(Intent(requireContext(), DayActivity::class.java))
        }
        binding?.timeBonus?.setOnClickListener {
            startActivity(Intent(requireContext(), HourBonusFragment::class.java))
        }
        binding?.spinBonus?.setOnClickListener {
            startActivity(Intent(requireContext(), SpinFragment::class.java))
        }
        binding?.scratchBonus?.setOnClickListener {
            startActivity(Intent(requireContext(), ScratchCardFragment::class.java))


        }
    }


}