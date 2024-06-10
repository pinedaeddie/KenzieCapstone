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

public class ScheduleAppointmentLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Inject
    AppointmentService appointmentService;

    public ScheduleAppointmentLambda() {
        DaggerServiceComponent.create().inject(this);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        Map<String, String> body = JsonUtils.jsonToMap(input.getBody());
        String userId = body.get("userId");
        String appointmentDetails = body.get("appointmentDetails");

        Appointment appointment = appointmentService.scheduleAppointment(userId, appointmentDetails);
        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(JsonUtils.objectToJson(appointment));
    }
}
