package com.kenzie.capstone.service.dependency;

import com.kenzie.capstone.service.LambdaService;

import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {ServiceModule.class, DaoModule.class})
public interface ServiceComponent {
    LambdaService provideLambdaService();
}
