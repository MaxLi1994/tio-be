package controllers;

import models.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;



/**
 * @author Jieying Xu
 */
public class UserController extends Controller {

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
     *     "nickname": string,
     *     "account": string,
     *     "id": int
     * }
     * @apiError {Msg} 1 User didn't complete all fields.
     * @apiError {Msg} 2 User input is empty string or whitespaces.
     * @apiError {Msg} 3 Input account is already taken by other users.
     * @apiError {Msg} 4 Account is not a valid email address.
     */
    @Before(POST.class)
    public void createAccount() {
        String account = getPara("account");
        String nickname = getPara("nickname");
        String password = getPara("password");

        if (account == null || nickname == null || password == null) {
            setAttr("code", -1);
            setAttr("msg", "Lack input");
        } else if (account.trim().equals("") || nickname.trim().equals("") || password.trim().equals("")) {
            setAttr("code", -1);
            setAttr("msg", "User input cannot be empty string or pure whitespaces.");
        } else if (User.dao.findFirst("select * from user where account=?", account) != null) {
            setAttr("code", -1);
            setAttr("msg", "User already existed.");
        } else {
            boolean result = true;
            try {
                InternetAddress emailAddr = new InternetAddress(account);
                emailAddr.validate();
            } catch (AddressException ex) {
                result = false;
            }

            if (!result) {
                setAttr("code", -1);
                setAttr("msg", "Illegal email address.");
            } else {
                User newUser = new User();
                newUser.set("account", account).set("nickname", nickname).set("password", password).save();
                setAttr("code", 0);
                newUser.remove("password");
                setAttr("data", newUser); // remove password field
            }
        }
        renderJson();
    }

    /**
     * @api {get} /user/login Check user input info with database record before log in
     * @apiName login
     * @apiGroup user
     *
     * @apiParam {String} account User input email address.
     * @apiParam {String} password User input password.
     *
     * @apiSuccessExample {json} Success-Response:
     * {
     *     "account": string,
     *     "nickname": string,
     *     "id": int
     * }
     * @apiError {Msg} 1 User didn't complete all fields.
     * @apiError {Msg} 2 User input is empty string or whitespaces.
     * @apiError {Msg} 3 User input doesn't correspond to any database record.
     */
    public void login() {
        String account = getPara("account");
        String password = getPara("password");
        if (account == null || password == null) {
            setAttr("code", -1);
            setAttr("msg", "Lack input.");
        } else if (account.trim().equals("") || password.trim().equals("")) {
            setAttr("code", -1);
            setAttr("msg", "User input cannot be empty string or pure whitespaces.");
        } else {
            User myUser = User.dao.findFirst("select * from user where account=? AND password=?", account, password);
            if (myUser == null) {
                setAttr("code", -1);
                setAttr("msg", "Account/Password combination doesn't exist.");
            } else {
                myUser.remove("password");
                setAttr("code", 0);
                setAttr("data", myUser);
            }
        }
        renderJson();
    }

    /**
     * @api {post} /user/changeNickName Change the user's nickname given his/her id.
     * @apiName changeNickName
     * @apiGroup user
     * @apiParam {String} user_id Current user's id.
     * @apiParam {String} newNickName User input new nickname.
     * @apiSuccess {Json} UserObject Updated user model.
     * @apiError {Json} IncompleteInput User didn't complete all fields.
     * @apiError {Json} IllegalEmptyInput User input is empty string or whitespaces.
     * @apiError {Json} IdFormatError User_id must be an integer.
     * @apiError {Json} UserNotFound Provided user id is not found in the database.
     */
    public void changeNickName() {
        String user_idStr = getPara("user_id");
//        System.out.println(user_idStr);
        String newNickName = getPara("newNickName");
        if (user_idStr == null || newNickName == null) {
            setAttr("code", -1);
            setAttr("msg", "Lack input.");
        } else if (user_idStr.trim().equals("") || newNickName.trim().equals("")) {
            setAttr("code", -1);
            setAttr("msg", "User input cannot be empty string or pure whitespaces.");
        } else {
            boolean success = true;
            int user_id = 0;
            try {
                System.out.println(user_idStr);
                user_id = Integer.parseInt(user_idStr);
            } catch (NumberFormatException e) {
                System.out.println("not success");
                success = false;
            }
            if (!success) {
                setAttr("code", -1);
                setAttr("msg", "User id must be an Integer.");
            } else {
                User myUser = User.dao.findById(user_id);
                if (myUser == null) {
                    setAttr("code", -1);
                    setAttr("msg", "User not found.");
                } else {
                    myUser.set("nickname", newNickName).update();
                    setAttr("code", 0);
                    setAttr("data", myUser);
                }
            }
        }
        renderJson();
    }

    /**
     * @api {post} /user/changePassWord Change the user's password given his/her id.
     * @apiName changePassWord
     * @apiGroup user
     * @apiParam {String} user_id Current user's id.
     * @apiParam {String} oldPassWord User input old password.
     * @apiParam {String} newPassWord User input new password.
     * @apiSuccess {Json} SuccessMsg User's password field has been changed in database.
     * @apiError {Json} IncompleteInput User didn't complete all fields.
     * @apiError {Json} IllegalEmptyInput User input is empty string or whitespaces.
     * @apiError {Json} UserNotFound Provided user id is not found in the database.
     * @apiError {Json} IdFormatError User_id must be an integer.
     * @apiError {Json} UserNotFound Provided user id is not found in the database.
     * @apiError {Json} IncompatibleOldPassword User input old password is not compatible with database record.
     */
    public void changePassWord() {
        String user_idStr = getPara("user_id");
        String oldPassWord = getPara("oldPassWord");
        String newPassWord = getPara("newPassWord");

        if (user_idStr == null || oldPassWord == null || newPassWord == null) {
            setAttr("code", -1);
            setAttr("msg", "Lack input");
        } else if (user_idStr.trim().equals("") || oldPassWord.trim().equals("") || newPassWord.trim().equals("")) {
            setAttr("code", -1);
            setAttr("msg", "User input cannot be empty string or pure whitespaces.");
        } else {
            boolean success = true;
            int user_id = 0;
            try {
                user_id = Integer.parseInt(user_idStr);
            } catch (NumberFormatException e) {
                success = false;
            }
            if (!success) {
                setAttr("code", -1);
                setAttr("msg", "User id must be an Integer.");
            } else {
                User myUser = User.dao.findById(user_id);
                if (myUser == null) {
                    setAttr("code", -1);
                    setAttr("msg", "User not found");
                } else {
                    String userOldPassWord = myUser.getStr("password");
                    if (!userOldPassWord.equals(oldPassWord)) {
                        setAttr("code", -1);
                        setAttr("msg", "Password not compatible.");
                    } else {
                        myUser.set("password", newPassWord).update();
                        setAttr("code", 0);
                        setAttr("msg", "Password successfully reset.");
                    }
                }
            }
        }
        renderJson();
    }


}
