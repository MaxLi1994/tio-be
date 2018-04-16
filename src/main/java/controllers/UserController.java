package controllers;

import annotations.ValidatePara;
import models.TpUser;
import models.User;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.POST;
import validators.*;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;



/**
 * @author Jieying Xu
 */
public class UserController extends BaseController {

    /**
     * @api {post} /user/createAccount Create a new user account
     * @apiName createAccount
     * @apiGroup user
     *
     * @apiParam {String} account User's email address(no duplicates allowed).
     * @apiParam {String} nickname Username.
     * @apiParam {String} password User password.
     * @apiSuccessExample {json} Success-Response:
     * {
     *    "code": 0,
     *    "data": {
     *        "nickname": "jieying",
     *        "id": 2,
     *        "account": "jieyingx@andrew.cmu.edu"
     *       }
     * }
     * @apiError {Msg} 1 User didn't complete all fields.
     * @apiError {Msg} 2 User input is empty string or whitespaces.
     * @apiError {Msg} 3 Input account is already taken by other users.
     * @apiError {Msg} 4 Account is not a valid email address.
     */
    @Before(POST.class)
    @ValidatePara(value = "account", validators = {NullValidator.class, EmptyStringValidator.class})
    @ValidatePara(value = "nickname", validators = {NullValidator.class, EmptyStringValidator.class})
    @ValidatePara(value = "password", validators = {NullValidator.class, EmptyStringValidator.class})
    public void createAccount() {
        String account = getPara("account");
        String nickname = getPara("nickname");
        String password = getPara("password");

        if (User.dao.findFirst("select * from user where account=?", account) != null) {
            errorResponse("User already existed!");
        } else {
            boolean result = true;
            try {
                InternetAddress emailAddr = new InternetAddress(account);
                emailAddr.validate();
            } catch (AddressException ex) {
                result = false;
            }

            if (!result) {
                errorResponse("Illegal email address!");
            } else {
                User newUser = new User();
                newUser.set("account", account).set("nickname", nickname).set("password", password).save();
                newUser.remove("password");
                successResponse(newUser);
            }
        }
    }

    /**
     * @api {get} /user/login Check user info with database record to log in
     * @apiName login
     * @apiGroup user
     *
     * @apiParam {String} account User input email address.
     * @apiParam {String} password User input password.
     *
     * @apiSuccessExample {json} Success-Response:
     * {
     *    "code": 0,
     *    "data": {
     *        "nickname": "jieying",
     *        "id": 2,
     *        "account": "jieyingx@andrew.cmu.edu"
     *       }
     * }
     * @apiError {Msg} 1 User didn't complete all fields.
     * @apiError {Msg} 2 User input is empty string or whitespaces.
     * @apiError {Msg} 3 User input doesn't correspond to any database record.
     */
    @Before(GET.class)
    @ValidatePara(value = "account", validators = {NullValidator.class, EmptyStringValidator.class})
    @ValidatePara(value = "password", validators = {NullValidator.class, EmptyStringValidator.class})
    public void login() {
        String account = getPara("account");
        String password = getPara("password");
        User myUser = User.dao.findFirst("select * from user where account=? AND password=?", account, password);
        if (myUser == null) {
            errorResponse("Account/Password combination doesn't exist!");
        } else {
            myUser.remove("password");
            successResponse(myUser);
        }
    }

    /**
     * @api {post} /user/changeNickname Change nickname given user id.
     * @apiName changeNickname
     * @apiGroup user
     *
     * @apiParam {String} userId Current user's id.
     * @apiParam {String} newNickname User input new nickname.
     *
     * @apiSuccessExample {json} Success-Response:
     * {
     *    "code": 0,
     *    "data": {
     *        "nickname": "jieying",
     *        "id": 2,
     *        "account": "jieyingx@andrew.cmu.edu"
     *       }
     * }
     * @apiError {Json} 1 User didn't complete all fields.
     * @apiError {Json} 2 User input is empty string or whitespaces.
     * @apiError {Json} 3 userId must be an integer.
     * @apiError {Json} 4 Provided user id is not found in the database.
     */
    @Before(POST.class)
    @ValidatePara(value = "userId", validators = {NullValidator.class, EmptyStringValidator.class, IntegerFormatValidator.class, UserRecordExistValidator.class})
    @ValidatePara(value = "newNickname", validators = {NullValidator.class, EmptyStringValidator.class})
    public void changeNickname() {
        String userIdStr = getPara("userId");
        String newNickname = getPara("newNickname");
        User myUser = User.dao.findById(Integer.parseInt(userIdStr));
        myUser.set("nickname", newNickname).update();
        myUser.remove("password");
        successResponse(myUser);
    }

