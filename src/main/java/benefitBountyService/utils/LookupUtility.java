package benefitBountyService.utils;

import org.springframework.data.mongodb.core.aggregation.LookupOperation;

public class LookupUtility {

    public static LookupOperation getLookupOperation(String from , String localField, String foreignField, String as) {
        return LookupOperation.newLookup()
                .from(from)
                .localField(localField)
                .foreignField(foreignField)
                .as(as);
    }

}
