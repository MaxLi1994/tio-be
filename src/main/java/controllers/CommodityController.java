package controllers;

/**
 * @author Jieying Xu
 */
public class CommodityController extends BaseController {

    /**
     * @api {get} /commodity/browseAllProducts Show all commodity records in the database
     * @apiName browseAllProducts
     * @apiGroup commodity
     *
     * @apiSuccessExample {json} Success-Response:
     * {
     *
     * }
     * @apiError {Msg} 1 No record in the database.
     */
    public void browseAllProducts() {


    }

    /**
     * @api {post} /user/browseCategory Show all the commodities in that category
     * @apiName broseCategory
     * @apiGroup commodity
     *
     * @apiParam {String} categoryName The name of the category.
     * @apiSuccessExample {json} Success-Response:
     * {
     *
     * }
     * @apiError {Msg} 1 Lack input.
     * @apiError {Msg} 2 Input is empty string or whitespaces.
     * @apiError {Msg} 3 Category not found in the database.
     * @apiError {Msg} 4 No commodity record of that category in the database.
     */
    public void browseCategory() {

    }
}
