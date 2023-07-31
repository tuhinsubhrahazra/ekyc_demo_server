/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connection;

/**
 *
 * @author Tuhin
 */
public class DBConstraints {

    public static String EKYC_LOGIN_SP = "{call Proc_Login(?,?,?,?,?)}";
    public static String EKYC_IS_ALREADY_ACC = "{call Proc_isAlreadyAcc(?,?)}";
    public static String EKYC_SEND_OTP = "{call Proc_GetSMSCredential(?,?,?)}";
    public static String EKYC_VERIFY_OTP = "{call Proc_verifyOTP(?,?)}";
    public static String EKYC_SAVE_ACC_INFO = "{call proc_saveAccInfo(?,?,?)}";
    public static String EKYC_SAVE_PAN_INFO = "{call Proc_savePanDetails(?,?,?,?)}";
    public static String EKYC_SAVE_PERSONAL_DETAILS = "{call proc_savepersonaldetails(?,?,?,?,?,?,?,?,?)}";
    public static String EKYC_SAVE_SEGMENT = "{call proc_savesegment(?,?,?,?,?,?,?)}";
    public static String EKYC_SAVE_DP = "{call proc_savedpdata(?,?,?,?,?,?)}";
    public static String EKYC_SAVE_BANK_DETAILS = "{call proc_savebankdetails(?,?,?,?,?)}";
    public static String EKYC_SAVE_REGULATORY_DETAILS = "{call proc_regulatoryques(?,?,?,?,?,?,?,?,?,?,?)}";


    
            

}
