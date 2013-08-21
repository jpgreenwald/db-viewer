package com.swsandbox.json;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jgreenwald
 * Date: 8/11/13
 * Time: 10:53 AM
 */
public class JsonUtil
{
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private static ObjectMapper mapper;

    static
    {
        mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        mapper.configure( SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static Map<String, Object> getMap(String data)
    {
        try
        {
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>()
            {
            };
            return mapper.readValue(data, typeRef);
        }
        catch (IOException e)
        {
            logger.error("unable to process json map", e);
            return new HashMap();
        }
    }

    public static String prettyPrint(Object obj)
    {
        try
        {
            return mapper.writeValueAsString(obj);
        }
        catch (IOException e)
        {
            logger.error("unable to pretty print {}", obj, e);
            return obj.toString();
        }
    }

}
