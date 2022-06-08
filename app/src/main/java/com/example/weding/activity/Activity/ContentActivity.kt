package com.example.weding.activity.Activity


import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.weding.R
import com.example.weding.activity.database.DatabaseHandler
import com.example.weding.activity.database.SqliteDatabase
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_content.*
import kotlinx.android.synthetic.main.event_list.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class ContentActivity : AppCompatActivity(), View.OnClickListener {
    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var dbHelper: DatabaseHandler
    private var saveImageToInternalStorage : Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }
        updateDateInView()
       // dbHelper = DatabaseHandler(this)
        date_et.setOnClickListener(this)
        tv_add_image.setOnClickListener(this)
        btn_save.setOnClickListener(this)

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
                             1 -> takePhotoFromCamera()
                     }
                 }
                 pictureDialog.show()
             }
            R.id.btn_save ->{
                when {
                    name_et.text.isNullOrEmpty() -> {
                        Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
                    }
                    couple_et.text.isNullOrEmpty() -> {
                        Toast.makeText(this, "Please enter your couple's name", Toast.LENGTH_SHORT).show()
                    }
                    et_address.text.isNullOrEmpty() -> {
                        Toast.makeText(this, "Please enter your Address", Toast.LENGTH_SHORT).show()
                    }
                    saveImageToInternalStorage == null ->{
                        Toast.makeText(this, "Please select an Image", Toast.LENGTH_SHORT).show()
                    }else ->{
                        val sqliteDatabase = SqliteDatabase(
                            0,
                           name_et.text.toString(),
                            couple_et.text.toString(),
                            date.text.toString(),
                            address.text.toString(),
                            ImageView.toString()
                        )
                   val dbHandler = DatabaseHandler(this)
                    val addwedding = dbHandler.addWedding(sqliteDatabase)
                    if (addwedding > 0){
                        setResult(Activity.RESULT_OK)
                        Toast.makeText(this, "Successfully insert Data", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == GALLERY){
                if (data != null){
                    val contentURI = data.data
                    try {
                        val selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                       saveImageToInternalStorage =  saveImageToInternalStorage(selectedImageBitmap)

                        ImageView.setImageBitmap(selectedImageBitmap)
                    }catch (e: IOException){
                        e.printStackTrace()
                        Toast.makeText(this@ContentActivity, "Failed !", Toast.LENGTH_SHORT).show()
                    }

                }
            }else if (requestCode == CAMERA_CODE){
                val photo : Bitmap = data!!.extras!!.get("data") as Bitmap
                saveImageToInternalStorage = saveImageToInternalStorage(photo)
                ImageView.setImageBitmap(photo)
            }
        }
    }

    private fun takePhotoFromCamera(){
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ).withListener(object: MultiplePermissionsListener{
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                if (report!!.areAllPermissionsGranted()){
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, CAMERA_CODE)
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
private fun choosePhotoFromGallery(){
    Dexter.withActivity(this).withPermissions(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ).withListener(object: MultiplePermissionsListener{
        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                if (report!!.areAllPermissionsGranted()){
                    val galleryintent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                    startActivityForResult(galleryintent, GALLERY)
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
        AlertDialog.Builder(this).setMessage("It looks like you have turned off permission required for this feature. It can be enabled under Application Settings")
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
    private fun saveImageToInternalStorage(bitmap: Bitmap):Uri{
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file,"${UUID.randomUUID()}.jpg")

        try {
          val stream : OutputStream = FileOutputStream(file)
          bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
          stream.flush()
          stream.close()
        }catch (e: IOException){
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }
    companion object{
        private const val GALLERY = 1
        private const val CAMERA_CODE = 2
        private const val IMAGE_DIRECTORY = "WedInImages"
    }
}



