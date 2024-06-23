import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import AppointmentClient from "../api/appointmentClient";

/**
 * Logic needed for the view playlist page of the website.
 */

class DeleteAppointmentPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onDeleteAppointmentById'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */

    async mount() {
        this.client = new AppointmentClient();
        document.getElementById('delete-appointment-form').addEventListener('submit', this.onDeleteAppointment);
    }


    // Event Handlers --------------------------------------------------------------------------------------------------

    async onDeleteAppointmentById(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        const appointmentId = document.getElementById('appointment-id').value;

        try {
            await this.client.deleteAppointmentById(appointmentId, this.errorHandler);
            this.dataStore.set('deletedAppointmentId', appointmentId);
            this.showMessage(`Appointment with ID ${appointmentId} successfully deleted!`);
        } catch (error) {
            this.errorHandler("Error deleting appointment! Try again...");
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