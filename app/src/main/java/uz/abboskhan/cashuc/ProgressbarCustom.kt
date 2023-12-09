package uz.abboskhan.cashuc

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater

class ProgressbarCustom
   (var context: Context) {
        private val prgDialog = AlertDialog.Builder(context)

        fun startDialog(){
            val prgDialogView= LayoutInflater.from(context).inflate(R.layout.progressbar_dialog, null)
            prgDialog.setView(prgDialogView)
            prgDialog.setCancelable(false)

            val dialog = prgDialog.create()
            dialog.show()
        }
        fun dismissProgressBar(){
            val dialog = prgDialog.create()
            dialog.dismiss()
        }

    }
