package validators;

import models.Commodity;

/**
 * @author Jieying Xu
 */
public class CommodityRecordExistValidator extends AbstractValidator {
    @Override
    public boolean validate(Object input) {
        String commodityId = (String)input;
        return Commodity.dao.findById(commodityId) != null;
    }

    @Override
    public String getErrorMsg(String inputName) {
        return "Commodity not found!";
    }
}
