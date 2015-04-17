(ns healthfinch.cache-test
  (:require [clojure.test :refer :all]
            [healthfinch.cache :refer :all]
            [healthfinch.text :as text]
            [healthfinch.html :as html]))

(deftest cache-behavior
  (testing "Word counts are cached"
    (let [counts [["quick" 3] ["brown" 2] ["fox" 1]]
          cache-misses (atom 0)
          url "http://example.com"]
      (with-redefs [stored-urls (atom {})
                    html/parse-url (constantly nil) ; Don't make HTTP requests during tests
                    html/extract-text (constantly nil)
                    text/count-words (fn [& _]
                                       (swap! cache-misses inc)
                                       counts)]
        (is (= (word-counts-for-url url)
               (word-counts-for-url url))
            "Subsequent calls should return the same result")
        (is (= 1 @cache-misses)
            "Calls after the first should hit the cache")
        (is (@stored-urls url)
            "URL should be present in cache")))))
