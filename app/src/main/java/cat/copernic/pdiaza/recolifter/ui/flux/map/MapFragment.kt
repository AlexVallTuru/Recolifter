package cat.copernic.pdiaza.recolifter.ui.flux.map


import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.fragment.app.Fragment
import cat.copernic.pdiaza.recolifter.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment() {


    private val callback = OnMapReadyCallback { googleMap ->

        val terrassa = LatLng(41.564126, 2.009461)
        val bonAire1 = LatLng(-75.870934, 3.041624)
        val bonAire2 = LatLng(41.583023, 1.998730)
        val bonAire3 = LatLng(41.579842, 2.014872)
        val caNanglada1 = LatLng(41.5633726,2.025522)
        val caNanglada2 = LatLng(41.5601797,2.0291661)
        val caNanglada3 = LatLng(41.5635837,2.0268142)
        val canBoada1 = LatLng(41.5715705,1.9942062)
        val canBoada2 = LatLng(41.571538, 2.001995)
        val canBoada3 = LatLng(41.568167, 2.000214)
        val canPalet1 = LatLng(41.5547795,2.0184491)
        val canPalet2 = LatLng(41.5547795,2.0184491)
        val canPalet3 = LatLng(41.558312, 2.027762)
        val centre1 = LatLng(41.560320, 2.007677)
        val centre2 = LatLng(41.566509, 2.016131)
        val centre3 = LatLng(41.5574872,2.0084105)
        val deixalleria = LatLng(41.5413737,2.0180119)

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(terrassa))
//        val handler = Handler()
//        handler.postDelayed(Runnable { // Write whatever to want to do after delay specified
            googleMap.addMarker(MarkerOptions().position(terrassa ).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_basura)))

            googleMap.addMarker(MarkerOptions().position(bonAire1).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_egg)).title(getString(R.string.easterEgg)))

            googleMap.addMarker(MarkerOptions().position(bonAire2).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_basura)))
            googleMap.addMarker(MarkerOptions().position(caNanglada1).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_basura)))
            googleMap.addMarker(MarkerOptions().position(caNanglada2).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_basura)))
            googleMap.addMarker(MarkerOptions().position(canBoada1).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_basura)))
            googleMap.addMarker(MarkerOptions().position(canBoada2).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_basura)))
            googleMap.addMarker(MarkerOptions().position(canPalet1).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_basura)))
            googleMap.addMarker(MarkerOptions().position(canPalet2).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_basura)))
            googleMap.addMarker(MarkerOptions().position(centre1).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_basura)))
            googleMap.addMarker(MarkerOptions().position(centre2).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_basura)))

            googleMap.addMarker(MarkerOptions().position(centre3).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_camionbasura)).title(getString(R.string.camioMati)))
            googleMap.addMarker(MarkerOptions().position(canPalet3).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_camionbasura)).title(getString(R.string.camioTarda)))
            googleMap.addMarker(MarkerOptions().position(canBoada3).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_camionbasura)).title(getString(R.string.camioMati)))
            googleMap.addMarker(MarkerOptions().position(caNanglada3).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_camionbasura)).title(getString(R.string.camioTarda)))
            googleMap.addMarker(MarkerOptions().position(bonAire3).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_camionbasura)).title(getString(R.string.camioTarda)))

            googleMap.addMarker(MarkerOptions().position(deixalleria).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_recycler)).title(getString(R.string.deixalleria)))

//                                     }, 5000)
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(terrassa, 17f),
            4000,
            null
        )



    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Este comando el encargado de generar el mapa
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }

}