# Overview

Modularization is the practice of breaking the concept of a monolithic, one-module codebase into
loosely coupled, self contained modules.

## Benefits of modularization

We use this approach because of this offers many benefits, including:
<table>
  <tr>
    <th>Aspect</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>Scalability</td>
    <td>In a tightly coupled codebase, a single change can trigger a cascade of alterations. A properly modularized project will embrace the <a href="https://en.wikipedia.org/wiki/Separation_of_concerns">separation of concerns</a> principle. This in turn empowers the contributors with more autonomy while also enforcing architectural patterns.</td>
  </tr>
  <tr>
    <td>Enabling work in parallel</td>
    <td>Modularization helps decrease version control conflicts and enables more efficient work in parallel for developers in larger teams.</td>
  </tr>
  <tr>
    <td>Ownership</td>
    <td>A module can have a dedicated owner who is responsible for maintaining the code and tests, fixing bugs, and reviewing changes.</td>
  </tr>
  <tr>
    <td>Encapsulation</td>
    <td>Isolated code is easier to read, understand, test, and maintain.</td>
  </tr>
  <tr>
    <td>Reduced build time</td>
    <td>Leveraging Gradle’s parallel and incremental build can reduce build times.</td>
  </tr>
  <tr>
    <td>Dynamic delivery</td>
    <td>Modularization is a requirement for <a href="https://developer.android.com/guide/playcore/feature-delivery">Play Feature Delivery</a> which allows certain features of your app to be delivered conditionally or downloaded on demand.</td>
  </tr>
  <tr>
    <td>Reusability</td>
    <td>Proper modularization enables opportunities for code sharing and building multiple apps, across different platforms, from the same foundation.</td>
  </tr>
</table>

## Modularization pitfalls

Modularization is a valuable architectural pattern but can be prone to misuse, with potential
pitfalls to consider when modularizing an app. One common issue we face is:

**Too Many Modules** - Each module introduces additional overhead in terms of build configuration
complexity. This can lead to increased Gradle sync times and ongoing maintenance costs. Furthermore,
adding more modules may complicate the project’s Gradle setup compared to a single monolithic
module.
> To address this, we create [convention plugins](/build-logic/convention/build.gradle.kts) to
> extract reusable build configurations into type-safe Kotlin code. In the BAZZ Movies app, these
> convention plugins are located in the [`build-logic` folder](/build-logic/).

## Types of modules in BAZZ Movies

Basically BAZZ Movies using [feature modules](https://developer.android.com/topic/modularization/patterns#feature-modules),
which each module has their layer(ui, domain, and data), but also for shared logic we created shared
module for each of them. Each feature module has pattern like below:

![Diagram Feature Module](/docs/architecture.png)

1. **Data Layer**
   - Depends on the Domain layer.Responsible for communicating with the Database or Remote.
      - **Data Source**: Write about Local(Room..), Remote(Retrofit..) etc.
      - **Repository(implements)**: Write an implementation of the repository interface of the Domain Layer. Write a code to communicate with Remote.
      - **Data Model**: Create a DTO to be used in the Repository
      - **Mapper**: A class that converts the Data Model to an Domain Model.

2. **Domain Layer**
   - Consists purely of Kotlin code and has no dependencies on other layers or Android. Usecase and Repository interface, Domain Model exist.
      - **Usecase**: The smallest unit of action
      - **Repository(interface)**: Interface that defines the behavior of the repository.
      - **Domain Model**: Definition of an object to be used in the project.

3. **UI Layer**
   - Handles processing for UI and has dependency on Domain Layer, View, ViewModel, Presenter, etc.
   - Usecases of Domain Layer are injected into ViewModel through dependency injection ([Hilt](https://dagger.dev/hilt/)) to create business logic.

and for overall modularization can see below:
![Diagram showing types of modules and their dependencies in BAZZ Movies app](/docs/modularization.png "Diagram showing types of modules and their dependencies in BAZZ Movies")

<hr>

> *Note: This explanatory content is inspired by [Now in Android](https://github.com/android/nowinandroid/blob/main/docs/ModularizationLearningJourney.md)
by Google. For further insights and detailed examples of modularization, you can explore the
repository directly.*
