package com.example.dropwizardlab.health;

import com.codahale.metrics.health.HealthCheck;

/**
 * Created by Alison on 24/01/15
 */
public class TemplateHealthCheck extends HealthCheck{
    private String template;

    public TemplateHealthCheck(String template) {
        this.template = template;
    }

    @Override
    protected Result check() throws Exception {
        // Normally do things like check database connection, check other services can be connected to.
        try {
            final String saying = String.format(template, "TEST");
            if(!saying.contains("TEST")){
                return Result.unhealthy("template doesn't include a name");
            }
            return Result.healthy();
        } catch (Exception e) {
            return Result.unhealthy("error", e);
        }
    }
}
