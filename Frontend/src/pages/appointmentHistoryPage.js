import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import AppointmentClient from "../api/appointmentClient";

/**
 * Logic needed for the view playlist page of the website.
 */

class AppointmentHistoryPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['renderAppointment', 'onGetAppointmentById', 'onGetAllAppointments'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */

    async mount() {
        this.client = new AppointmentClient();
        document.getElementById('get-appointment-by-id-form').addEventListener('submit', this.onGetAppointmentById);
        document.getElementById('get-all-appointments-form').addEventListener('submit', this.onGetAllAppointments);
        this.dataStore.addChangeListener(this.renderAppointment)
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderAppointment() {

        let resultArea = document.getElementById("result-info");

        const appointmentRecord = this.dataStore.get("appointmentRecord");
        const appointmentList = this.dataStore.get("appointmentList");

        if(appointmentRecord) {
            resultArea.innerHTML = `
                <h2>Appointment Details</h2>
                <div>ID: ${appointmentRecord.appointmentId}</div>
                <div>Patient Name: ${appointmentRecord.patientFirstName} ${appointmentRecord.patientLastName}</div>
                <div>Provider Name: ${appointmentRecord.providerName}</div>
                <div>Gender: ${appointmentRecord.gender}</div>
                <div>Appointment Date: ${appointmentRecord.appointmentDate}</div>
                <div>Appointment Time: ${appointmentRecord.appointmentTime}</div>
            `;
        } else if (appointmentList) {
            resultArea.innerHTML = '<h2>All Appointments</h2>';
            appointmentList.forEach(appointment => {
                resultArea.innerHTML += `
                    <div>ID: ${appointment.appointmentId}</div>
                    <div>Patient Name: ${appointment.patientFirstName} ${appointment.patientLastName}</div>
                    <div>Provider Name: ${appointment.providerName}</div>
                    <div>Gender: ${appointment.gender}</div>
                    <div>Appointment Date: ${appointment.appointmentDate}</div>
                    <div>Appointment Time: ${appointment.appointmentTime}</div>
                    <hr>
                `;
            });
        } else {
            resultArea.innerHTML = "No Appointments Found";
        }
    }


    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGetAppointmentById(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        const appointmentId = document.getElementById("find-appointmentId").value;

        try {
            if (!appointmentId) throw new Error("Invalid input. Please provide an ID");
            const appointmentRecord = await this.client.getAppointmentById(appointmentId, this.errorHandler);
            this.dataStore.set("appointmentRecord", appointmentRecord);
            this.dataStore.set("appointmentList", null);
            this.showMessage(`Appointment found for ID: ${appointmentId}`);
        } catch (error) {
            this.errorHandler("Error retrieving appointment! Try again...");
        }
    }


    async onGetAllAppointments(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        try {
            const appointmentList = await this.client.getAllAppointments(this.errorHandler);
            this.dataStore.set("appointmentList", appointmentList);
            this.dataStore.set("appointmentRecord", null);
            this.showMessage(`Appointments retrieved successfully!`);
        } catch (error) {
            this.errorHandler("Error retrieving appointments! Try again...");
        }
    }
}


/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const appointmentHistoryPage = new AppointmentHistoryPage();
    appointmentHistoryPage.mount();
};

window.addEventListener('DOMContentLoaded', main);