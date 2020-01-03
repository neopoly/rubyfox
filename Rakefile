sfe_root = ENV['SFE_ROOT']

unless sfe_root
  abort "SFE_ROOT not set"
else
  install_lib_dir = File.join(sfe_root, "extensions", "__lib__")
  lib_dir = File.join(sfe_root, "lib")

  unless File.directory?(install_lib_dir)
    FileUtils.mkdir_p(install_lib_dir)
  end
end

desc "Compile, build and install jar"
task :default => "rubyfox"

desc "Compile, build and install jar"
task :rubyfox => "rubyfox:install"

namespace :rubyfox do
  desc "Compile, build and install jar"
  task :install => [ :install_jruby, :install_jar ]

  desc "Compile rubyfox"
  task :compile do
    if Gem.win_platform?
      system "javac -d out -cp src/java;lib/*;#{sfe_root}/lib/* src/java/com/neopoly/rubyfox/*.java"
    else
      system "javac -d out -cp 'src/java:lib/*:#{sfe_root}/lib/*' src/java/com/neopoly/rubyfox/*.java"
    end
  end

  desc "Build rubyfox.jar"
  task :jar => :compile do
    system 'jar cvMf target/rubyfox.jar -C out .'
  end

  desc "Install jars to #{install_lib_dir}"
  task :install_jruby do
    cp FileList["lib/*.jar"], install_lib_dir
  end

  desc "Install rubyfox.jar"
  task :install_jar => :jar do
    cp FileList["target/rubyfox.jar"], sfe_root
  end
end
