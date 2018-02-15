package com.thinkpace.pacifyca.entity;

/**
 * Created by Krishna Upadhya on 12/1/2016.
 */

public class CPCStudentDetails implements IPCDataModel{

    private String FathersMobileNo;

    private String FathersName;

    private String MothersMobileNo;

    private String FullName;

    private String CourseName;

    private String student_id;

    private String GuardianMobileNo;

    public String getProfilePhoto() {
        return ProfilePhoto;
    }

    private String ProfilePhoto;

    private String MobileAppEnabled;

    private String GuardianName;

    private String client_id;

    private String MotherName;

    private String Institute;

    private String AcademicYear;

    private String RegisterNumber;

    public String getFathersMobileNo ()
    {
        return FathersMobileNo;
    }

    public void setFathersMobileNo (String FathersMobileNo)
    {
        this.FathersMobileNo = FathersMobileNo;
    }

    public String getFathersName ()
    {
        return FathersName;
    }

    public void setFathersName (String FathersName)
    {
        this.FathersName = FathersName;
    }

    public String getMothersMobileNo ()
    {
        return MothersMobileNo;
    }

    public void setMothersMobileNo (String MothersMobileNo)
    {
        this.MothersMobileNo = MothersMobileNo;
    }

    public String getFullName ()
    {
        return FullName;
    }

    public void setFullName (String FullName)
    {
        this.FullName = FullName;
    }

    public String getCourseName ()
    {
        return CourseName;
    }

    public String getStudentAdmissionNo ()
    {
        return RegisterNumber;
    }

    public void setCourseName (String CourseName)
    {
        this.CourseName = CourseName;
    }

    public void setRegisterNumber (String RegisterNumber)
    {
        this.RegisterNumber = RegisterNumber;
    }

    public String getStudent_id ()
    {
        return student_id;
    }

    public void setStudent_id (String student_id)
    {
        this.student_id = student_id;
    }

    public String getGuardianMobileNo ()
    {
        return GuardianMobileNo;
    }

    public void setGuardianMobileNo (String GuardianMobileNo)
    {
        this.GuardianMobileNo = GuardianMobileNo;
    }

    public String getMobileAppEnabled ()
    {
        return MobileAppEnabled;
    }

    public void setMobileAppEnabled (String MobileAppEnabled)
    {
        this.MobileAppEnabled = MobileAppEnabled;
    }

    public String getGuardianName ()
    {
        return GuardianName;
    }

    public void setGuardianName (String GuardianName)
    {
        this.GuardianName = GuardianName;
    }

    public String getClient_id ()
    {
        return client_id;
    }

    public void setClient_id (String client_id)
    {
        this.client_id = client_id;
    }

    public String getMotherName ()
    {
        return MotherName;
    }

    public void setMotherName (String MotherName)
    {
        this.MotherName = MotherName;
    }

    public String getInstitute ()
    {
        return Institute;
    }

    public void setInstitute (String Institute)
    {
        this.Institute = Institute;
    }

    public String getAcademicYear ()
    {
        return AcademicYear;
    }

    public void setAcademicYear (String AcademicYear)
    {
        this.AcademicYear = AcademicYear;
    }

}
