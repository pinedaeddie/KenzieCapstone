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
                <div style="font-size: 1.4em; font-style: italic;"><strong>ID:</strong> ${appointmentRecord.appointmentId}</div>
                <div style="font-size: 1.4em; font-style: italic;"><strong>Patient Name:</strong> ${appointmentRecord.patientFirstName}</div>
                <div style="font-size: 1.4em; font-style: italic;"><strong>Patient Last Name:</strong> ${appointmentRecord.patientLastName}</div>
                <div style="font-size: 1.4em; font-style: italic;"><strong>Provider Name:</strong> ${appointmentRecord.providerName}</div>
                <div style="font-size: 1.4em; font-style: italic;"><strong>Gender:</strong> ${appointmentRecord.gender}</div>
                <div style="font-size: 1.4em; font-style: italic;"><strong>Appointment Date:</strong> ${appointmentRecord.appointmentDate}</div>
                <div style="font-size: 1.4em; font-style: italic;"><strong>Appointment Time:</strong> ${appointmentRecord.appointmentTime}</div>
            `;
        } else {
            resultArea.innerHTML = `<span style="font-size: 1.5em; font-weight: bold; font-style: italic;"> No Appointment Details Available </span>`;
        }
    }


    // Event Handlers --------------------------------------------------------------------------------------------------

    async onCreateAppointment(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        const patientFirstName = document.getElementById('first-name').value.trim();
        const patientLastName = document.getElementById('last-name').value.trim();
        const providerName = document.getElementById('provider-name').value.trim();
        const gender = document.getElementById('gender').value.trim();
        const appointmentDate = document.getElementById('appointment-date').value.trim();
        const appointmentTime = document.getElementById('appointment-time').value.trim();

        if (!patientFirstName || !patientLastName || !providerName || !gender || !appointmentDate || !appointmentTime) {
            this.errorHandler("All fields must be filled out.");
            this.errorHandler("Error creating appointment! Try again...");
            return;
        }

        const request = {
            patientFirstName,
            patientLastName,
            providerName,
            gender,
            appointmentDate,
            appointmentTime
        };

        this.showMessage(`Request successfully submitted, please wait..`);
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