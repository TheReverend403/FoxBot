Contribootin' Guidelines
========================

We use Maven 3 for our dependency management. To compile, change to the root directory and run

    mvn clean package

Binaries can be found in the target/ directory.

* Braces go on the next line. See [Allman style](http://en.wikipedia.org/wiki/Indent_style#Allman_style)
* Don't spread code onto multiple lines unnecessarily. We don't follow a column limit.
* Don't commit project files. We don't want to see your IDE's configuration files all over the place. We use Maven for a reason. If necessary, update the gitignore.
* Keep the amount of commits to a minimum. Learn how to squash with Git.
* Try to keep your changes limited to the scope of your pull request, ie, don't make changes that are unrelated to the intention of your pull request.
* Test, test, test. Does it do what you expected it to? Does it break existing code? Does it compile and run successfully on Java 7 and Java 8?
* Documentation. Does your change require any manual config updates? Does it change the usage of an existing function? Does it add any new functionality?
* Provide a small justification for your pull request. Convince me to accept it.
* Don't add yourself to the contributors section of the pom.xml. This will be handled by a project maintainer.