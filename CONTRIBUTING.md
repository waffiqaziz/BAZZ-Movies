# Contributing Guidelines

*Pull requests, bug reports, and all other forms of contribution are welcomed and highly
encouraged!* :octocat:

## 📚 Contents

- [Code of Conduct](#-code-of-conduct)
- [Asking Questions](#-asking-questions)
- [Opening an Issue](#-opening-an-issue)
- [Feature Requests](#-feature-requests)
- [Triaging Issues](#-triaging-issues)
- [Submitting Pull Requests](#-submitting-pull-requests)
- [Writing Commit Messages](#-writing-commit-messages)
- [Code Review](#white_check_mark-code-review)
- [Coding Style](#nail_care-coding-style)
- [Certificate of Origin](#medal_sports-certificate-of-origin)

> **This guide serves to set clear expectations for everyone involved with the project so that we
can improve it together while also creating a welcoming space for everyone to participate. Following
these guidelines will help ensure a positive experience for contributors and maintainers.**

## 📖 Code of Conduct

Please review our [Code of Conduct](CODE_OF_CONDUCT.md). It is in effect at all times. We expect it
to be honored by everyone who contributes to this project. Acting like an asshole will not be
tolerated.

## 💡 Asking Questions

See our [Support Guide](SUPPORT.md). In short, GitHub issues are not the appropriate place to debug
your specific project, but should be reserved for filing bugs and feature requests.

## 📥 Opening an Issue

Before [creating an issue](https://help.github.com/en/github/managing-your-work-on-github/creating-an-issue),
check if you are using the latest version of the project. If you are not up-to-date, see if updating
fixes your issue first.

### 🔒 Reporting Security Issues

Review our [Security Policy](SECURITY.md). **Do
not** file a public issue for security vulnerabilities.

### 🐞 Bug Reports and Other Issues

One of the best ways to help improve the project is by reporting any issues you encounter. We love
receiving clear, and detailed bug reports! :v:

Since you’re likely a developer, think of submitting an issue as creating a ticket you’d like to
receive yourself. Here are some tips to make your report as helpful as possible:

- Check the documentation and the [Support Guide](SUPPORT.md) first to see if your issue is already
  covered.

- Please avoid opening duplicate issues. Take a moment to search through existing issues to see if
  someone else has already reported the same problem. If you find an existing issue, feel free to
  add any extra details you have.

- Prefer use
  [reactions](https://github.blog/2016-03-10-add-reactions-to-pull-requests-issues-and-comments/)
  (👍 or 👎) to show your support for an issue, rather than adding a comment like “+1.” or “I have
  this problem too” This helps keep the discussion clear and lets us focus on the most important
  problems.

- Complete the issue template fully. The template asks for the key information we need to address
  the problem efficiently. Be clear and descriptive. The more details you provide—such as steps to
  reproduce, error messages, stack traces, library versions, OS versions, and screenshots—the easier
  it will be for us to help.

- When you include code or console outputs,
  use [GitHub-flavored Markdown](https://help.github.com/en/github/writing-on-github/basic-writing-and-formatting-syntax).
  and remember to wrap code and logs in backticks (```) for better readability.

## 💌 Feature Requests

We love hearing your ideas and feature requests! While we can’t guarantee every request will be
accepted, we definitely value your input. Our goal is to keep the project focused and
avoid [feature creep](https://en.wikipedia.org/wiki/Feature_creep), so some requests might be
outside the scope of what we’re able to implement.

If your feature request is accepted, please note that we can’t commit to a specific timeline for
implementation, but you’re always welcome to help out by submitting a pull request!

Here’s how you can make your feature request as helpful as possible:

- Avoid opening duplicate feature requests. Before submitting, take a moment to search for similar
  requests. If you find one that’s close to yours, feel free to comment with your thoughts or any
  additional details!

- Complete the feature request template to help us get all the info we need to understand your idea.
  The template is designed to make sure we’re on the same page from the start.

- Be clear about the desired outcome of the feature and how it fits in with what’s already in the
  project. If you have ideas on how it could be implemented, please share them!

## 🔍 Triaging Issues

You can triage issues which may include reproducing bug reports or asking for additional
information, such as version numbers or reproduction instructions. Any help you can provide to
quickly resolve an issue is very much appreciated!

## 🔄 Submitting Pull Requests

We **love** pull requests!
Before [forking the repo](https://help.github.com/en/github/getting-started-with-github/fork-a-repo)
and [creating a pull request](https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/proposing-changes-to-your-work-with-pull-requests)
for non-trivial changes, it is usually best to first open an issue to discuss the changes, or
discuss your intended approach for solving the problem in the comments for an existing issue.

📚 *Note: All contributions will be licensed under the project's license.*

- **Smaller is better**. Try to submit one pull request per bug fix or feature. Keeping each pull
  request focused on a single change makes it easier for us to review. Instead of large pull
  requests, it’s better to submit several smaller ones. This keeps the process manageable for
  everyone!

- **Coordinate bigger changes**. For larger or more complex changes, it’s a good idea to open an
  issue
  and discuss your plan with the maintainers. This way, we can make sure your work is on the right
  track and avoid unnecessary rework.

- **Prioritize clarity over cleverness**. Code is read far more often than it is written. Focus on
  making your code clear and easy to understand. If something’s not immediately obvious, don’t
  hesitate to add a comment to explain it. Clear code makes everyone’s life easier!

- **Follow the existing coding style and conventions**. Keeping your code consistent with the rest
  of
  the
  project helps everyone work together smoothly. We use a linter to help enforce this, but feel free
  to ask if you’re unsure about something!

- **Include test coverage**. If you’re adding or changing functionality, it’s always helpful to add
  unit
  or UI tests to go along with it. If there’s an existing pattern for tests in the project, please
  follow it to keep things consistent.

- **Add documentation**. Be sure to update any relevant documentation, whether it’s inline code
  comments
  or external guides. This makes it easier for others to use and understand your work.

- **Use the repo's default branch**. When you’re ready to create your pull request, make sure you
  branch from and
  [submit your pull request](https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request-from-a-fork)
  to the repo’s default branch (main).
  [Resolve any merge conflicts](https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/resolving-a-merge-conflict-on-github)
  that occur.

- **Promptly address any CI failures**. If your pull request fails to build or pass tests, please
  push another commit to fix it. If you believe the failure is a false positive (e.g., caused by code analysis tools), please provide write a comment and tag the maintainer.

- **Writing a comment**, use properly constructed sentences, including punctuation.

## 📝 Writing Commit Messages

Please
read [commit guidelines](https://ec.europa.eu/component-library/v1.15.0/eu/docs/conventions/git/) and
[commit guidelines addition](https://karma-runner.github.io/6.3/dev/git-commit-msg.html).

## :white_check_mark: Code Review

- Focus on the code, not the coder. Offer helpful suggestions to improve the code, and remember to
  stay positive and respectful. We’re all here to grow and learn together!

- Separate yourself from your code. Feedback on your code isn’t a reflection of you—it's a chance to
  make the code even better. Embrace constructive input with an open mind.

- Mistakes happen—it's okay! Nobody's perfect, and bugs are part of the process. Let’s aim to learn
  from them and improve for next time.

- If you notice something that could use some attention, kindly and respectfully bring it up.
  Constructive discussions help everyone!

## :nail_care: Coding Style

Consistency makes code easier to read and maintain. Follow the existing style, formatting, and
naming conventions of the file or project you’re working on. This helps keep reviews focused on
functionality and performance, rather than formatting tweaks.

For example: If methods use camelCase (e.g., `thisIsMyNewMethod`), avoid switching to
`this_is_my_new_method`.

For testing name convention, please
[follow this naming guide](./docs/BAZZMoviesTesting.md/#test-naming-convention).

> [!NOTE]
> **A Note on Package Naming**
>
> The current package naming uses snake_case, which does not align with Java and Kotlin conventions.
> However,
> because the app is already live on the Play Store, this naming style will continue to be used.
> Please follow this convention when working with packages in the project.
>
> When in doubt, feel free to ask or look for similar patterns in the codebase.
>
> To help ensure consistency, you can run `./gradlew detekt` to check the code style before
> submitting
> your changes. It’s a handy tool to catch most issues early!

## :medal_sports: Certificate of Origin

Developer's Certificate of Origin 1.1

By making a contribution to this project, I certify that:

> 1. The contribution was created in whole or in part by me and I have the right to submit it under
     the open source license indicated in the file; or
> 2. The contribution is based upon previous work that, to the best of my knowledge, is covered
     under an appropriate open source license and I have the right under that license to submit that
     work with modifications, whether created in whole or in part by me, under the same open source
     license (unless I am permitted to submit under a different license), as indicated in the file;
     or
> 3. The contribution was provided directly to me by some other person who certified (1), (2) or (3)
     and I have not modified it.
> 4. I understand and agree that this project and the contribution are public and that a record of
     the contribution (including all personal information I submit with it, including my sign-off)
     is maintained indefinitely and may be redistributed consistent with this project or the open
     source license(s) involved.
