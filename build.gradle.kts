plugins {
   id("us.ihmc.ihmc-build")
}

ihmc {
   group = "us.ihmc"
   version = "b4829"
   vcsUrl = "https://github.com/ihmcrobotics/llama.cpp-javacpp"
   openSource = true

   configureDependencyResolution()
   configurePublications()
}

mainDependencies {
   api("org.bytedeco:javacpp:1.5.11")
   api("us.ihmc:ihmc-native-library-loader:2.0.6")
}
