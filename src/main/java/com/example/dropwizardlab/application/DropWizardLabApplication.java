package com.example.dropwizardlab.application;

import com.example.dropwizardlab.configuration.HelloWorldConfiguration;
import com.example.dropwizardlab.health.TemplateHealthCheck;
import com.example.dropwizardlab.resources.HelloWorldResource;
import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.reader.ClassReaders;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * Created by Alison on 24/01/15
 */
public class DropWizardLabApplication extends Application<HelloWorldConfiguration> {

    //final static Logger logger = LoggerFactory.getLogger(HelloWorldApplication.class);

    public static void main(String[] args) throws Exception {
        new DropWizardLabApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
/*        AssetsBundle bundle = new AssetsBundle("/html", "/","coffee.html");
        bootstrap.addBundle(bundle);*/
    }

    @Override
    public void run(HelloWorldConfiguration configuration, Environment environment) throws Exception {
        // Bug in 0.7.0 http://stackoverflow.com/questions/24822939/dropwizard-serving-assets-help-needed
        // need to set path to / in config
        environment.jersey().setUrlPattern("/api/*");

        environment.jersey().register(new HelloWorldResource(configuration.getTemplate(), configuration.getDefaultName()));

        environment.healthChecks().register("template", new TemplateHealthCheck(configuration.getTemplate()));

        configureSwagger(environment);
        configureCors(environment);
    }


    void configureSwagger(Environment environment) {
        environment.jersey().register(new ApiListingResourceJSON());
        environment.jersey().register(new ApiDeclarationProvider());
        environment.jersey().register(new ResourceListingProvider());
        ScannerFactory.setScanner(new DefaultJaxrsScanner());
        ClassReaders.setReader(new DefaultJaxrsApiReader());
        SwaggerConfig config = ConfigFactory.config();
        config.setApiVersion("0.0.1");
        config.setBasePath(environment.getApplicationContext().getContextPath());
    }

    private void configureCors(Environment environment) {
        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        filter.setInitParameter("allowCredentials", "true");
    }
}
