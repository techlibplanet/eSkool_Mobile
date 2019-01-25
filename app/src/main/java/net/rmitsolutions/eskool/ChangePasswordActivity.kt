package net.rmitsolutions.eskool


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_change_password.*
import net.rmitsolutions.eskool.databinding.ChangePasswordDataBinding
import net.rmitsolutions.eskool.dependency.components.DaggerBaseActivityComponent
import net.rmitsolutions.eskool.helpers.*
import net.rmitsolutions.eskool.models.ChangePasswordModel
import net.rmitsolutions.eskool.network.IChangePassword
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import javax.inject.Inject


class ChangePasswordActivity : AppCompatActivity() {

   var compositeDisposable: CompositeDisposable? = null

    @Inject lateinit var changePasswordService: IChangePassword

    lateinit var changePasswordModel: ChangePasswordModel
    lateinit var dataBinding: ChangePasswordDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)
        changePasswordModel= ChangePasswordModel("","","")
        dataBinding.changePasswordVM=changePasswordModel
        compositeDisposable = CompositeDisposable()
        val depComponent = DaggerBaseActivityComponent.builder()
                .applicationComponent(ESkoolApplication.applicationComponent)
                .build()
        depComponent.injectChangePasswordActivity(this)


    }
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.dispose()
    }
    fun onSubmitClick(view: View) {
        changePassword()
    }
    private fun changePassword() {
        if (!validate())
            return
        setProgress(true)

        compositeDisposable?.add(changePasswordService.changePassword(
            apiAccessToken,changePasswordModel).processRequest(
                        { changePassword ->

                            setProgress(false)
                            var message=changePassword.get("message")

                            removePref(SharedPrefKeys.TOKEN_KEY,
                                        SharedPrefKeys.TOKEN_OBJECT_KEY,
                                        SharedPrefKeys.STUDENT_KEY, SharedPrefKeys.FIRST_LAUNCH)
                                Constants.studentModel = null
                                Constants.studentProfile = null
                                toast(message.toString())
                                startActivity<WelcomeActivity>()
                                finishNoAnim()

                        },
                        { errMsg ->
                            setProgress(false)

                            snackBar(submitButton, errMsg, Snackbar.LENGTH_SHORT)
                            logE(errMsg)
                        }
                ))
    }

    private fun validate(): Boolean {

        if (changePasswordModel.currentPassword.isBlank()) {
            lblOldPassword.error = "Old password is required."
            return false
        }
        else {
            lblOldPassword.isErrorEnabled = false
        }

        if (changePasswordModel.newPassword.isBlank()) {
            lblAlertNewPassword.error = "New Password is required."
            return false
        } else {
            lblAlertNewPassword.isErrorEnabled = false
        }

        if (changePasswordModel.confirmPassword.isBlank()) {
            lblAlertConfirmPassword.error = "Confirm Password is required."
            return false
        } else {
            lblAlertConfirmPassword.isErrorEnabled = false
        }

        if (!changePasswordModel.confirmPassword.equals(changePasswordModel.newPassword)) {
            lblAlertConfirmPassword.error = "New Password and confirm password Should be same"
            return false
        } else {
            lblAlertConfirmPassword.isErrorEnabled = false
        }


        if (!isNetConnected(false)) {
            snackBar(submitButton, getString(R.string.you_are_offline))
            return false
        }


        return true
    }

    private fun setProgress(show: Boolean) {
        if (show) {
            submitButton.visibility = View.INVISIBLE
            showProgress()
        } else {
            submitButton.visibility = View.VISIBLE
            hideProgress()
        }
    }
}
