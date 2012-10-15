
sfe_root = ENV['SFE_ROOT']

unless sfe_root
  abort "SFE_ROOT not set"
end

task :default => :install

task :compile do
  system "javac -d out -cp 'src/java:lib/*:#{sfe_root}/../../lib/*' src/java/com/neopoly/rubyfox/Main.java"
end

task :jar => :compile do
  system 'jar cvMf target/rubyfox.jar -C out .'
end

task :install_jruby do
  cp FileList["lib/*.jar"], "#{sfe_root}/../__lib__"
end

task :install_jar => :jar do
  cp "target/rubyfox.jar", sfe_root
end

task :install => [ :install_jruby, :install_jar ]
