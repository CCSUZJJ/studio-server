name := "studio-portal"

version := "1.0.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache
)     

play.Project.playJavaSettings

resolvers ++= Seq(
  "Nexus osc" at "http://maven.oschina.net/content/groups/public/",
  "Nexus Repository" at "http://192.168.180.78:8082/nexus/content/groups/public/"
)

//resolvers += Resolver.mavenLocal
//
//libraryDependencies ++= Seq(
//  "log4j" % "log4j" % "1.2.17",
//  "com.alibaba" % "fastjson" % "1.2.8",
//  "org.bouncycastle" % "bcprov-jdk16" % "1.45",
//  "com.mob" % "studio-dao" % "1.0.0-SNAPSHOT",
//  "com.mob" % "studio-service" % "1.0.0-SNAPSHOT"
//)
