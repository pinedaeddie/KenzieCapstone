// File path: src/main/java/com/kenzie/capstone/service/lambda/UpdateAppointmentLambda.java

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
import java.util.Map;

public class UpdateAppointmentLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Inject
    AppointmentService appointmentService;

    public UpdateAppointmentLambda() {
        DaggerServiceComponent.create().inject(this);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        Map<String, String> body = JsonUtils.jsonToMap(input.getBody());
        String appointmentId = input.getPathParameters().get("appointmentId");
        String newDetails = body.get("newDetails");

        Appointment appointment = appointmentService.updateAppointment(appointmentId, newDetails);
        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(JsonUtils.objectToJson(appointment));
    }
}
