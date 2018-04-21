package controllers;

import annotations.ValidatePara;
import models.*;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.POST;
import validators.*;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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
     *    "nickname": "jieying",
     *    "id": 2,
     *    "account": "jieyingx@andrew.cmu.edu"
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
     * @api {get} /user/login Check user info before logging in
     * @apiName login
     * @apiGroup user
     *
     * @apiParam {String} account User input email address.
     * @apiParam {String} password User input password.
     *
     * @apiSuccessExample {json} Success-Response:
     * {
     *    "nickname": "jieying",
     *    "id": 2,
     *    "account": "jieyingx@andrew.cmu.edu"
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
     * @api {post} /user/changeNickname Change user's nickname
     * @apiName changeNickname
     * @apiGroup user
     *
     * @apiParam {String} userId Current user's id.
     * @apiParam {String} newNickname User input new nickname.
     *
     * @apiSuccessExample {json} Success-Response:
     * {
     *    "nickname": "jieying",
     *    "id": 2,
     *    "account": "jieyingx@andrew.cmu.edu"
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
     * @api {post} /user/changePassword Change user's password
     * @apiName changePassword
     * @apiGroup user
     *
     * @apiParam {String} userId Current user's id.
     * @apiParam {String} oldPassword User input old password.
     * @apiParam {String} newPassword User input new password.
     *
     * @apiSuccessExample {json} Success-Response:
     * {
     *    "nickname": "jieying",
     *    "id": 2,
     *    "account": "jieyingx@andrew.cmu.edu"
     * }
     *
     * @apiError {Json} 1 User didn't complete all fields.
     * @apiError {Json} 2 User input is empty string or whitespaces.
     * @apiError {Json} 3 userId must be an integer.
     * @apiError {Json} 4 Provided user id is not found in the database.
     * @apiError {Json} 5 User input old password is not compatible with database record.
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
     * @api {get} /user/loginWithTpId Use Third-party id to log in
     * @apiName loginWithTpId
     * @apiGroup user
     *
     * @apiParam {String} tpId User's third-party id.
     * @apiParam {String} type The third-party service name.
     *
     * @apiSuccessExample {json} Success-Response:
     * {
     *    "nickname": "jieying",
     *    "id": 2,
     *    "account": "jieyingx@andrew.cmu.edu"
     * }
     *
     * @apiError {Json} 1 Lack input.
     * @apiError {Json} 1 Input is empty string or whitespaces.
     * @apiError {Json} 1 Third-party user not found in the records.
     */
    @Before(GET.class)
    @ValidatePara(value = "tpId", validators = {NullValidator.class, EmptyStringValidator.class})
    @ValidatePara(value = "type", validators = {NullValidator.class, EmptyStringValidator.class, TpTypeValidator.class})
    public void loginWithTpId() {
        String tpId = getPara("tpId");
        String type = getPara("type");
        TpUser tpUser = TpUser.dao.findFirst("select * from tp_user where tp_id=? and type=?", tpId, type);
        if (tpUser == null) {
            errorResponse("Third-party user not found!");
        } else {
            int userId = tpUser.getInt("user_id");
            User myUser = User.dao.findById(userId);
            if (myUser == null) {
                errorResponse("User not found!");
            } else {
                myUser.remove("password");
                successResponse(myUser);
            }
        }
    }

    /**
     * @api {post} /user/bindTpIdWithUserId Bind third-party id with user id
     * @apiName bindTpIdWithUserId
     * @apiGroup user
     *
     * @apiParam {String} tpId User's third-party id.
     * @apiParam {String} type User's third-party service type.
     * @apiParam {String} userId User's user id.
     *
     * @apiSuccessExample {json} Success-Response:
     * {
     *    "nickname": "jieying",
     *    "id": 2,
     *    "account": "jieyingx@andrew.cmu.edu"
     * }
     * @apiError {Json} 1 Lack input parameters.
     * @apiError {Json} 2 Input is empty string or whitespaces.
     * @apiError {Json} 3 Input userId is not legal integer format.
     * @apiError {Json} 4 User not found.
     * @apiError {Json} 5 User id and type combination already exists.
     * @apiError {Json} 6 Third-party id already exists.
     */
    @Before(POST.class)
    @ValidatePara(value = "tpId", validators = {NullValidator.class, EmptyStringValidator.class})
    @ValidatePara(value = "type", validators = {NullValidator.class, EmptyStringValidator.class, TpTypeValidator.class})
    @ValidatePara(value = "userId", validators = {NullValidator.class, EmptyStringValidator.class, IntegerFormatValidator.class, UserRecordExistValidator.class})
    public void bindTpIdWithUserId() {
        String tpId = getPara("tpId");
        String type = getPara("type");
        int userId = Integer.parseInt(getPara("userId"));
        if (TpUser.dao.findFirst("select * from tp_user where type=? and user_id=?", type, userId) != null) {
            errorResponse("User has already bind a " + type + " account!");
        } else if (TpUser.dao.findFirst("select * from tp_user where tp_id=?", tpId) != null) {
            errorResponse("This "+type+" account has been bind to an user!");
        } else {
            TpUser newRecord = new TpUser();
            newRecord.set("tp_id", tpId).set("user_id", userId).set("type", type).save();
            User myUser = User.dao.findById(userId);
            successResponse(myUser.remove("password"));
        }
    }

    /**
     * @api {get} /favoriteList Display user's favorite list
     * @apiName favoriteList
     * @apiGroup user
     *
     * @apiParam {String} userId User's user id.
     *
     * @apiSuccessExample {json} Success-Response:
     * [
     *     {
     *         "name": "Dior999",
     *         "id": 1,
     *         "desc_img": "www.exampleImageUrl.com"
     *     },
     *     {
     *         "name": "YSL102",
     *         "id": 2,
     *         "desc_img": "www.exampleImageUrl.com"
     *     }
     * ]
     * @apiError {Json} 1 Lack input parameters.
     * @apiError {Json} 2 Input is empty string or whitespaces.
     * @apiError {Json} 3 Input userId is not legal integer format.
     * @apiError {Json} 4 User not found.
     */
    @Before(GET.class)
    @ValidatePara(value = "userId", validators = {NullValidator.class, EmptyStringValidator.class, IntegerFormatValidator.class, UserRecordExistValidator.class})
    public void favoriteList() {
        int userId = Integer.parseInt(getPara("userId"));
        List<FavoriteList> commodityIdList = FavoriteList.dao.find("select commodity_id from favorite_list where user_id = ?", userId);
        List<Commodity> favoriteList = new ArrayList<Commodity>();
        for (FavoriteList f: commodityIdList) {
            int commodityId = f.getInt("commodity_id");
            favoriteList.add(Commodity.dao.findByIdLoadColumns(commodityId, "name, id, desc_img"));
        }
        successResponse(favoriteList);
    }

    /**
     * @api {get} /viewingHistory Display user's viewing history
     * @apiName viewingHistory
     * @apiGroup user
     *
     * @apiParam {String} userId User's user id.
     *
     * @apiSuccessExample {json} Success-Response:
     * [
     *     {
     *         "name": "Dior999",
     *         "id": 1,
     *         "desc_img": "www.exampleImageUrl.com"
     *     },
     *     {
     *         "name": "YSL102",
     *         "id": 2,
     *         "desc_img": "www.exampleImageUrl.com"
     *     }
     * ]
     * @apiError {Json} 1 Lack input parameters.
     * @apiError {Json} 2 Input is empty string or whitespaces.
     * @apiError {Json} 3 Input userId is not legal integer format.
     * @apiError {Json} 4 User not found.
     */
    @Before(GET.class)
    @ValidatePara(value = "userId", validators = {NullValidator.class, EmptyStringValidator.class, IntegerFormatValidator.class, UserRecordExistValidator.class})
    public void viewingHistory() {
        int userId = Integer.parseInt(getPara("userId"));
        List<ViewingHistory> commodityIdList = ViewingHistory.dao.find("select commodity_id from viewing_history where user_id = ?", userId);
        Collections.sort(commodityIdList);
        List<Commodity> viewedCommodities = new ArrayList<Commodity>();
        for (ViewingHistory v: commodityIdList) {
            int commodityId = v.getInt("commodity_id");
            viewedCommodities.add(Commodity.dao.findByIdLoadColumns(commodityId, "name, id, desc_img"));
        }
        successResponse(viewedCommodities);
    }
}
