package controllers;

import annotations.ValidatePara;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.GET;
import models.Category;
import models.Commodity;
import validators.CategoryRecordExistValidator;
import validators.EmptyStringValidator;
import validators.NullValidator;

import java.util.List;

/**
 * @author Jieying Xu
 */
public class CommodityController extends BaseController {

    /**
     * @api {get} /commodity/browseAllCommodities Display all commodities in the database
     * @apiName browseAllCommodities
     * @apiGroup commodity
     *
     * @apiSuccessExample {json} Success-Response:
     *  {
     *      "code": 0,
     *      "data": [
     *          {
     *              "name": "Dior999",
     *              "id": 1,
     *              "desc_img": "www.exampleImageUrl.com"
     *          },
     *          {
     *              "name": "YSL102",
     *              "id": 2,
     *              "desc_img": "www.exampleImageUrl.com"
     *          }
     *      ]
     *  }
     * @apiError {Msg} 1 No record in the database.
     */
    @Before(GET.class)
    public void browseAllCommodities() {
        List<Commodity> allCommodities = Commodity.dao.find("select id, desc_img, name from commodity");
        if (allCommodities.size() == 0) {
            errorResponse("No commodities in database");
        } else {
            successResponse(allCommodities);
        }
    }

    /**
     * @api {get} /commodity/browseCategory Show all commodities in that category
     * @apiName broseCategory
     * @apiGroup commodity
     *
     * @apiParam {String} categoryName The name of the category.
     * @apiSuccessExample {json} Success-Response:
     *  {
     *      "code": 0,
     *      "data": [
     *          {
     *              "name": "Dior999",
     *              "id": 1,
     *              "desc_img": "www.exampleImageUrl.com"
     *          },
     *          {
     *              "name": "YSL102",
     *              "id": 2,
     *              "desc_img": "www.exampleImageUrl.com"
     *          }
     *      ]
     *  }
     * @apiError {Msg} 1 Lack input.
     * @apiError {Msg} 2 Input is empty string or whitespaces.
     * @apiError {Msg} 3 Category not found in the database.
     * @apiError {Msg} 4 No commodity record of that category in the database.
     */
    @Before(GET.class)
    @ValidatePara(value = "categoryName", validators = {NullValidator.class, EmptyStringValidator.class, CategoryRecordExistValidator.class})
    public void browseCategory() {
        String name = getPara("categoryName");
        int categoryId = Category.dao.findFirst("select * from category where name = ?", name).getInt("id");
        List<Commodity> data = Commodity.dao.find("select id, desc_img, name from commodity where category_id = ?", categoryId);
        if (data.size() == 0) {
            errorResponse("No commodity in category: " + name);
        } else {
            successResponse(data);
        }
    }
}
