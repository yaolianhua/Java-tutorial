package cn.yaolianhua.dependencies.javaBase;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-05 15:01
 **/
public class CustomCondition implements Condition {
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        Map<String, Object> attributes = metadata.getAnnotationAttributes(Flag.class.getName());
        if (attributes != null){
            Object value = attributes.get("value");
            return Boolean.parseBoolean(value.toString());
        }

        return true;
    }
}
