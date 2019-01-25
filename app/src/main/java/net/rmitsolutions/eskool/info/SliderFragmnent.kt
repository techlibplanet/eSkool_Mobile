package net.rmitsolutions.eskool


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.eskool.models.ArrList
import org.jetbrains.anko.find



class SliderFragmnent : BaseFragment() {

    lateinit var overviewText:TextView
    lateinit var overviewImage:ImageView
    private var mCompositeDisposable: CompositeDisposable? = null


    var data: ArrList?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater!!.inflate(R.layout.slider_layout, container, false)

        overviewText=rootView.find<TextView>(R.id.ovewview_text)
        overviewImage=rootView.find<ImageView>(R.id.over_image)
        mCompositeDisposable = CompositeDisposable()
        handleResponse(data)
        return rootView
    }




    fun handleResponse(response: ArrList?) {

        if(response==null){
            return;
        }
        var des:String=response.description
        var thumb:String=response.image
        overviewText!!.setText(des)
        Glide.with(this).load(thumb).into(overviewImage)


    }


 fun setData1(data: ArrList){
        this.data=data;
    }
}