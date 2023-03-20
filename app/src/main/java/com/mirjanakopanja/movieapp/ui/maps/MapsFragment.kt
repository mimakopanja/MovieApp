package com.mirjanakopanja.movieapp.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.mirjanakopanja.movieapp.R
import com.mirjanakopanja.movieapp.databinding.FragmentSearchMapBinding
import com.mirjanakopanja.movieapp.extensions.BIRTHPLACE_BUNDLE
import kotlinx.android.synthetic.main.fragment_search_map.*
import kotlinx.coroutines.flow.combine
import java.io.IOException

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MapsFragment : Fragment() {

    private lateinit var map: GoogleMap
    private val markers: ArrayList<Marker> = ArrayList()
    private lateinit var binding: FragmentSearchMapBinding

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        }
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
        val initialPlace = LatLng(52.52, 13.40)
        googleMap.apply {
            addMarker(MarkerOptions().position(initialPlace).title("Default"))
            moveCamera(CameraUpdateFactory.newLatLng(initialPlace))

            setOnMapLongClickListener { latLng ->
                getAddressAsync(latLng)
                addMarkerToArray(latLng)
                drawLine()
            }
        }
    }

    private fun drawLine() {
        val last: Int = markers.size - 1
        if (last >= 1){
            val previous: LatLng = markers[last - 1].position
            val current: LatLng = markers[last].position
            map.addPolyline(
                PolylineOptions()
                    .add(previous,current)
                    .color(Color.RED)
                    .width(5f)
            )
        }
    }

    private fun addMarkerToArray(location: LatLng) {
        val marker = setMarker(location, markers.size.toString(), null)
        markers.add(marker)
    }

    private fun setMarker(location: LatLng, searchText: String, resourceId: Int?): Marker {
        return map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText))
    }

    private fun getAddressAsync(location: LatLng) {
        context?.let {
            val geoCoder = Geocoder(it)
            Thread{
                try {
                    val addresses = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
                    addressTextView.post{
                        addressTextView.text = addresses[0].getAddressLine(0) }
                    }catch (e:IOException){
                        e.printStackTrace()
                    }
                }.start()
            }
         }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.buttonSearch.setOnClickListener {
            val addressToSearch: String = binding.searchAddress.text.toString()
            initSearchByAddress(addressToSearch)
        }

        val birthPlace = arguments?.getString(BIRTHPLACE_BUNDLE)
        if (birthPlace != null && birthPlace.isNotEmpty()){
            initSearchByAddress(birthPlace)
        }
    }


    private fun initSearchByAddress(location: String) {
            val geoCoder = Geocoder(requireContext())
            Thread{
                try {
                    val addresses = geoCoder.getFromLocationName(location, 1)
                    if (addresses.isNotEmpty()){
                        goToAddress(addresses, view, location)
                    }
                }catch (e: IOException){
                    e.printStackTrace()
                }
            }.start()
    }

    private fun goToAddress(addresses: MutableList<Address>, view: View?, searchText: String) {
        val location = LatLng(addresses[0].latitude, addresses[0].longitude)
        view?.post {
            setMarker(location, searchText, R.drawable.ic_baseline_place_24)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
        }
    }

    companion object{
        fun newInstance(bundle: Bundle): MapsFragment {
            val fragment = MapsFragment()
            fragment.arguments = bundle
            return fragment
        }

        fun newEmptyInstance() = MapsFragment()
    }
}