    /**
     * @api {post} /user/changePassword Change password given user id.
     * @apiName changePassword
     * @apiGroup user
     *
     * @apiParam {String} userId Current user's id.
     * @apiParam {String} oldPassword User input old password.
     * @apiParam {String} newPassword User input new password.
     *
     * @apiSuccessExample {json} Success-Response:
     * {
     *    "code": 0,
     *    "msg": String
     * }
     *
     * @apiError {Json} 1 User didn't complete all fields.
     * @apiError {Json} 2 User input is empty string or whitespaces.
     * @apiError {Json} 3 Provided user id is not found in the database.
     * @apiError {Json} 4 userId must be an integer.
     * @apiError {Json} 5 Provided user id is not found in the database.
     * @apiError {Json} 6 User input old password is not compatible with database record.
     */
    @Before(POST.class)
    @ValidatePara(value = "userId", validators = {NullValidator.class, EmptyStringValidator.class, IntegerFormatValidator.class, UserRecordExistValidator.class})
    @ValidatePara(value = "oldPassword", validators = {NullValidator.class, EmptyStringValidator.class})
    @ValidatePara(value = "newPassword", validators = {NullValidator.class, EmptyStringValidator.class})
    public void changePassword() {
        String userIdStr = getPara("userId");
        String oldPassword = getPara("oldPassword");
        String newPassword = getPara("newPassword");
        User myUser = User.dao.findById(Integer.parseInt(userIdStr));

        String userOldPassword = myUser.getStr("password");
        if (!userOldPassword.equals(oldPassword)) {
            errorResponse("Password not compatible!");
        } else {
            myUser.set("password", newPassword).update();
            successResponse("Password successfully reset!");
        }
    }

    /**
     * @api {get} /user/loginWithTpId Use Third-party id to log in.
     * @apiName loginWithTpId
     * @apiGroup user
     *
     * @apiParam {String} tpId User's third-party
     * id.
     *
     * @apiSuccessExample {json} Success-Response:
     * {
     *    "code": 0,
     *    "data": {
     *        "nickname": "jieying",
     *        "id": 2,
     *        "account": "jieyingx@andrew.cmu.edu"
     *       }
     * }
     *
     * @apiError {Json} 1 Lack input.
     * @apiError {Json} 1 Input is empty string or whitespaces.
     * @apiError {Json} 1 Third-party id not found in the records.
     */
    @Before(GET.class)
    @ValidatePara(value = "tpId", validators = {NullValidator.class, EmptyStringValidator.class, TpRecordExistValidator.class})
    public void loginWithTpId() {
        String tpId = getPara("tpId");
        int userId = TpUser.dao.findFirst("select * from tp_user where tp_id=?", tpId).getInt("user_id");
        User myUser = User.dao.findById(userId);
        if (myUser == null) {
            errorResponse("User not found!");
        } else {
            myUser.remove("password");
            successResponse(myUser);
        }
    }

    /**
     * @api {post} /user/bindGoogleIdWithUserId Bind Google id with user id.
     * @apiName bindGoogleIdWithUserId
     * @apiGroup user
     *
     * @apiParam {String} googleId User's Google id.
     * @apiParam {String} userId User's user id.
     *
     * @apiSuccessExample {json} Success-Response:
     * {
     *    "code": 0,
     *    "data": {
     *        "nickname": "jieying",
     *        "id": 2,
     *        "account": "jieyingx@andrew.cmu.edu"
     *       }
     * }
     * @apiError {Json} 1 Lack input parameters.
     * @apiError {Json} 2 Input is empty string or whitespaces.
     * @apiError {Json} 3 Input userId is not legal integer format.
     * @apiError {Json} 3 User id not found in the records.
     */
    @Before(POST.class)
    @ValidatePara(value = "googleId", validators = {NullValidator.class, EmptyStringValidator.class})
    @ValidatePara(value = "userId", validators = {NullValidator.class, EmptyStringValidator.class, IntegerFormatValidator.class, UserRecordExistValidator.class})
    public void bindGoogleIdWithUserId() {
        String googleId = getPara("googleId");
        int userId = Integer.parseInt(getPara("userId"));
        TpUser newRecord = new TpUser();
        newRecord.set("tp_id", googleId).set("user_id", userId).set("type", "google").save();
        User myUser = User.dao.findById(userId);
        successResponse(myUser.remove("password"));
    }
}
