package net.rmitsolutions.eskool.contact

import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_contact.*
import net.rmitsolutions.eskool.BaseActivity
import net.rmitsolutions.eskool.ESkoolApplication
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.database.eSkoolDatabase
import net.rmitsolutions.eskool.databinding.ContactDataBinding
import net.rmitsolutions.eskool.dependency.components.DaggerBaseActivityComponent
import net.rmitsolutions.eskool.helpers.*
import net.rmitsolutions.eskool.models.SchoolAddress
import net.rmitsolutions.eskool.network.IMasterData
import org.jetbrains.anko.toast
import javax.inject.Inject


/**
 * Created by Madhu on 22-Jul-2017.
 */
class ContactActivity : BaseActivity(), OnMapReadyCallback {

    @Inject lateinit var masterDataService: IMasterData
    @Inject lateinit var database: eSkoolDatabase
    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    private lateinit var dataBinding: ContactDataBinding
    private var model: SchoolAddress? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_contact)
        window.reenterTransition = null

        collapsingToolbarLayout =findViewById(R.id.collapsing_toolbar)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val depComponent = DaggerBaseActivityComponent.builder()
                .applicationComponent(ESkoolApplication.applicationComponent)
                .build()

        depComponent.injectContactActivity(this)
    }


    override fun onMapReady(map: GoogleMap) {

        val latLng = LatLng(17.479028, 78.634344)

        val melbourne = map.addMarker(MarkerOptions()
                .position(latLng)
                .title("Reqelford International School"))
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 22f)
        map.animateCamera(cameraUpdate)
        melbourne.showInfoWindow()
        map.setOnMapClickListener {
            melbourne.showInfoWindow()
        }
        //map.setMapType(GoogleMap.MAP_TYPE_TERRAIN)
        showProgress()

    }


    override fun getSelfNavDrawerItem() = R.id.nav_contact

    override fun onStart() {
        super.onStart()
        getAddressFromLocalDb()
    }

    private fun getSchoolAddress() {
        showProgress()
        if (!internetCheck()) return
        compositeDisposable.add(masterDataService.getSchoolAddress(apiAccessToken)
                .processRequest(
                        { address ->
                            hideProgress()
                            model = address
                            dataBinding.contactVM = address
                            contactContainer.visibility = View.VISIBLE
                            //insert into db
                            insertIntoLocalDb(address)
                        },
                        { err ->
                            contactContainer.visibility = View.INVISIBLE
                            hideProgress(true, err)
                        }
                )

        )
    }

    fun onCallPhoneClick(view: View) {
        if (model == null) return
        val number = when (view.id) {
            R.id.contactPhone1 -> model!!.phone1
            R.id.contactPhone2 -> model!!.phone2
            R.id.contactMobile1 -> model!!.mobile1
            R.id.contactMobile2 -> model!!.mobile2
            else -> ""
        }
        callToNumber(number)
    }

    private fun internetCheck(): Boolean {
        if (!isNetConnected(false)) {
            hideProgress(true, "You are Offline")

            return false
        }

        return true
    }


    private fun callToNumber(number: String) {
        if (number.isEmpty()) return

        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + number)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            toast("No app found to call")
        }
    }

    fun onEmailLinkClick(view: View) {
        if (model == null) return
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:${model!!.emailId}")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            toast("No email app found")
        }
    }

    fun onWebsiteLinkClick(view: View) {
        if (model == null) return
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(model!!.webSite))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            toast("No browser found to open the url")
        }
    }

    private fun insertIntoLocalDb(address: SchoolAddress) {
        compositeDisposable.add(Single.fromCallable {
            address.id = Constants.studentModel!!.schoolId
            database.schoolDao().insertSchoolAddress(address)
        }.processRequest())
    }

    private fun getAddressFromLocalDb() {
        showProgress()
        compositeDisposable.add(
                Single.fromCallable {
                    database.schoolDao().getSchoolAddress(Constants.studentModel!!.schoolId)
                }.processRequest(
                        { address ->
                            hideProgress()
                            if (address == null) getSchoolAddress()
                            else {
                                model = address
                                dataBinding.contactVM = address
                                contactContainer.visibility = View.VISIBLE
                            }
                        },
                        {
                            getSchoolAddress()
                        }
                )
        )
    }
}