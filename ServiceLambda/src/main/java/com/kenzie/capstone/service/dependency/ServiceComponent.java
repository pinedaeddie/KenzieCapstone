// File path: src/main/java/com/kenzie/capstone/service/dependency/ServiceComponent.java

package com.kenzie.capstone.service.dependency;

import com.kenzie.capstone.service.LambdaService;
import com.kenzie.capstone.service.lambda.*;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ServiceModule.class, DaoModule.class})
public interface ServiceComponent {
    LambdaService provideLambdaService();

    void inject(ScheduleAppointmentLambda scheduleAppointmentLambda);
    void inject(ManageAppointmentScheduleLambda manageAppointmentScheduleLambda);
    void inject(DeleteAppointmentLambda deleteAppointmentLambda);
    void inject(UpdateAppointmentLambda updateAppointmentLambda);
    // Add other inject methods as needed
}
