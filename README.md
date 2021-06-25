# Cat facts app

## Features

- Allows interested users to learn new facts about cats.
- Each fact on a screen looks like a picture with a cat, under which there is text information, as well as the date when the fact was added.
- All facts are placed in a vertical scrollable feed.
- Initially, the user is shown a blank screen with an invitation to learn a new fact. 
- When you click on the button in the action bar, one random fact is loaded, as well as a random picture for this fact.
- A random image is taken from the service https://cataas.com/
- A random fact is taken from https://catfact.ninja/
- The next time you click, a new fact and image are loaded and added to the feed.

## Code

- Written only in Java
- Implemented MVVM architecture
- Use of Dependency Injection
- Use of RxJava
- Use of Material Design

## Added unit tests

Run unit tests using command:

```
$ ./gradlew test
```

## Offline mode

- All uploaded facts are saved to the database (using Room) and displayed from the database when there is no internet connection.

## Delete a cat fact

- User can delete a fact from the screen and from the database. You can do this through a long press on the corresponding fact.

