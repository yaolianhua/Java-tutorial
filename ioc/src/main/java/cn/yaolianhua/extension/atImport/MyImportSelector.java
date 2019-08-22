package cn.yaolianhua.extension.atImport;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-19 16:53
 **/
public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{MyImportBeanDefinitionRegistrar.class.getName()};
    }
}
