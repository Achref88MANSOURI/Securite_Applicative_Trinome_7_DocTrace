1. downloaded maven

2. Run:

	a. `C:\Users\amin\Documents\Tools\apache-maven-3.10.2\bin\mvn.cmd dependency:go-offline`

4. Packaging - Run:
   a. `$env:JAVA_HOME = 'C:\Program Files\Eclipse Adoptium\jdk-25.0.5.10002-hotspot'`

   b. `& 'C:\Users\amin\Documents\Tools\wildfly-42.0.0.Final\bin\jboss-cli.bat' --connect --command="deploy C:\Users\amin\Documents\Projects\Securite_Applicative_Trinome_7_DocTrace\doctrace-api\target\doctrace-api-0.1.0-SNAPSHOT.war"`
5. Run:

   a.`$env:JAVA_HOME = 'C:\Program Files\Eclipse Adoptium\jdk-25.0.5.10002-hotspot'`

   b.`& 'C:\Users\amin\Documents\Tools\wildfly-42.0.0.Final\bin\standalone.bat' -b 127.0.0.1`
6. Check `127.0.0.1:8080/api/health`

OR

### 8.2 Startup and API Deployment

```powershell
# Terminal 1: start the server (keep this terminal open)
& "C:\Tools\wildfly-42.0.0.Final\bin\standalone.bat" -b 127.0.0.1

# Terminal 2: from the repository root, with the WAR previously built
& "C:\Tools\wildfly-42.0.0.Final\bin\jboss-cli.bat" --connect `
  --command="deploy doctrace-api\target\doctrace-api-0.1.0-SNAPSHOT.war"
```

WildFly reported a successful startup:


### 8.3 Undertow Static Handler

The default server initially used `welcome-content`, pointing to `${jboss.home.dir}/welcome-content`. This handler **was not modified directly**; a new handler was created:

```powershell
& "C:\Tools\wildfly-42.0.0.Final\bin\jboss-cli.bat" --connect `
  --command="/subsystem=undertow/configuration=handler/file=doctrace-frontend:add(path=C:/Tools/doctrace-deploy/frontend,directory-listing=false,follow-symlink=false)"
```

Reading the configuration confirmed:

```text
"path" => "C:/Tools/doctrace-deploy/frontend"
"directory-listing" => false
"follow-symlink" => false
```

The `/` path of the default Undertow host was then mapped to the new handler:

```powershell
& "C:\Tools\wildfly-42.0.0.Final\bin\jboss-cli.bat" --connect `
  --command="/subsystem=undertow/server=default-server/host=default-host/location=\/:write-attribute(name=handler,value=doctrace-frontend)"
```

WildFly requested a reload (`reload-required`). This was executed:

```powershell
& "C:\Tools\wildfly-42.0.0.Final\bin\jboss-cli.bat" --connect --command=":reload"
```

Both URLs were verified again after this reload.


can't verify/build:

RUN:

```xml

$env:JAVA_HOME = 'C:\Program Files\Eclipse Adoptium\jdk-25.0.5.10002-hotspot'
$env:Path = "$env:JAVA_HOME\bin;$env:Path" 

```
