package web.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import web.config.filter.TrailingSlashRedirectFilter;

import javax.servlet.Filter;

//@Order(110)
public class ApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{
                ServletConfiguration.class,
        };
    }

    @Override
    public Filter[] getServletFilters() {
        return new Filter[]{
                new TrailingSlashRedirectFilter(),
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
