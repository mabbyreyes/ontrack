## Description
onTrack is an application for people trying to live in the moment. 
This app helps keep you on top of your studying by keeping you off what distracts you the most, your phone. As the set time goes by, Ontrack begins creating a complete puzzle piece. However, if you choose to leave the app, your puzzle piece becomes nothing more than scrunched paper. The determination to complete the puzzle of every stunning downloadable/sharable image of Albuquerque NM will motivate you to spend your time elsewhere, create better studying habits, and stay off your phone.

## Intended users
* Teenagers who spend too much time on their phone procrastinating.
* Student trying to create better study habits.
* Families wanting to spend more quality time together.

[User stories](user-stories.md)

## Design Documentation
[WireFrame Diagram](wireframe.md)

[Entity-relationship diagram](erd.md)

## External services
* [Free ABQ images](https://freeabqimages.com)
    * Pull from the website free high quality pictures of Albuquerque for the puzzle pieces. It be able to function without full-time access to this service as it only needs to pull an image at a time.
* [Google play services](https://developers.google.com/identity/sign-in/web/sign-in)
    * Integrate Google Sign-In for user to save pieces so when they log in, the progress and timeline populate on application. It would not function without constant connection to this service because the user would lose all data and notification preferences.
* [Image to puzzle](https://www.jigsawplanet.com/api/oembed/doc)
    * Use website as tool to generate any given photo to a puzzle and add API to application. It would not be able to function without continuous access to this service for the reason of needed puzzle pieces . 
    
## [Implementation](implementation.md)