package com.example.weding.activity


import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import com.example.weding.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_content.*
import java.text.SimpleDateFormat
import java.util.*

class ContentActivity : AppCompatActivity(), View.OnClickListener {
    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolBar.setNavigationOnClickListener {
            onBackPressed()
        }
        dateSetListener = DatePickerDialog.OnDateSetListener {
                view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }
        date.setOnClickListener(this)
        tv_add_image.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.date ->{
                DatePickerDialog(this@ContentActivity, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
            R.id.iv_image ->{
                val pictureDialog = AlertDialog.Builder(this)
                pictureDialog.setTitle("Select Action")
                val pictureDialogItems = arrayOf("Select photo from Gallery",
                                                 "Capture photo from Camera")
                pictureDialog.setItems(pictureDialogItems){
                    dialog, which ->
                    when(which){
                        0 -> choosePhotoFromGallery()
                        1 -> Toast.makeText(
                        this@ContentActivity,
                        "Camera selection",
                        Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                pictureDialog.show()
            }
        }
    }

    private fun choosePhotoFromGallery() {
        Dexter.withActivity(this).withPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object: MultiplePermissionsListener, PermissionListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report != null) {
                    if (report.areAllPermissionsGranted()){
                        Toast.makeText(this@ContentActivity,
                        "Storage READ/WRITE permission are granted", Toast.LENGTH_SHORT).show()
                    }

                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {

            }

            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                TODO("Not yet implemented")
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                TODO("Not yet implemented")
            }


            override fun onPermissionRationaleShouldBeShown(
                permission: PermissionRequest?,
                token: PermissionToken?
            ) {

                AlertDialog.Builder(this@ContentActivity).setMessage("It looks like you have turned off permission required for this feature. It can be enabled under the Application Setting"
                ).setPositiveButton("Go to SETTINGS")
                {_, _ ->
                    try {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException){
                        e.printStackTrace()
                    }

                }.setNegativeButton("Cancel"){ dialog, _ ->
                    dialog.dismiss()
                }.show()
            }
        }).check()

    }

    private fun updateDateInView(){
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        date.setText(sdf.format(cal.time).toString())
    }
}

private fun DexterBuilder.SinglePermissionListener.withListener(multiplePermissionsListener: MultiplePermissionsListener) {

}


