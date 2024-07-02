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
        } else if (appointmentList) {
            appointmentList.forEach(appointment => {
                resultArea.innerHTML += `
                <div style="font-size: 1.4em; font-style: italic;"><strong>ID:</strong> ${appointment.appointmentId}</div>
                <div style="font-size: 1.4em; font-style: italic;"><strong>Patient Name:</strong> ${appointment.patientFirstName}</div>
                <div style="font-size: 1.4em; font-style: italic;"><strong>Patient Last Name:</strong> ${appointment.patientLastName}</div>
                <div style="font-size: 1.4em; font-style: italic;"><strong>Provider Name:</strong> ${appointment.providerName}</div>
                <div style="font-size: 1.4em; font-style: italic;"><strong>Gender:</strong> ${appointment.gender}</div>
                <div style="font-size: 1.4em; font-style: italic;"><strong>Appointment Date:</strong> ${appointment.appointmentDate}</div>
                <div style="font-size: 1.4em; font-style: italic;"><strong>Appointment Time:</strong> ${appointment.appointmentTime}</div>
                <br>
            `;
            });
        } else {
            resultArea.innerHTML = `<span style="font-size: 1.5em; font-weight: bold; font-style: italic;"> No Appointments Found </span>`;
        }
    }


    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGetAppointmentById(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        const appointmentId = document.getElementById("find-appointmentId").value.trim();

        if (!appointmentId) {
            this.errorHandler("Invalid input. Please provide a valid appointment ID.");
            this.errorHandler("Error retrieving appointment! Try again...");
            return;
        }

        this.showMessage("Retrieving Appointment, please wait..");
        try {
            const appointmentRecord = await this.client.getAppointmentById(appointmentId);
            if (appointmentRecord) {
                this.dataStore.set("appointmentRecord", appointmentRecord);
                this.showMessage(`Appointment found for ID: ${appointmentId}`);
            } else {
                this.dataStore.set("appointmentRecord", null);
                this.errorHandler("No appointment found with the given ID.");
            }
        } catch (error) {
            this.dataStore.set("appointmentRecord", null);
            this.errorHandler("Error retrieving appointment! Try again...");
        }
    }


    async onGetAllAppointments(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        this.showMessage("Retrieving all booked appointments! please wait..");
        try {
            const appointmentList = await this.client.getAllAppointments(this.errorHandler);
            if (appointmentList.length == 0) {
                throw new Error("No booked appointments available.");
            }
            this.dataStore.set("appointmentList", appointmentList);
            this.showMessage(`Appointments retrieved successfully!`);
        } catch (error) {
            this.dataStore.set("appointmentRecord", null);
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