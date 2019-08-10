package com.clarity.claritydispatcher.service;
import javax.inject.Named;
import java.lang.annotation.*;

@Documented
@Named
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface CommandPipeline {
}
