package cn.yaolianhua.extension.atImport;

import cn.yaolianhua.extension.annotation.MyProxy;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-19 17:38
 **/
public class MyImportBeanDefinitionRegistrar  implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes =
                AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(MyProxy.class.getName()));

        if (annotationAttributes != null)
        {
            Boolean value = (Boolean) annotationAttributes.get("value");
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition();
            GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanDefinitionBuilder.getBeanDefinition();
//            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(value.getClass());
            beanDefinition.setAttribute("proxy",value);
            beanDefinition.setBeanClass(ProxyCreator.class);

            registry.registerBeanDefinition("proxyCreator",beanDefinition);

        }


    }
}
