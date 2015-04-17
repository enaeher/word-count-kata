(ns healthfinch.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.codec :refer [url-encode]]
            [hiccup.core :as hiccup]
            [hiccup.page :as page]
            [healthfinch.cache :as core]
            [healthfinch.html :as html]
            [healthfinch.text :as text]))

(defn page-wrapper [& hiccup-fragments]
  (page/html5
   `[:html
     [:head
      [:link {:rel "stylesheet" :href "main.css"}]]
     [:body
      ~@hiccup-fragments]]))

(defroutes app-routes
  (GET "/" []
       (page-wrapper
        [:form {:action "/parse-url" :method :get}
         [:label {:for "url"} "URL: "]
         [:input {:type "text" :name "url"}]
         [:input {:type "submit"}]]
        [:table
         [:tr
          [:th "URL"]
          [:th "Most frequent word"]
          [:th "Occurrences"]]
         (if (seq @core/stored-urls)
           (for [[url counts] @core/stored-urls]
             (let [[word word-count] (first counts)]
               [:tr
                [:td [:a {:href (str "/parse-url?url=" (url-encode url))} url]]
                [:td word]
                [:td word-count]]))
           [:tr [:td {:colspan 3} "(No URLs stored.)"]])]))  
  (GET "/parse-url" [url]
       (page-wrapper
        [:table
         (for [[word count] (core/word-counts-for-url url)]
           [:tr
            [:td word]
            [:td count]])]))
  (route/resources "/")
  (route/not-found "Not Found"))

(defn nicer-malformed-url-exceptions [f]
  (fn [request]
    (try (f request)
         (catch java.net.MalformedURLException e
           {:body (page-wrapper "Invalid URL.")}))))

(def app
  (-> app-routes
      (wrap-defaults site-defaults)
      (nicer-malformed-url-exceptions)))
