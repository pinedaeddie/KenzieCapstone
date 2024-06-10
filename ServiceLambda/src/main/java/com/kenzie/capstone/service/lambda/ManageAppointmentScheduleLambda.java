// File path: src/main/java/com/kenzie/capstone/service/lambda/ManageAppointmentScheduleLambda.java

package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.kenzie.capstone.service.AppointmentService;
import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import com.kenzie.capstone.service.model.Appointment;
import com.kenzie.capstone.service.util.JsonUtils;

import javax.inject.Inject;
import java.util.List;

public class ManageAppointmentScheduleLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Inject
    AppointmentService appointmentService;

    public ManageAppointmentScheduleLambda() {
        DaggerServiceComponent.create().inject(this);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        List<Appointment> appointments = appointmentService.getAppointments();
        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(JsonUtils.objectToJson(appointments));
    }
}
