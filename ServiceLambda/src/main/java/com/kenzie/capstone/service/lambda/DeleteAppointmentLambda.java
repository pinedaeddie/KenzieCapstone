// File path: src/main/java/com/kenzie/capstone/service/lambda/DeleteAppointmentLambda.java

package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.kenzie.capstone.service.AppointmentService;
import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import com.kenzie.capstone.service.util.JsonUtils;

import javax.inject.Inject;

public class DeleteAppointmentLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Inject
    AppointmentService appointmentService;

    public DeleteAppointmentLambda() {
        DaggerServiceComponent.create().inject(this);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        String appointmentId = input.getPathParameters().get("appointmentId");
        appointmentService.deleteAppointment(appointmentId);
        return new APIGatewayProxyResponseEvent().withStatusCode(204);
    }
}
