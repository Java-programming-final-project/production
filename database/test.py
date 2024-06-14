import jnius_config
jnius_config.add_classpath(".;sqlite-jdbc-3.45.3.0.jar;slf4j-api-1.7.36.jar")
from jnius import autoclass
DiaryManager = autoclass("DiaryManager")
DM = DiaryManager()
