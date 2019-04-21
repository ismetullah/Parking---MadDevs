# Parking - MadDevs


This repository contains a prototype Android application for `Parking` developed as a task app for Mad Devs company.


# Walkthrough

*	When the app is opened, a splash screen will appear until Parking Zones are downloaded from assets in the background
* After the zones are downloaded, the Main activity will be opened with zones drawn on the Google Maps
* If a zone is clicked, a new fragment will appear containing details about the zone
* There is an activity to check the history of parkings. You can delete a parking event by sliding to left
* Another activity is for the list of parking zones

#Some features

* Design Pattern: `MVVM`
* Database: `Room Persistence Library`
* Dependency injection: `Dagger 2`
* `Data Binding` was also used in the application
* For location tracking `LocationTrackerActivity` service is used. (Mistake on the name of the class)
* Location tracking is used for two reasons
		[1] When a user reachs a parking zone, after some time the app asks to park in that zone
		[2] When a user leaves a parking zone, the app asks to stop parking
		
# Screenshots

<img src="/screenshots/1.png?raw=true" width="360" height="640" />
<img src="/screenshots/2.png?raw=true" width="360" height="640" />
<img src="/screenshots/3.png?raw=true" width="360" height="640" />
<img src="/screenshots/4.png?raw=true" width="360" height="640" />
