# Another LLM Chat Bot for your Paper/Spigot Minecraft Server

Another Chat Bot to make some fun for your players.

## Prerequisites

In order to try this out you need the following software to be installed on your machine:

* Java version 21 or above (e.g. [Oracle Java](https://www.oracle.com/java/technologies/downloads/#java21))
* [git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)


## How to build and install the plugin

Clone the template project to your system:
````bash
git clone https://github.com/DmitryRendov/mob-chatbot.git
````

This project uses [Maven](https://maven.apache.org/) for building. So on your command line run

````bash
mvn clean package
````

To install the plugin in your Minecraft server just copy the artifact file `target/mob-chatbot-0.1.0.jar`
to the server's `plugin` folder.
