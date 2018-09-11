#!/bin/bash

# localhost
java -classpath mybatis-generator-core-1.3.5.1.jar:CustomMyBatisGenerator.jar org.mybatis.generator.api.ShellRunner -configfile generatorConfig_@localhost.xml -overwrite -verbose

