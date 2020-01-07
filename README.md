[github]: https://github.com/neopoly/rubyfox
[doc]: http://rubydoc.info/github/neopoly/rubyfox/master/file/README.md
[gem]: https://rubygems.org/gems/rubyfox
[travis]: https://travis-ci.org/neopoly/rubyfox
[codeclimate]: https://codeclimate.com/github/neopoly/rubyfox
[inchpages]: https://inch-ci.org/github/neopoly/rubyfox

# Rubyfox

[Source][github] |
[Documentation][doc]

SmartFox server extension for loading a ruby class with JRuby

## Usage example

### Install smartfox

#### via gem
    $ gem install smartfox
    $ smartfox install /var/smartfox/test/
    
### Build and deploy rubyfox
    $ export SFE_ROOT=/var/smartfox/test/
    $ export SFE_NAME=example
    $ rake
    
### config a  zone to use the extension:
     
     <extension>
       <name>example</name>
       <type>JAVA</type>
       <file>com.neopoly.rubyfox.RubyExtension</file>
       <propertiesFile></propertiesFile>
       <reloadMode>NONE</reloadMode>
     </extension>
