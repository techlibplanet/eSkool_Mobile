package net.rmitsolutions.eskool.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import kotlinx.android.synthetic.main.activity_about.*
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import net.rmitsolutions.eskool.BaseActivity
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.helpers.Constants
import java.util.*


/**
 * Created by Madhu on 04-Aug-2017.
 */
class AboutActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setAboutContent()
    }

    override fun getSelfNavDrawerItem() = R.id.nav_about

    private fun setAboutContent() {
        val version = Constants.getAppVersionName(this)
        val versionElement = Element()
        versionElement.title = "Version $version"

        val aboutPage = AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.reqelford)
                .setDescription(getString(R.string.app_description))
                .addItem(versionElement)
                .addGroup("Connect with us")
                .addEmail("info@reqelfordinternationalschool.com")
                .addWebsite("http://www.reqelfordinternationalschool.com/")
                .addItem(getBlogItem("http://www.reqelfordinternationalschool.com/blog", "Visit our blog"))
                .addFacebook("ReqelfordInternationalSchool")
                .addTwitter("ReqelfordIntl")
                .addYoutube("UCZvCmlR2gYi3P3Mc2IOrYnA")
                .addPlayStore("net.rmitsolutions.eskool")
                .addItem(getCopyRightsElement())
                .create()

        aboutApp.addView(aboutPage)
    }

    private fun getBlogItem(url: String, title: String): Element {
        var urlBlog = url
        if (!urlBlog.startsWith("http://") && !urlBlog.startsWith("https://")) {
            urlBlog = "http://" + urlBlog
        }
        val blogElement = Element()
        blogElement.title = title
        blogElement.iconDrawable = R.drawable.ic_blogger
        blogElement.iconTint = R.color.orange
        blogElement.value = urlBlog

        val uri = Uri.parse(urlBlog)
        val browserIntent = Intent(Intent.ACTION_VIEW, uri)

        blogElement.intent = browserIntent
        return blogElement
    }

    private fun getCopyRightsElement(): Element {
        val copyRightsElement = Element()
        val copyrights = getString(R.string.copy_right, Calendar.getInstance().get(Calendar.YEAR))
        copyRightsElement.title = copyrights
        copyRightsElement.gravity = Gravity.CENTER
        return copyRightsElement
    }
}