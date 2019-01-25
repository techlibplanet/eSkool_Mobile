package net.rmitsolutions.eskool.dependency.components

import net.rmitsolutions.eskool.dependency.scopes.ActivityScope
import net.rmitsolutions.eskool.filedownload.FileDownloadService
import dagger.Component

/**
 * Created by Madhu on 15-Jul-2017.
 */
@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface ServiceComponent {
    fun injectFileDownloadService(service: FileDownloadService)
}