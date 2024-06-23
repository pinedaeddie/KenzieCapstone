import BaseClass from "../util/baseClass";
import axios from 'axios'

/**
 * Client to call the MusicPlaylistService.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
 */
export default class AppointmentClient extends BaseClass {
    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'createAppointment', 'getAppointmentById', 'getAllAppointments', 'updateAppointmentById', 'deleteAppointmentById'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */

    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }

    async createAppointment(request, errorCallback) {
        try {
            const response = await this.client.post(`/appointments`,
                {
                    patientFirstName: request.patientFirstName,
                    patientLastName: request.patientLastName,
                    providerName: request.providerName,
                    gender: request.gender,
                    appointmentDate: request.appointmentDate,
                    appointmentTime: request.appointmentTime
                });
            return response.data;
        } catch (error) {
            this.handleError("createAppointment", error, errorCallback);
        }
    }

    async getAppointmentById(id, errorCallback) {
        try {
            const response = await this.client.get(`/appointments/${id}`);
            return response.data;
        } catch (error) {
            this.handleError("getAppointmentById", error, errorCallback);
        }
    }

    async getAllAppointments(errorCallback) {
            try {
                const response = await this.client.get(`/appointments/all`);
                return response.data;
            } catch (error) {
                this.handleError("getAllAppointments", error, errorCallback);
            }
        }

    async updateAppointmentById(id, request, errorCallback) {
        try {
            const response = await this.client.put(`/appointments/${id}`,
                {
                    patientFirstName: request.patientFirstName,
                    patientLastName: request.patientLastName,
                    providerName: request.providerName,
                    gender: request.gender,
                    appointmentDate: request.appointmentDate,
                    appointmentTime: request.appointmentTime
                });
            return response.data;
        } catch (error) {
            this.handleError("updateAppointmentById", error, errorCallback);
        }
    }

    async deleteAppointmentById(id, errorCallback) {
           try {
               const response = await this.client.delete(`/appointments/${id}`);
               return response.data;
           } catch (error) {
               this.handleError("deleteAppointmentById", error, errorCallback);
           }
       }




    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */

    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
}