(defproject healthfinch "0.1.0-SNAPSHOT"
  :description "Healthfinch code test"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/test.check "0.7.0"]
                 [net.htmlparser.jericho/jericho-html "3.3"]
                 [compojure "1.3.3"]
                 [ring/ring-defaults "0.1.2"]
                 [hiccup "1.0.5"]
                 [ring/ring-codec "1.0.0"]
                 [hiccup-bootstrap "0.1.2"]]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler healthfinch.handler/app}
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring-mock "0.1.5"]]}})
