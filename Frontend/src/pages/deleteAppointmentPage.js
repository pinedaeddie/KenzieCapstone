import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import AppointmentClient from "../api/appointmentClient";

/**
 * Logic needed for the view playlist page of the website.
 */

class DeleteAppointmentPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['renderResult', 'onDeleteAppointmentById'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */

    async mount() {
        this.client = new AppointmentClient();
        document.getElementById('delete-appointment-form').addEventListener('submit', this.onDeleteAppointmentById);
        this.dataStore.addChangeListener(this.renderResult)
    }


    // Render Methods --------------------------------------------------------------------------------------------------

    async renderResult() {
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
            resultArea.innerHTML = `<span style="font-size: 1.5em; font-weight: bold; font-style: italic;"> No Appointment Found to Delete </span>`;
        }
    }


    // Event Handlers --------------------------------------------------------------------------------------------------

    async onDeleteAppointmentById(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        const appointmentId = document.getElementById('appointment-id').value.trim();

        if (!appointmentId) {
            this.errorHandler("Invalid input. Please provide a valid appointment ID.");
            this.errorHandler("Error deleting appointment! Try again...");
            return;
        }

        this.showMessage(`Request successfully submitted for deletion, please wait..`);
        try {
            const deletedAppointment = await this.client.deleteAppointmentById(appointmentId);
            this.dataStore.set('appointmentRecord', deletedAppointment);

            if (deletedAppointment) {
                this.showMessage(`Appointment successfully deleted for ${deletedAppointment.patientFirstName}!`);
            } else {
                this.errorHandler("Error deleting appointment! Try again...");
            }
        } catch (error) {
            if (error.response && error.response.status == 400) {
                this.errorHandler("Invalid input. Please provide a valid appointment ID.");
            } else {
                this.errorHandler("Error deleting appointment! Try again...");
            }
        }
    }
}



/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const deleteAppointmentPage = new DeleteAppointmentPage();
    deleteAppointmentPage.mount();
};

window.addEventListener('DOMContentLoaded', main);