package com.example.weding.activity


import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.weding.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_content.*
import java.io.IOException
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
        dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }
        date.setOnClickListener(this)
        tv_add_image.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.date -> {
                DatePickerDialog(
                    this@ContentActivity, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
             R.id.tv_add_image -> {
                 val pictureDialog = AlertDialog.Builder(this)
                 pictureDialog.setTitle("Select Action")
                 val pictureDialogItems = arrayOf("Select photo from Gallery","Capture photo from Camera")
                 pictureDialog.setItems(pictureDialogItems){
                         _, which ->
                     when(which){
                         0 -> choosePhotoFromGallery()
                             1 -> Toast.makeText(this@ContentActivity,
                         "Camera selection coming soon...",
                         Toast.LENGTH_SHORT).show()
                     }
                 }
                 pictureDialog.show()
             }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == GALLERY){
                if (data != null){
                    val contentURI = data.data
                    try {
                        val selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                        ImageView.setImageBitmap(selectedImageBitmap)
                    }catch (e: IOException){
                        e.printStackTrace()
                        Toast.makeText(this@ContentActivity, "Failed !", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

private fun choosePhotoFromGallery(){
    Dexter.withActivity(this).withPermissions(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    ).withListener(object: MultiplePermissionsListener{
        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                if (report!!.areAllPermissionsGranted()){
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, GALLERY)
                }

            }


        override fun onPermissionRationaleShouldBeShown(
            permissions: MutableList<PermissionRequest>?,
            token: PermissionToken?
        ) {
           showRationalDialogForPermission()
        }

    }).onSameThread().check()
}

    private fun showRationalDialogForPermission() {
        AlertDialog.Builder(this).setMessage("It looks like you have turned off permission required for this feature. I t can be enabled under Application Settings")
            .setPositiveButton("GO TO SETTINGS")
            {
                _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val  uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel"){
                    dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun updateDateInView() {
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        date.setText(sdf.format(cal.time).toString())
    }
    companion object{
        private const val GALLERY = 1
        private const val CAMERA_CODE = 2
    }
}



