# android_network
Android application to notify users whenever there is a change in network

## Change in network means:
* Any change in network connectivity (eg. Wifi -> Mobile data)
* Any change in connection state (eg. Connected -> Disconnected)

## How it works
* When user first opens the app, a new service will be created.
* The service will register the braodcast receiver that will capture any signal for change in network.
* When the braodcast receiver detects a change in network, it will send a notification to the user on the device's new network connection status.