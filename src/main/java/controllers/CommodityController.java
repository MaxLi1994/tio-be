package controllers;

import annotations.ValidatePara;
import com.jfinal.aop.Before;
import com.jfinal.aop.Interceptor;
import com.jfinal.ext.interceptor.GET;
import models.Category;
import models.Commodity;
import validators.*;

import java.util.List;

/**
 * @author Jieying Xu
 */
public class CommodityController extends BaseController {

    /**
     * @api {get} /commodity/listAll Display all commodities
     * @apiName listAll
     * @apiGroup commodity
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
     */
    @Before(GET.class)
    public void listAll() {
        List<Commodity> allCommodities = Commodity.dao.find("select id, desc_img, name from commodity");
        successResponse(allCommodities);
    }

    /**
     * @api {get} /commodity/list Display all commodities in a category
     * @apiName list
     * @apiGroup commodity
     *
     * @apiParam {String} categoryName The name of the category.
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
     * @apiError {Msg} 1 Lack input.
     * @apiError {Msg} 2 Input is empty string or whitespaces.
     * @apiError {Msg} 3 Category name not found in the database.
     */
    @Before(GET.class)
    @ValidatePara(value = "categoryName", validators = {NullValidator.class, EmptyStringValidator.class, CategoryRecordExistValidator.class})
    public void list() {
        String name = getPara("categoryName");
        int categoryId = Category.dao.findFirst("select * from category where name = ?", name).getInt("id");
        List<Commodity> data = Commodity.dao.find("select id, desc_img, name from commodity where category_id = ?", categoryId);
        successResponse(data);
    }

    /**
     * @api {get} /commodity/tryOn Get the model of the commodity
     * @apiName tryOn
     * @apiGroup commodity
     *
     * @apiParam {String} commodityId The id of the commodity.
     * @apiSuccessExample {json} Success-Response:
     * {
     *
     * }
     * @apiError {Msg} 1 Lack input.
     * @apiError {Msg} 2 Input is empty string or whitespaces.
     * @apiError {Msg} 3 Commodity id is not legal integer format.
     * @apiError {Msg} 4 Commodity not found.
     */
    @Before(GET.class)
    @ValidatePara(value = "commodityId", validators = {NullValidator.class, EmptyStringValidator.class, IntegerFormatValidator.class, CommodityRecordExistValidator.class})
    public void tryOn() {

    }

    /**
     * @api {get} /commodity/detail Get the detailed information of the commodity
     * @apiName detail
     * @apiGroup commodity
     *
     * @apiParam {String} commodityId The id of the commodity.
     * @apiSuccessExample {json} Success-Response:
     * {
     *     "id": 1,
     *     "name": String,
     *     "desc": String,
     *     "desc_img": url,
     *     "model_url": url,
     *     "shop_url": url,
     *     "category_id": int,
     *     "brand_id": int,
     *     "tp_shop_id": int
     * }
     *
     * @apiError {Msg} 1 Lack input.
     * @apiError {Msg} 2 Input is empty string or whitespaces.
     * @apiError {Msg} 3 Commodity id is not legal integer format.
     * @apiError {Msg} 4 Commodity not found.
     */
    public void detail() {

    }

    /**
     * @api {post} /commodity/checkIsFavorite Check if the commodity is favorited by the user
     * @apiName checkIsFavorite
     * @apiGroup commodity
     *
     * @apiParam {String} commodityId The id of the commodity.
     * @apiParam {String} userId The id of the user.
     * @apiSuccessExample {json} Success-Response:
     * {
     *     "favorited": boolean
     * }
     *
     * @apiError {Msg} 1 Lack input.
     * @apiError {Msg} 2 Input is empty string or whitespaces.
     * @apiError {Msg} 3 Id is not legal integer format.
     * @apiError {Msg} 4 Commodity not found.
     * @apiError {Msg} 4 User not found.
     */
    public void checkIsFavorite() {

    }

    /**
     * @api {post} /commodity/addFavorite Add the commodity to the user's favorite list
     * @apiName addFavorite
     * @apiGroup commodity
     *
     * @apiParam {String} commodityId The id of the commodity.
     * @apiParam {String} userId The id of the user.
     * @apiSuccessExample {json} Success-Response:
     * {
     *     "msg": "This commodity is successfully added to the user's favorite."
     * }
     *
     * @apiError {Msg} 1 Lack input.
     * @apiError {Msg} 2 Input is empty string or whitespaces.
     * @apiError {Msg} 3 Id is not legal integer format.
     * @apiError {Msg} 4 Commodity not found.
     * @apiError {Msg} 4 User not found.
     * @apiError {Msg} 5 Duplicate record.
     */
    public void addFavorite() {

    }

    /**
     * @api {post} /commodity/delFavorite Delete the commodity from the user's favorite list
     * @apiName delFavorite
     * @apiGroup commodity
     *
     * @apiParam {String} commodityId The id of the commodity.
     * @apiParam {String} userId The id of the user.
     * @apiSuccessExample {json} Success-Response:
     * {
     *     "msg": "This commodity is successfully deleted from the user's favorite."
     * }
     *
     * @apiError {Msg} 1 Lack input.
     * @apiError {Msg} 2 Input is empty string or whitespaces.
     * @apiError {Msg} 3 Id is not legal integer format.
     * @apiError {Msg} 4 Commodity not found.
     * @apiError {Msg} 5 User not found.
     * @apiError {Msg} 6 Record not exist.
     */
    public void delFavorite() {
        
    }

    /**
     * @api {post} /commodity/addViewing Add the commodity to the user's viewing history
     * @apiName addViewing
     * @apiGroup commodity
     *
     * @apiParam {String} commodityId The id of the commodity.
     * @apiParam {String} userId The id of the user.
     * @apiSuccessExample {json} Success-Response:
     * {
     *     "msg": "This commodity is successfully added to the user's viewing history."
     * }
     *
     * @apiError {Msg} 1 Lack input.
     * @apiError {Msg} 2 Input is empty string or whitespaces.
     * @apiError {Msg} 3 Id is not legal integer format.
     * @apiError {Msg} 4 Commodity not found.
     * @apiError {Msg} 4 User not found.
     */
    public void addViewing() {

    }
}
