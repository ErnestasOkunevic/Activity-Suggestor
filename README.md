# Capstone project for android app development course

## App name: **Activity Suggestor**
 
#### The url: **https://www.boredapi.com**
**-------------------------------------------------------------------------------------------------------------------------------------------------**
## Description:
#### Controls (for browsing):
* Swipe left to skip suggestion;
* Swipe right to save suggestion;

##### This app will allow the user to save or skip the random activities suggested to them from the "bored api".
There will be four activities:
* The **MainActivity** (main menu);
* The **BrowseActivity** (for browsing through suggestions from the api);
* The **ViewSavedActivity** (for checking out the users saved suggestions);
* The **OnPressedActivity** (for when the user presses on a saved suggestion, opens a whole activity with information about the pressed suggestion);

There will be one broadcast receiver:
* The **MyDatabaseReceiver** (broadcast will be sent from the BrowseActivity and the BrowseActivity to start the database service);


There will be one service:
* The **MyDatabaseService** (adds or removes suggestions to/from the database);

There will be one content provider:
* The **ActivityDBHelper** (for interaction with the database, all column and table names stored in the ActivitySuggestorTable class);