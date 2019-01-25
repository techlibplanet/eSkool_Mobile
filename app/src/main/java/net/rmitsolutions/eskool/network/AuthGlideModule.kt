package net.rmitsolutions.eskool.network

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import net.rmitsolutions.eskool.ESkoolApplication
import net.rmitsolutions.eskool.dependency.components.DaggerBaseActivityComponent
import okhttp3.OkHttpClient
import java.io.InputStream
import javax.inject.Inject


/**
 * Created by Madhu on 21-Sep-17.
 */
@GlideModule
class AuthGlideModule() : AppGlideModule() {
    @Inject
    lateinit var httpClient: OkHttpClient

    init {
        val depComponent = DaggerBaseActivityComponent.builder()
                .applicationComponent(ESkoolApplication.applicationComponent)
                .build()
        depComponent.injectGlideModule(this)
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        //nothing to do here
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val factory = OkHttpUrlLoader.Factory(httpClient)
        registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }
}