package cat.copernic.pdiaza.recolifter.ui.flux

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import cat.copernic.pdiaza.recolifter.R
import cat.copernic.pdiaza.recolifter.databaseManager.FirebaseReadWrite
import cat.copernic.pdiaza.recolifter.databinding.FragmentUserProfileBinding
import cat.copernic.pdiaza.recolifter.models.DataUser
import cat.copernic.pdiaza.recolifter.utils.Util
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.regex.Pattern

/**
 * Clase encargada de vincular los datos del perfil de usuario con la parte visual
 */
class UserProfileFragment : Fragment() {

    private lateinit var firebaseCompadre: FirebaseReadWrite
    private var _binding: FragmentUserProfileBinding? = null
    private var dataUser: DataUser? = null
    private val MY_PERMISSIONS_REQUEST_CAMERA = 1
    private val binding get() = _binding!!
    private val MOBILE_PATTERN: Pattern = Pattern.compile(
        "^[67]{1}([0-9]{8,8})\$"
    )

    companion object {
        private const val TAG = "UserProfile"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        firebaseCompadre = FirebaseReadWrite.initialize()

        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        binding.dataUserProfileLayout.visibility = View.INVISIBLE
        binding.shimmerUserProfileLayout.startShimmer()

        getUserData()

        // Inflate the layout for this fragment
        return binding.root
    }

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) { //tracta les dades rebudes
                if (it.data != null) {
                    val photo = it.data!!.extras!!["data"] as Bitmap
                    val nubitmap = scaleImage(photo, 200f, 200f)
                    uploadToFirestore(nubitmap)
                    binding.profileImageView.setImageBitmap(nubitmap)
                } else {
                    Util.showSnackBar(
                        binding.profileImageView, getString(R.string.photo_not_possible)
                    )
                }
            } else {
                Util.showSnackBar(binding.root, getString(R.string.photo_canceled))
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).hideBottomNav()
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).showBottomNav()
    }

    /**
    Se encarga de manejar el evento onClick del botón "Editar perfil".
    Si el usuario hace clic en el botón, se activa o desactiva el modo de edición, permitiendo al usuario editar los campos de texto de su perfil.
    Si los campos de texto están vacíos o el número de teléfono es inválido, se muestra un mensaje de error.
    Si los campos de texto no están vacíos y el número de teléfono es válido, se guardan los datos del usuario.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var flag = 0

        binding.buttonEditProfile.setOnClickListener {
            if (flag == 0) {
                flag = 1
                editFields(flag)
                binding.buttonEditProfile.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(), R.drawable.ic_baseline_save_24
                    )
                )
            } else {
                if (binding.nameInputEditText.text.toString().isBlank() ||
                    binding.emailInputEditText.text.toString().isBlank()
                ) {
                    Util.showSnackBar(
                        view, R.string.email_username_check_isempty.toString()
                    )
                } else {
                    if (binding.mobileInputEditText.text.toString().isBlank()) {
                        flag = 0
                        editFields(flag)
                        setUserData()
                        binding.buttonEditProfile.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(), R.drawable.ic_pencil_edit_black_24
                            )
                        )
                    } else if (isMobileValid()) {
                        flag = 0
                        editFields(flag)
                        setUserData()
                        binding.buttonEditProfile.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(), R.drawable.ic_pencil_edit_black_24
                            )
                        )
                    } else {
                        Util.showSnackBar(
                            view, getString(R.string.phone_number_validation)
                        )
                    }
                }
            }
        }

        binding.birthdayInputEditText.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.select_birthday)).build()
            datePicker.show(parentFragmentManager, "tag")
            datePicker.addOnPositiveButtonClickListener {
                binding.birthdayInputEditText.setText("" + datePicker.headerText)
            }
        }

        binding.profileImageView.setOnClickListener {
            checkCameraPermission()
        }
    }

    /**
     * Hace un escalado de la imágen, recibe la imágen, la anchura y la altura.
     *
     * @param mBitmap
     * @param newWidth
     * @param newHeigth
     */
    private fun scaleImage(mBitmap: Bitmap, newWidth: Float, newHeigth: Float): Bitmap {
        //Redimensionem
        val width = mBitmap.width
        val height = mBitmap.height
        var scaleWidth = newWidth / width
        var scaleHeight = newHeigth / height

        //ens quedem amb l'escalat més petit per que no es deformi la imatge
        if (scaleWidth > scaleHeight) scaleWidth = scaleHeight
        else scaleHeight = scaleWidth

        // create a matrix for the manipulation
        val matrix = Matrix()
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight)
        // recreate the new Bitmap
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false)
    }

    /**
     * Sube una imagen Bitmap a Firebase Storage y actualiza la URL de la imagen en Firebase Firestore.
     *
     * @param mBitmap es el bitmap que sube
     */
    private fun uploadToFirestore(mBitmap: Bitmap) {
        val storageRef = FirebaseStorage.getInstance()
            .getReference("images/${UUID.randomUUID()}")
        val baos = ByteArrayOutputStream()
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = storageRef.putBytes(data)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
                val url = downloadUrl.toString()
                if (dataUser != null) {
                    dataUser!!.image = url
                } else {
                    dataUser = DataUser(image = url)
                }
                firebaseCompadre.setUserProfileData(dataUser!!)
            }
        }.addOnFailureListener {
            Log.d(TAG, "Upload task failed")
        }

        firebaseCompadre.setUserProfileData(dataUser!!)
    }

    /**
     * Valida si el número de teléfono ingresado cumple con el patrón establecido.
     * @return true si el número es válido, false en caso contrario.
     */
    private fun isMobileValid(): Boolean {
        val number: String = binding.mobileInputEditText.text.toString().trim()

        binding.mobileInputEditText.setText(number)

        return MOBILE_PATTERN.matcher(number).matches()
    }

    /**
     * Habilita o deshabilita los campos de edición del perfil.
     * @param flag 1 para habilitar, 0 para deshabilitar.
     */
    private fun editFields(flag: Int) {

        if (flag == 1) {
            binding.nameInputEditText.isEnabled = true
            binding.surnameInputEditText.isEnabled = true
            binding.birthdayInputEditText.isEnabled = true
            binding.mobileInputEditText.isEnabled = true
            binding.emailInputEditText.isEnabled = true
        } else {
            binding.nameInputEditText.isEnabled = false
            binding.surnameInputEditText.isEnabled = false
            binding.birthdayInputEditText.isEnabled = false
            binding.mobileInputEditText.isEnabled = false
            binding.emailInputEditText.isEnabled = false
        }
    }

    /**
     * Guarda los datos del usuario en la base de datos.
     */
    private fun setUserData() {

        val firstName = binding.nameInputEditText.text.toString()
        val lastName = binding.surnameInputEditText.text.toString()
        val email = binding.emailInputEditText.text.toString()
        val dateOfBrith = binding.birthdayInputEditText.text.toString()
        val phoneNumber = binding.mobileInputEditText.text.toString()
        val sex = "Helicoptere Apache de Combate"

        if (dataUser != null) {

            dataUser!!.firstName = firstName
            dataUser!!.lastName = lastName
            dataUser!!.email = email
            dataUser!!.dateOfBrith = dateOfBrith
            dataUser!!.phoneNumber = phoneNumber
            dataUser!!.sex = sex

        } else {

            dataUser = DataUser(
                firstName = firstName,
                lastName = lastName,
                email = email,
                dateOfBrith = dateOfBrith,
                phoneNumber = phoneNumber,
                sex = sex
            )
        }

        firebaseCompadre.setUserProfileData(dataUser!!)
    }

    /**
    Obtiene los datos de perfil de un usuario desde Firebase y los almacena en un objeto de tipo DataUser.
    También utiliza el método changeShimmerToDataLayout() para cambiar el diseño de la interfaz de usuario.
     */
    private fun getUserData() {
        firebaseCompadre.getUserProfileData().addOnSuccessListener {
            dataUser = it.toObject<DataUser>()
            Log.d("dataUser", dataUser.toString())
            changeShimmerToDataLayout()
        }
    }

    /**
    Comprueba si el permiso de cámara ha sido concedido. Si no lo ha sido, solicita el permiso al usuario.
    Si el permiso ha sido concedido, llama al método openCamera() para abrir la cámara del dispositivo.
     */
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permiso no concedido, solicita permiso
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                MY_PERMISSIONS_REQUEST_CAMERA
            )
        } else {
            // Permiso concedido, abre la cámara
            openCamera()
        }
    }

    /**
     * Abre la cámara del dispositivo
     */
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        getResult.launch(cameraIntent)
    }

    /**
    Oculta el diseño de carga (shimmer) y muestra el diseño de datos de perfil del usuario.
    También establece los valores de los campos de texto con los datos del objeto DataUser y carga una imagen si esta esta disponible.
     */
    private fun changeShimmerToDataLayout() {

        binding.shimmerUserProfileLayout.stopShimmer()
        binding.shimmerUserProfileLayout.visibility = View.INVISIBLE
        binding.dataUserProfileLayout.visibility = View.VISIBLE

        binding.nameInputEditText.setText(dataUser?.firstName ?: "")
        binding.surnameInputEditText.setText(dataUser?.lastName ?: "")
        binding.birthdayInputEditText.setText(dataUser?.dateOfBrith ?: "")
        binding.mobileInputEditText.setText((dataUser?.phoneNumber ?: "").toString())
        binding.emailInputEditText.setText(dataUser?.email ?: "")
        binding.sexInputEditText.setText(dataUser?.sex ?: "")
        val imageURL = dataUser?.image

        if (imageURL != null) {
            Glide.with(this)
                .load(imageURL)
                .into(binding.profileImageView)
        }
    }
}