Introduction to Maven
=====================
**What is Maven?**
Maven is a build automation tool used primarily for Java projects. Maven is developed by the Apache Software Foundation and is free to use. Both Eclipse and IDEA support Maven projects.

**Why do we need Maven?**
When we did robot or tortoise programs in the early levels we had to add the jar to our project. To do this we distributed the jar through Google Docs so it was available on all the machines. But, if we wanted to do a robot project at home, we would have to get the robot jar from the Internet.

With Google App Engine projects, and most real world projects, we will use many, many dependencies. These dependencies can be other projects, software development kits like Google App Engine or third party jars. Currently once we get IntelliJ IDEA or Eclipse set up it will probably work for our project, but we have to manually download the jars from the internet and add them to our project. Furthermore if I want to swith from Eclipse to IDEA or from IDEA to Eclipse I would have to change my project. I may even end up saving copies of my jars into my GitHub project. If I have many projects all using the same jars this can eat up storage space quickly.

Because of these problems we developed build automation tools like Maven or Gradle, which is used for Android projects. Older build automation tools included ANT and Make, though those were not initially developed to consolidate dependency management.

**What does Maven do?**
Maven knows how to compile Java and build jars. It also knows how to find jars and even how to find the jars that your jars may require. People have also written plugins that allow it to do much, much more including deploying projects to Google App Engine servers.

Converting our Hello World project to Maven
-------------------------------------------

1. Let's convert our existing Hello World project to a Maven project.
    1. Open your Hello World project in IDEA
    2. Right click (or command click) on your project root (HelloWorld) and select "Add framework support..."
    3. Choose maven from the list of frameworks. IDEA will create a simple pom.xml file (the tab will have the name of your project.)
        1. Specify org.jointheleague.(your-github-username) as the groupId
        2. Accept HelloWorld (or your project name) as the artifactId
        3. Version 1.0-SNAPSHOT is acceptable
    4. If you are prompted import the changes. (You can click enable autoimport, but my preference is to manually import changes.)

2. Set up our project for app-engine
    1. Tell maven to package our project for the web by adding the following line.
    
        ```xml
        <packaging>war</packaging>
        ```
    2. Specify the version of app-engine by adding the following lines:
    
        ```xml
        <properties>  
            <appengine.app.version>1</appengine.app.version>  
            <appengine.sdk.version>1.9.24</appengine.sdk.version>  
        </properties>
        ```
    3. App engine requires maven 3.1 or higher so add the following block:  
    
        ```xml
        <prerequisites>
            <maven>3.1.0</maven>
        </prerequisites>
        ```

3. Tell maven what dependencies are required (all dependencies are inside a `<dependency>` block: 
 
    ```xml
    <dependencies>
        <dependency>
            <groupId>com.google.appengine</groupId>
            <artifactId>appengine-api-1.0-sdk</artifactId>
            <version>${appengine.sdk.version}</version>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>servlet-api</artifactId>
            <version>2.5</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>jstl</groupId>
            <artifactId>jstl</artifactId>
            <version>1.2</version>
        </dependency>
    </dependencies>
    ```
    A note: It's pretty easy to find the dependencies you need by googling for them. IDEA will also search for them for you and automatically add them if you ask nicely.
    
4. Finally we need to tell maven how to build our project. We add a build section as follows, this is a big block but it simply
configures the plugins we need to build an appengine project.
  
    ```xml
      <build>
        <!-- for hot reload of the web application-->
        <outputDirectory>${project.build.directory}/${project.build.finalName}/WEB-INF/classes</outputDirectory>
        <plugins>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>versions-maven-plugin</artifactId>
                <version>2.1</version>
                <executions>
                    <execution>
                        <phase>compile</phase>
                        <goals>
                            <goal>display-dependency-updates</goal>
                            <goal>display-plugin-updates</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <version>3.2</version>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>1.7</source>
                    <target>1.7</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>2.6</version>
                <configuration>
                    <archiveClasses>true</archiveClasses>
                    <webResources>
                        <!-- in order to interpolate version from pom into appengine-web.xml -->
                        <resource>
                            <directory>${basedir}/web</directory>
                            <filtering>true</filtering>
                            <targetPath>/</targetPath>
                        </resource>
                    </webResources>
                </configuration>
            </plugin>
            <plugin>
                <groupId>com.google.appengine</groupId>
                <artifactId>appengine-maven-plugin</artifactId>
                <version>${appengine.sdk.version}</version>
                <configuration>
                    <enableJarClasses>false</enableJarClasses>
                    <!-- Comment in the below snippet to bind to all IPs instead of just localhost -->
                    <!-- address>0.0.0.0</address>
                    <port>8080</port -->
                    <!-- Comment in the below snippet to enable local debugging with a remote debugger
                         like those included with Eclipse or IntelliJ -->
                    <!-- jvmFlags>
                      <jvmFlag>-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n</jvmFlag>
                    </jvmFlags -->
                </configuration>
            </plugin>
        </plugins>
    </build>
    ```
    
5. If you have errors in your pom, use the context sensitive help to resolve them. Then try rebuilding the project.
6. Let's see if this worked, cross your fingers and open Eclipse.
    1. In Eclipse select Import > Maven > Existing Maven Projects
    2. Browse to your project, select open. Eclipse should see your pom.xml, click Finish.
    3. Click Run > Run as... > Maven Test 
    4. Wait for the build success message.
7. We can't launch from Eclipse unless we install the Google App Engine plugin. But we can build in Eclipse and then launch in IDEA to prove everything worked.

**What else should I know about Maven?**
Beyond managing your project's build, Maven manages dependencies on your computer and network. If you look in your home directory (/Users/league) you will find a .m2 directory. Under that is a repository directory where Maven saves all of your downloaded artifacts. This allows all of your projects to share a single jar file. In addition we have a Maven Repository Manager in the classroom. All of the classroom computers have been configured to look at this machine for maven artifacts. Because the Repository Manager is a proxy, if it can't find an artifact it will download it. This prevents multiple simultaneous downloads of the same file. 

In the future our repository manager will also serve as the destination for artifacts we create. We can specifically point to other repository managers in our pom files. This would allow us to create pom files for programs that used our Robot.jar that would allow anyone to retrieve the Robot.jar from our repository manager.

If you would like to browse the repository manager point your web browser to [http://nexus.jointheleague.org:8081](http://nexus.jointheleague.org:8081)
