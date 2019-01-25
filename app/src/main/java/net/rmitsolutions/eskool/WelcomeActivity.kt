package net.rmitsolutions.eskool


import android.app.Dialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.include_login.*
import net.rmitsolutions.eskool.dashboard.DashboardActivity
import net.rmitsolutions.eskool.databinding.ActivityWelcomeBinding
import net.rmitsolutions.eskool.databinding.ResetPasswordDataBinding
import net.rmitsolutions.eskool.dependency.components.DaggerBaseActivityComponent
import net.rmitsolutions.eskool.helpers.*
import net.rmitsolutions.eskool.models.AccessToken
import net.rmitsolutions.eskool.network.IForgotPassword
import net.rmitsolutions.eskool.network.IStudent
import net.rmitsolutions.eskool.network.IUser
import net.rmitsolutions.eskool.network.TokenService
import net.rmitsolutions.eskool.viewmodels.UserViewModel
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import javax.inject.Inject


class WelcomeActivity : AppCompatActivity() {
    lateinit private var fireBaseAnalytics: FirebaseAnalytics
    private var compositeDisposable: CompositeDisposable? = null
    lateinit var userViewModel: UserViewModel
    lateinit var resetPasswordModel: ResetPasswordModel
    lateinit var dataBinding: ActivityWelcomeBinding
    @Inject lateinit var studentService: IStudent
    lateinit var resetPasswordDatabinding: ResetPasswordDataBinding
    @Inject lateinit var forgotPasswordService: IForgotPassword

