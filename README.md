Team 12 MemeUps

Kerry Ferguson
Travis Bain
Dirk Sexton


Fixed bugs:
On back press bugs fixed
Quiz blank answer bug fixed
Typos fixed
Class diagram fixed


We fixed the bugs addressed by the Prof and class, and also fixed the ones we found along the way. We also fixed things that were not necessarily bugs, such as, having hints on the email and password fields for example. Remaining bugs include our php files not properly being able to parse ‘ and “ which can cause errors with mySQL.


High and medium user stories:
As a user, I want to match with other users, so I can find relationships. 
Priority Level = High COMPLETE (Match with other users on View Profile page)
As a user, I wish to take a meme personality quiz, so I can be accurately matched with others. 
Priority Level = High COMPLETE (Meme knowledge quiz, results stored for matching)
As a user, I wish to view other users with similar preferences, so I can match with them. Priority Level = High COMPLETE (Find matches with users with the same score)
As a user, I want to message other users, so I can contact my matches and converse. Priority Level = High COMPLETE (Send emails to matched users)
As a user, I want to maintain a profile page, so that other users can view it. 
Priority Level = High COMPLETE (Edit Profile page, results saved in database)
As a user, I want to share my memes so other users can view them.
Priority Level = Medium COMPLETE (Displays “Favorite Meme” on profiles)
As a user, I want to favorite memes, so that I can view them later 
Priority Level = Medium NOT IMPLEMENTED (Not enough time)
As an administrator, I want to send updates to users, so I can display recent trending memes. 
Priority Level = Medium COMPLETE (Meme of the day can be updated with DB)
As a user, I want to have a feed of memes, so I can scroll through the list to view them. Priority Level = Low NOT IMPLEMENTED (Beyond scope of our project, uploading imgs)
As a user, I want to be shown recent popular memes, so I can stay up to date. Priority Level = Low COMPLETE (Meme of the day displayed on home screen)
As a user, I want a place to edit my memes live, so I can create new memes in-app. Priority Level = Low NOT IMPLEMENTED (Beyond scope of our project)
As a user, I want to have location services, so I can meet other local users. Priority Level = Low NOT IMPLEMENTED

Saved data:
We used SQLite to save the list of user’s matches. After taking the quiz, the list of matches will populate. They can then turn on airplane mode, back out, and go back in to have the list be populated by a locally stored database.
We used SharedPreferences to keep track if the user logged in so that they don’t have to keep logging in when they open the app. When logging in, we also store the current user’s email address in SharedPreferences, which is used throughout the application to specify which user’s data should be pulled from the database. Examples of this are in MyProfileActivity, MatchActivity, ViewMatchActivity, and every other instance the user’s data is pulled from the database. In addition, we use this stored email to share which user is sending in email when contacting a matched user through an email application.

Content sharing:
Users can share their meme score to their favorite social media or messaging app on the results page of their quiz. Doing this will send a default message that can be edited.
Users can add a message inside the application and then send it as an email to their match with the email “to:”, subject, and body being automatically filled.

Graphics:
The app has a custom logo and icon. We also implemented the open source, Apache-licenced Picasso library to load images from urls and display them around the application as Display Pictures, Favorite Memes, and Meme of the Day.

JUnit/Instrumentation test:
JUnit: User.java
    Wrote unit tests to validate email and password information, tested getters and setters, and tested the method that parses a Json String for user data.
Instrumentation: HomeScreenActivity.java
    Wrote tests that check to make sure you can log in, view the home screen page, properly use the four buttons that take you to the rest of the main activities, and logout to return to the login page.



