package net.rmitsolutions.eskool.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Madhu on 15-Jul-2017.
 */
class DownloadParcelable() : Parcelable {
    var progress: Int = 0
    var currentFileSize: Float = 0f
    var totalFileSize: Float = 0f

    constructor(parcel: Parcel) : this() {
        progress = parcel.readInt()
        currentFileSize = parcel.readFloat()
        totalFileSize = parcel.readFloat()
    }

    companion object CREATOR : Parcelable.Creator<DownloadParcelable> {
        override fun createFromParcel(parcel: Parcel): DownloadParcelable {
            return DownloadParcelable(parcel)
        }

        override fun newArray(size: Int): Array<DownloadParcelable?> {
            return arrayOfNulls(size)
        }
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(progress);
        dest?.writeFloat(currentFileSize);
        dest?.writeFloat(totalFileSize);
    }

    override fun describeContents(): Int = 0
}