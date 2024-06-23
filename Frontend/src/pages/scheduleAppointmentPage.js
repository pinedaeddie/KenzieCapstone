import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import AppointmentClient from "../api/appointmentClient";

/**
 * Logic needed for the view playlist page of the website.
 */

class ScheduleAppointmentPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['renderAppointment', 'onCreateAppointment'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */

    async mount() {
        this.client = new AppointmentClient();
        document.getElementById('schedule-appointment-form').addEventListener('submit', this.onCreateAppointment);
        this.dataStore.addChangeListener(this.renderAppointment)
    }


    // Render Methods --------------------------------------------------------------------------------------------------

    async renderAppointment() {
        let resultArea = document.getElementById("result-info");
        const appointmentRecord = this.dataStore.get("appointmentRecord");

        if (appointmentRecord) {
            resultArea.innerHTML = `
                <h2>Appointment Details</h2>
                <div>ID: ${appointmentRecord.appointmentId}</div>
                <div>Patient Name: ${appointmentRecord.patientFirstName} ${appointmentRecord.patientLastName}</div>
                <div>Provider Name: ${appointmentRecord.providerName}</div>
                <div>Gender: ${appointmentRecord.gender}</div>
                <div>Appointment Date: ${appointmentRecord.appointmentDate}</div>
                <div>Appointment Time: ${appointmentRecord.appointmentTime}</div>
            `;
        } else {
            resultArea.innerHTML = "No Appointment Details Available";
        }
    }


    // Event Handlers --------------------------------------------------------------------------------------------------

    async onCreateAppointment(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        const request = {
            patientFirstName: document.getElementById('first-name').value,
            patientLastName: document.getElementById('last-name').value,
            providerName: document.getElementById('provider-name').value,
            gender: document.getElementById('gender').value,
            appointmentDate: document.getElementById('appointment-date').value,
            appointmentTime: document.getElementById('appointment-time').value
        };

        try {
            const createdAppointment = await this.client.createAppointment(request, this.errorHandler);
            this.dataStore.set('appointmentRecord', createdAppointment);

            if (createdAppointment) {
                this.showMessage(`Appointment successfully created for ${createdAppointment.patientFirstName}!`);
            } else {
                this.errorHandler("Error creating appointment! Try again...");
            }
        } catch (error) {
            this.errorHandler("Error creating appointment! Try again...");
        }
    }
}



/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const scheduleAppointmentPage = new ScheduleAppointmentPage();
    scheduleAppointmentPage.mount();
};

window.addEventListener('DOMContentLoaded', main);