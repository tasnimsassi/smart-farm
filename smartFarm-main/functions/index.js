/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

const {onRequest} = require("firebase-functions/v2/https");
const logger = require("firebase-functions/logger");


const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendTemperatureNotification = functions.firestore
  .document('data/LJR1TlDHAGFHlPsF2WSi')
  .onUpdate((change, context) => {
    const newValue = change.after.data();
    const previousValue = change.before.data();

    const newTemperature = newValue.temperature;

    if (newTemperature > 15 && newTemperature !== previousValue.temperature) {
      const payload = {
        notification: {
          title: 'Temperature Alert',
          body: `Temperature is ${newTemperature}°C (exceeds 15°C)`,
        },
      };

      // Replace 'device_token' with the token of the device you want to send the notification to
      const deviceToken = 'eKBGp3B7RxSYCbsMfygZzR:APA91bF4MGPeSc16ikjIbBvBf3fXpkF0FnOK0sgpkzKr_0B9RfcCNkOlqTyHvMw-EVapo6nkMeP6wfrRv2HdAQPe-A2FZ5ouYfXicFf4AlQEywcKzT12yVmLMgcVFj1EF6GxtVuMz_W7';

      return admin.messaging().sendToDevice(deviceToken, payload);
    }

    return null;
  });



// Create and deploy your first functions
// https://firebase.google.com/docs/functions/get-started

// exports.helloWorld = onRequest((request, response) => {
//   logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });
