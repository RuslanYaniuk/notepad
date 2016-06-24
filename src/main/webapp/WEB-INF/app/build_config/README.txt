How to prepare this project for development:

1. install npm

2. install bower

3. run in console (from the current directory):
  -> npm install
  -> grunt prepare
  
Preparation before development:

1. npm install
2. bower-installer
  
  
How to make a production .war file:

1. npm install -g bower-installer
2. cd src/main/webapp/WEB-INF/app/build_config/
3. bower-installer
4. grunt prod
5. cd <project root dir>
5. mvn package:war

If you don't update bower.json file or you have already installed 'bower-installer' skip steps 1 and 3