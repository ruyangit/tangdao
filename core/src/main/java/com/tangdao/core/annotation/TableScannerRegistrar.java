/**
 * 
 */
package com.tangdao.core.annotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2021年4月10日
 */
public class TableScannerRegistrar
		implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, BeanClassLoaderAware, EnvironmentAware {

	private static Map<String, Map<String, Object>> ANNATTR_NID_ENTITY_CACHE = new ConcurrentHashMap<>();

	private ResourceLoader resourceLoader;

	public TableScannerRegistrar() {
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
	}

	@Override
	public void setEnvironment(Environment environment) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		AnnotationAttributes tableTreeColumnScanAttrs = AnnotationAttributes
				.fromMap(importingClassMetadata.getAnnotationAttributes(TableScan.class.getName()));
		if (tableTreeColumnScanAttrs != null) {

			List<String> basePackages = new ArrayList<>();
			basePackages.addAll(Arrays.stream(tableTreeColumnScanAttrs.getStringArray("value"))
					.filter(StringUtils::hasText).collect(Collectors.toList()));

			basePackages.addAll(Arrays.stream(tableTreeColumnScanAttrs.getStringArray("basePackages"))
					.filter(StringUtils::hasText).collect(Collectors.toList()));

			if (basePackages.isEmpty()) {
				basePackages.add(getDefaultBasePackage(importingClassMetadata));
			}

			ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
			scanner.setResourceLoader(this.resourceLoader);
			scanner.addIncludeFilter(new AnnotationTypeFilter(Table.class));
			for (String basePackage : basePackages) {
				Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(basePackage);
				for (BeanDefinition beanDefinition : beanDefinitions) {
					if (beanDefinition instanceof AnnotatedBeanDefinition) {
						AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) beanDefinition;
						AnnotationMetadata annotationMetadata = annotatedBeanDefinition.getMetadata();

						String className = annotatedBeanDefinition.getBeanClassName();
						Map<String, Object> map = annotationMetadata
								.getAnnotationAttributes(Table.class.getName());
						if (map != null && map.size() > 0) {
							ANNATTR_NID_ENTITY_CACHE.put(className, map);
						}
					}
				}
			}
		}
	}

	private static String getDefaultBasePackage(AnnotationMetadata importingClassMetadata) {
		return ClassUtils.getPackageName(importingClassMetadata.getClassName());
	}

	public static Map<String, Object> getTable(Class<?> clazz) {
		return ANNATTR_NID_ENTITY_CACHE.get(clazz.getName());
	}

}
