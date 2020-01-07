sfe_root = ENV['SFE_ROOT']
extension_name = ENV['SFE_NAME']

unless sfe_root
  abort "SFE_ROOT not set"
else
  extensions_dir = File.join(sfe_root, "extensions")
  extension_dir = File.join(extensions_dir, extension_name)
  install_lib_dir = File.join(extensions_dir, "__lib__")

  unless File.directory?(install_lib_dir)
    mkdir_p(install_lib_dir)
  end
end

desc "Compile, build and install jar"
task :default => "rubyfox:install"

namespace :rubyfox do
  desc "Compile, build and install jar"
  task :install => [:jar, :install_jruby, :install_jar, :copy_example]

  desc "Compile rubyfox"
  task :compile do
    puts "Compiling..."
    if Gem.win_platform?
      system "javac -source 1.8 -target 1.8 -d out -cp src/java;lib/*;#{sfe_root}/lib/* src/java/com/neopoly/rubyfox/*.java"
    else
      system "javac -source 1.8 -target 1.8 -d out -cp 'src/java:lib/*:#{sfe_root}/lib/*' src/java/com/neopoly/rubyfox/*.java"
    end
  end

  desc "Build rubyfox.jar"
  task :jar => :compile do
    puts "Bundling..."
    system 'jar cvMf target/rubyfox.jar -C out .'
  end

  desc "Install jars to #{install_lib_dir}"
  task :install_jruby do
    puts "Installing JRuby..."
    cp FileList["lib/*.jar"], install_lib_dir
  end

  desc "Install rubyfox.jar"
  task :install_jar do
    puts "Installing jar..."
    unless File.directory?(extension_dir)
      mkdir_p(extension_dir)
    end
    cp FileList["target/rubyfox.jar"], extension_dir
  end

  desc "Copies example extension only when"
  task :copy_example do
    unless File.file? File.join(extension_dir,"config.properties")
      ruby_lib = File.join(extension_dir, "lib")
      unless File.directory?(ruby_lib)
        mkdir_p(ruby_lib)
      end
      puts "Copy example files..."
      cp FileList["example/config.properties"], extension_dir
      cp FileList["example/rubyfox.rb"], ruby_lib
    end
  end
end