    private val tokenService: TokenService by lazy { TokenService() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fireBaseAnalytics = FirebaseAnalytics.getInstance(this)

        val userAccessToken = getPref(SharedPrefKeys.TOKEN_OBJECT_KEY, "")

        val depComponent = DaggerBaseActivityComponent.builder()
                .applicationComponent(ESkoolApplication.applicationComponent)
                .build()
        depComponent.injectWelcomeActivity(this)

        if (!userAccessToken.isEmpty()) {
            Constants.accessToken = JsonHelper.jsonToKt<AccessToken>(userAccessToken)
            startActivity<DashboardActivity>()
            finishNoAnim()
            return
        }

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)
        Glide.with(this).load(R.drawable.reqelford).into(welcomeLogo)
        userViewModel = UserViewModel("", "")
        dataBinding.user = userViewModel
        compositeDisposable = CompositeDisposable()

    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.dispose()
    }

    override fun attachBaseContext(newBase: Context) = super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))

    fun onLoginClick(view: View) {
        login()
    }

    fun onForgotPasswordClick(view: View) {

      var dialog = Dialog(this)

        resetPasswordDatabinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.reset_password_alert, null, false)
        dialog.setContentView(resetPasswordDatabinding.root)
        dialog.show()
        dialog.setCancelable(true)

        resetPasswordModel = ResetPasswordModel("", "")
        resetPasswordDatabinding.resetpasswordVM = resetPasswordModel

        resetPasswordDatabinding.closeButton.setOnClickListener() {
            dialog.dismiss()
        }

        resetPasswordDatabinding.dialogClose.setOnClickListener() {
            dialog.dismiss()
        }

    }

    fun onSubmitClick(view: View) {

        if (!resetPasswordAlertValidate()) return
        setProgressResetPassword(true)
        compositeDisposable?.add(forgotPasswordService.forgotPassword(  apiAccessToken,
                resetPasswordModel)
                .processRequest(
                        { forgotPassword ->
                            setProgressResetPassword(false)
                            var message=forgotPassword.get("message")
                            toast(message.toString())
                            startActivity<WelcomeActivity>()
                            finishNoAnim()

                        },
                        { errMsg ->
                            setProgressResetPassword(false)
                            toast(errMsg.toString())
                            logE(errMsg)
                        }
                ))


    }

    private fun resetPasswordAlertValidate(): Boolean {
        if (resetPasswordModel.userName.isBlank()) {
            resetPasswordDatabinding.lblAlertUserName.error = "User name is required."
            return false
        } else {
            resetPasswordDatabinding.lblAlertUserName.isErrorEnabled = false
        }
        if (resetPasswordModel.email.isBlank()) {
            resetPasswordDatabinding.lblAlertEmail.error = "Email is required"
            return false
        } else {
            resetPasswordDatabinding.lblAlertEmail.isErrorEnabled = false
        }

        if (!isValidEmail(resetPasswordModel.email)) {
            resetPasswordDatabinding.lblAlertEmail.error = "Invalid Email"
            return false
        } else {
            resetPasswordDatabinding.lblAlertEmail.isErrorEnabled = false
        }

        if (!isNetConnected(false)) {
            snackBar(buttonLogin, getString(R.string.you_are_offline))
            return false
        }

        return true
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        return if (email.matches(emailRegex.toRegex())) {
            true
        } else false
    }

    private fun login() {
        if (!validate()) return

        setProgress(true)
        compositeDisposable?.add(tokenService.getService<IUser>().validateUser(
                Constants.GRANT_TYPE, Constants.CLIENT_ID, Constants.CLIENT_SECRET, userViewModel.userName, userViewModel.password)
                .processRequest(
                        { token ->
                           // putPref("isFirstTimeAppLunched",false)
                            logFireBase(userViewModel.userName)
                            Constants.accessToken = token
                            val gson = Gson()
                            val json = gson.toJson(token)
                            println(json)
                            addBulkPrefs {
                                editor ->
                                addToPrefEditor(editor, SharedPrefKeys.TOKEN_OBJECT_KEY, JsonHelper.KtToJson(token))
                                addToPrefEditor(editor, SharedPrefKeys.TOKEN_KEY, token.accessToken)
                            }

                            setProgress(false)
                            getStudentInfo()

                        },
                        { errMsg ->
                            setProgress(false)
                            snackBar(buttonLogin, errMsg, Snackbar.LENGTH_SHORT)
                            logE(errMsg)
                        }
                ))
    }
    private fun getStudentInfo() {
        compositeDisposable!!.add(studentService.getStudent(apiAccessToken)
                .processRequest(
                        { student ->

                            val gson = Gson()
                            val json = gson.toJson(student)
                            println(json)
                            if(student.isFirstLogin==1){
                                removePref(SharedPrefKeys.TOKEN_KEY,
                                        SharedPrefKeys.TOKEN_OBJECT_KEY,
                                        SharedPrefKeys.STUDENT_KEY,SharedPrefKeys.FIRST_LAUNCH)
                                Constants.studentModel = null
                                Constants.studentProfile = null
                                startActivity<ChangePasswordActivity>()
                                finishNoAnim()

                            }
                        else{
                                startActivity<DashboardActivity>()
                                finishNoAnim()
                            }
                        },
                        { errMsg ->
                            toast("$errMsg")
                            logE(errMsg)
                        }
                ))
    }


    private fun validate(): Boolean {
        if (userViewModel.userName.isBlank()) {
            lblUserName.error = "User name is required."
            return false
        } else {
            lblUserName.isErrorEnabled = false
        }

        if (userViewModel.password.isBlank()) {
            lblPassword.error = "Password is required."
            return false
        } else {
            lblPassword.isErrorEnabled = false
        }

        if (!isNetConnected(false)) {
            snackBar(buttonLogin, getString(R.string.you_are_offline))
            return false
        }

        return true
    }


    private fun setProgress(show: Boolean) {
        if (show) {
            buttonLogin.visibility = View.INVISIBLE
            showProgress()
        } else {
            buttonLogin.visibility = View.VISIBLE
            hideProgress()
        }
    }

    private fun setProgressResetPassword(show: Boolean) {
        if (show) {
            resetPasswordDatabinding.submitButton.visibility = View.INVISIBLE
            resetPasswordDatabinding.indicatorView.visibility=View.VISIBLE
            resetPasswordDatabinding.progressBar.visibility=View.VISIBLE

        } else {
            resetPasswordDatabinding.submitButton.visibility = View.VISIBLE
            resetPasswordDatabinding.indicatorView.visibility=View.INVISIBLE
            resetPasswordDatabinding.progressBar.visibility=View.INVISIBLE
        }
    }

    private fun logFireBase(user: String) {
        fireBaseAnalytics.setUserId(user)
        val bundle = Bundle()
        bundle.putString("UserName", user);
        fireBaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)
    }
}
