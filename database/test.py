#import jnius_config
#jnius_config.add_classpath(".;sqlite-jdbc-3.45.3.0.jar;slf4j-api-1.7.36.jar")
from jnius import autoclass
Test = autoclass("Test")
ArrayList = autoclass("java.util.ArrayList")
a = ArrayList()
a.add("hello")
a.add("world")
Test.say(a)
