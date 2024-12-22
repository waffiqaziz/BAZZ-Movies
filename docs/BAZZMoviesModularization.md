## Overview

Modularization is the practice of breaking the concept of a monolithic, one-module codebase into
loosely coupled, self contained modules.

### Benefits of modularization

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

### Modularization pitfalls

Modularization is a valuable architectural pattern but can be prone to misuse, with potential
pitfalls to consider when modularizing an app. One common issue we face is:

**Too Many Modules** - Each module introduces additional overhead in terms of build configuration
complexity. This can lead to increased Gradle sync times and ongoing maintenance costs. Furthermore,
adding more modules may complicate the project’s Gradle setup compared to a single monolithic
module.
> To address this, we create [convention plugins](/build-logic/convention/build.gradle.kts) to
> extract reusable build configurations into type-safe Kotlin code. In the Now in Android app, these
> convention plugins are located in the [`build-logic` folder](/build-logic/).

## Types of modules in BAZZ Movies

![Diagram showing types of modules and their dependencies in Now in Android](/docs/Modularization.drawio.svg "Diagram showing types of modules and their dependencies in BAZZ Movies")

<hr>

> *Note: This explanatory content is inspired by
the [Now in Android](https://github.com/android/nowinandroid/blob/main/docs/ModularizationLearningJourney.md)
by Google. For further insights and detailed examples of modularization, you can explore the
repository directly.*

