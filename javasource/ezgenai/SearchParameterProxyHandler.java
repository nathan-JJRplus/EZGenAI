package ezgenai;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;

import ezgenai.proxies.SearchParameter;
import ezgenai.proxies.SearchParameterBoolean;
import ezgenai.proxies.SearchParameterNumerical;

public class SearchParameterProxyHandler {

    public static List<IMendixObject> importSearchParameters(IContext context, Map<String, Object> searchParameters) {
        List<IMendixObject> searchParameterList = new ArrayList<>();

        for (String key : searchParameters.keySet()) {
            Object value = searchParameters.get(key);

            if (value instanceof Boolean) {
                java.lang.Boolean valueBoolean = (java.lang.Boolean) value;
                SearchParameterBoolean searchParameterBoolean = new SearchParameterBoolean(context);
                searchParameterBoolean.setKey(context, key);
                searchParameterBoolean.setValue(valueBoolean);
                searchParameterList.add(searchParameterBoolean.getMendixObject());
            } else {
                SearchParameterNumerical searchParameterNumerical = new SearchParameterNumerical(context);
                searchParameterNumerical.setKey(context, key);
                searchParameterNumerical.setValue(new BigDecimal(value.toString()));
                searchParameterList.add(searchParameterNumerical.getMendixObject());
            }

        }

        return searchParameterList;
    }

    public static Map<String, Object> exportSearchParameters(List<SearchParameter> searchParameters) {
        Map<String, Object> searchParameterList = new HashMap<>();

        for (SearchParameter searchParameter : searchParameters) {
            if (searchParameter instanceof SearchParameterBoolean) {
                SearchParameterBoolean searchParameterBoolean = (SearchParameterBoolean) searchParameter;
                searchParameterList.put(searchParameter.getKey(), searchParameterBoolean.getValue());
            }
            if (searchParameter instanceof SearchParameterNumerical) {
                SearchParameterNumerical searchParameterNumerical = (SearchParameterNumerical) searchParameter;
                searchParameterList.put(searchParameter.getKey(), searchParameterNumerical.getValue().doubleValue());
            }
        }

        return searchParameterList;
    }
}
