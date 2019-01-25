package net.rmitsolutions.eskool.profile

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.rmitsolutions.eskool.BaseFragment
import net.rmitsolutions.eskool.ESkoolApplication
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.dependency.components.DaggerBaseFragmentComponent
import net.rmitsolutions.eskool.helpers.*
import net.rmitsolutions.eskool.models.Student
import net.rmitsolutions.eskool.network.IStudent
import net.rmitsolutions.eskool.viewmodels.ProfileViewModel
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.find
import java.text.DateFormat
import javax.inject.Inject

/**
 * Created by Madhu on 05-Jul-2017.
 */
class ProfileFragment : BaseFragment() {
    @Inject
    lateinit var studentService: IStudent

    var sectionIndex: Int = 0
    lateinit var recyclerView: RecyclerView
    val adapter: ProfileRecyclerViewAdapter by lazy { ProfileRecyclerViewAdapter() }
    lateinit var modelList: MutableList<ProfileViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dependComponent = DaggerBaseFragmentComponent.builder()
                .applicationComponent(ESkoolApplication.applicationComponent)
                .build()
        dependComponent.injectProfileFragment(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_profile, container, false)

        recyclerView = rootView.find<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter
        sectionIndex = arguments!!.getInt(ARG_SECTION_NUMBER)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        modelList = mutableListOf<ProfileViewModel>()
        when (sectionIndex) {
            PROFILE, GRADE -> getStudentPersonalAndGradeInfo()
            PARENTS, ADDRESS -> getStudentProfile()
        }
    }

    private fun setRecyclerViewAdapter(list: List<ProfileViewModel>) {
        adapter.items = list
        adapter.notifyDataSetChanged()
    }

    companion object {
        private val PROFILE: Int = 0
        private val GRADE: Int = 1
        private val PARENTS: Int = 2
        private val ADDRESS: Int = 3


        private val ARG_SECTION_NUMBER = "section_number"
        fun newInstance(sectionNumber: Int): ProfileFragment {
            val fragment = ProfileFragment()
            fragment.arguments = bundleOf(ARG_SECTION_NUMBER to sectionNumber)
            return fragment
        }
    }

    private fun getStudentPersonalAndGradeInfo() {
        if (Constants.studentModel == null) {
            val student = context!!.getPref(SharedPrefKeys.STUDENT_KEY, "")
            if (!student.isEmpty()) {
                Constants.studentModel = JsonHelper.jsonToKt<Student>(student)
            }
        }

        if (Constants.studentModel != null) {
            if (sectionIndex == PROFILE) setPersonalData() else setGradeData()
            return
        }

        if (!isNetConnected()) return
        showProgress()
        compositeDisposable.add(studentService.getStudent(apiAccessToken)
                .processRequest(
                        { student ->
                            Constants.studentModel = student
                            context!!.putPref(SharedPrefKeys.STUDENT_KEY, JsonHelper.KtToJson(student))
                            hideProgress()
                            if (sectionIndex == PROFILE) setPersonalData() else setGradeData()
                        },
                        { errMsg ->
                            hideProgress(true, errMsg)
                            logE(errMsg)
                        }
                ))
    }

    private fun setPersonalData() {
        modelList.clear()
        val student = Constants.studentModel!!
        modelList.add(ProfileViewModel("Name", student.name))
        modelList.add(ProfileViewModel("Gender", student.genderName))
        modelList.add(ProfileViewModel("Born on", DateFormat.getDateInstance().format(student.dob)))
        modelList.add(ProfileViewModel("Mobile Number", student.mobile))
        modelList.add(ProfileViewModel("Email", student.email))
        setRecyclerViewAdapter(modelList)
    }

    private fun setGradeData() {
        modelList.clear()
        val student = Constants.studentModel!!
        modelList.add(ProfileViewModel("School", student.school))
        modelList.add(ProfileViewModel("Student Code", student.code))
        modelList.add(ProfileViewModel("Syllabus", student.syllabus))
        modelList.add(ProfileViewModel("Grade", student.grade))
        modelList.add(ProfileViewModel("Section", student.section))
        modelList.add(ProfileViewModel("Academic Year", student.academicYear))
        setRecyclerViewAdapter(modelList)
    }

    private fun getStudentProfile() {
        if (Constants.studentProfile != null) {
            if (sectionIndex == PARENTS) setStudentParents() else setStudentAddress()
            return
        }

        if (!isNetConnected()) return
        showProgress()
        compositeDisposable.add(
                studentService.getStudentProfile(apiAccessToken)
                        .processRequest(
                                { profile ->


                                    Constants.studentProfile = profile
                                    if (sectionIndex == PARENTS) setStudentParents() else setStudentAddress()
                                    hideProgress()
                                },
                                { error ->
                                    hideProgress(true, error)
                                    logE(error)
                                }
                        )
        )
    }

    private fun setStudentParents() {
        modelList.clear()
        if (Constants.studentProfile == null) return

        val parents = Constants.studentProfile!!.parents
        for (p in parents) {
            modelList.add(ProfileViewModel("Relation", p.relation))
            modelList.add(ProfileViewModel("Name", p.name))
            modelList.add(ProfileViewModel("Mobile", p.mobile))
            modelList.add(ProfileViewModel("Email", p.email))
        }
        setRecyclerViewAdapter(modelList)
    }

    private fun setStudentAddress() {
        modelList.clear()
        if (Constants.studentProfile == null) return

        val address = Constants.studentProfile!!.address
        modelList.add(ProfileViewModel("H.No", address?.houseNo ?: ""))
        modelList.add(ProfileViewModel("Street/Colony", address?.street ?: ""))
        modelList.add(ProfileViewModel("City", address?.city ?: ""))
        modelList.add(ProfileViewModel("District", address?.district ?: ""))
        modelList.add(ProfileViewModel("State", address?.state ?: ""))
        modelList.add(ProfileViewModel("Pin code", address?.pincode ?: ""))
        modelList.add(ProfileViewModel("Phone", address?.phone ?: ""))
        setRecyclerViewAdapter(modelList)
    }
}