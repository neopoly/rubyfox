
sfe_root = ENV['SFE_ROOT']

unless sfe_root
  abort "SFE_ROOT not set"
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
    system "javac -d out -cp 'src/java:lib/*:#{sfe_root}/../../lib/*' src/java/com/neopoly/rubyfox/*.java"
  end

  desc "Build rubyfox.jar"
  task :jar => :compile do
    system 'jar cvMf target/rubyfox.jar -C out .'
  end

  desc "Install jars to #{sfe_root}/../__lib__"
  task :install_jruby do
    cp FileList["lib/*.jar"], "#{sfe_root}/../__lib__"
  end

  desc "Install rubyfox.jar"
  task :install_jar => :jar do
    cp "target/rubyfox.jar", sfe_root
  end
end
