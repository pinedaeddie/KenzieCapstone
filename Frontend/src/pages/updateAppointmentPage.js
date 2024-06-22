import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import AppointmentClient from "../api/appointmentClient";

/**
 * Logic needed for the view playlist page of the website.
 */

class UpdateAppointmentPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['renderAppointment', 'onUpdateAppointmentById'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */

    async mount() {
        this.client = new AppointmentClient();
        document.getElementById('update-appointment-form').addEventListener('submit', this.onUpdateAppointment);
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

    async onUpdateAppointmentById(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        const appointmentId = document.getElementById('appointment-id').value;
        const request = {
            patientFirstName: document.getElementById('first-name').value,
            patientLastName: document.getElementById('last-name').value,
            providerName: document.getElementById('provider-name').value,
            gender: document.getElementById('gender').value,
            appointmentDate: document.getElementById('appointment-date').value,
            appointmentTime: document.getElementById('appointment-time').value
        };

        try {
            const updatedAppointment = await this.client.updateAppointmentById(appointmentId, request, this.errorHandler);
            this.dataStore.set('appointmentRecord', updatedAppointment);

            if (updatedAppointment) {
                this.showMessage(`Appointment successfully updated for ${updatedAppointment.patientFirstName}!`);
            } else {
                this.errorHandler("Error updating appointment! Try again...");
            }
        } catch (error) {
            this.errorHandler("Error updating appointment! Try again...");
        }
    }
}


/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const updateAppointmentPage = new UpdateAppointmentPage();
    updateAppointmentPage.mount();
};

window.addEventListener('DOMContentLoaded', main);