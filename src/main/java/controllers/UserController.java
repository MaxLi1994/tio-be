package controllers;

import models.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.POST;
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
    @Before(GET.class)
    public void login() {
        String account = getPara("account");
        String password = getPara("password");
        if (account == null || password == null) {
            errorResponse("Lack input");
        } else if (account.trim().equals("") || password.trim().equals("")) {
            errorResponse("User input cannot be empty string or pure whitespaces.");
        } else {
            User myUser = User.dao.findFirst("select * from user where account=? AND password=?", account, password);
            if (myUser == null) {
                errorResponse("Account/Password combination doesn't exist.");
            } else {
                myUser.remove("password");
                successResponse(myUser);
            }
        }
    }

    /**
     * @api {post} /user/changeNickname Change the user's nickname given his/her id.
     * @apiName changeNickname
     * @apiGroup user
     *
     * @apiParam {String} userId Current user's id.
     * @apiParam {String} newNickname User input new nickname.
     *
     * @apiSuccessExample {json} Success-Response:
     * {
     *     "account": string,
     *     "nickname": string,
     *     "id": int
     * }
     * @apiError {Json} 1 User didn't complete all fields.
     * @apiError {Json} 2 User input is empty string or whitespaces.
     * @apiError {Json} 3 userId must be an integer.
     * @apiError {Json} 4 Provided user id is not found in the database.
     */
    @Before(POST.class)
    public void changeNickname() {
        String userIdStr = getPara("userId");
        String newNickname = getPara("newNickname");
        if (userIdStr == null || newNickname == null) {
            setAttr("code", -1);
            setAttr("msg", "Lack input.");
        } else if (userIdStr.trim().equals("") || newNickname.trim().equals("")) {
            setAttr("code", -1);
            setAttr("msg", "User input cannot be empty string or pure whitespaces.");
        } else {
            boolean success = true;
            int userId = 0;
            try {
                System.out.println(userIdStr);
                userId = Integer.parseInt(userIdStr);
            } catch (NumberFormatException e) {
                System.out.println("not success");
                success = false;
            }
            if (!success) {
                setAttr("code", -1);
                setAttr("msg", "User id must be an Integer.");
            } else {
                User myUser = User.dao.findById(userId);
                if (myUser == null) {
                    setAttr("code", -1);
                    setAttr("msg", "User not found.");
                } else {
                    myUser.set("nickname", newNickname).update();
                    myUser.remove("password");
                    setAttr("code", 0);
                    setAttr("data", myUser);
                }
            }
        }
        renderJson();
    }

    /**
     * @api {post} /user/changePassword Change the user's password given his/her id.
     * @apiName changePassword
     * @apiGroup user
     *
     * @apiParam {String} userId Current user's id.
     * @apiParam {String} oldPassword User input old password.
     * @apiParam {String} newPassword User input new password.
     *
     * @apiSuccessExample {json} Success-Response:
     * {
     *     "msg": string
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
    public void changePassword() {
        String userIdStr = getPara("userId");
        String oldPassword = getPara("oldPassword");
        String newPassword = getPara("newPassword");

        if (userIdStr == null || oldPassword == null || newPassword == null) {
            setAttr("code", -1);
            setAttr("msg", "Lack input");
        } else if (userIdStr.trim().equals("") || oldPassword.trim().equals("") || newPassword.trim().equals("")) {
            setAttr("code", -1);
            setAttr("msg", "User input cannot be empty string or pure whitespaces.");
        } else {
            boolean success = true;
            int userId = 0;
            try {
                userId = Integer.parseInt(userIdStr);
            } catch (NumberFormatException e) {
                success = false;
            }
            if (!success) {
                setAttr("code", -1);
                setAttr("msg", "User id must be an Integer.");
            } else {
                User myUser = User.dao.findById(userId);
                if (myUser == null) {
                    setAttr("code", -1);
                    setAttr("msg", "User not found");
                } else {
                    String userOldPassword = myUser.getStr("password");
                    if (!userOldPassword.equals(oldPassword)) {
                        setAttr("code", -1);
                        setAttr("msg", "Password not compatible.");
                    } else {
                        myUser.set("password", newPassword).update();
                        setAttr("code", 0);
                        setAttr("msg", "Password successfully reset.");
                    }
                }
            }
        }
        renderJson();
    }


}
