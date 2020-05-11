package com.stephen.learning.blade.scan;

import com.stephen.learning.blade.annotation.RpcService;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Arrays;
import java.util.Set;

/**
 * @Author: jack
 * @Description:
 * @Date: 2020/5/11 12:44
 * @Version: 1.0
 */
public class RpcScanner extends ClassPathBeanDefinitionScanner {
    public RpcScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            logger.warn("No rpcService was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            for (BeanDefinitionHolder holder : beanDefinitions) {
                GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();

                // RpcFactoryBean是工厂bean，用来产生rpcInterface的代理
                definition.getPropertyValues().add("rpcInterface", definition.getBeanClassName());
                definition.setBeanClass(RpcFactoryBean.class);

                boolean explicitFactoryUsed = false;
                if (!explicitFactoryUsed) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Enabling autowire by type for RpcFactoryBean with name '" + holder.getBeanName() + "'.");
                    }
                    definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                }
            }
        }

        return beanDefinitions;
    }

    /**
     * 扫描bean的Filter，默认扫描 package 下面有 @RpcService 标记的类
     */
    public void registerFilters() {
        addIncludeFilter(new AnnotationTypeFilter(RpcService.class));
    }

    /**
     * 只处理接口类型的
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return (beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent());
    }

}
