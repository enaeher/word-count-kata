(ns healthfinch.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [healthfinch.handler :refer :all]
            [healthfinch.cache :as cache]
            [ring.util.codec :refer [url-encode]]))

(deftest test-app
  (with-redefs [cache/word-counts-for-url* (constantly [["quick" 3]
                                                        ["brown" 2]
                                                        ["fox" 1]])]
    (let [url (url-encode "http://example.com")]
      (testing "lookup route"
        (let [response (app (mock/request :get (str "/parse-url?url=" url)))]
          (is (= (:status response) 200))
          (is (.contains (:body response) "quick"))
          (is (.contains (:body response) "brown"))
          (is (.contains (:body response) "fox"))))

      (testing "main route"
        (let [response (app (mock/request :get "/"))]
          (is (= (:status response) 200))
          (is (.contains (:body response) "http://example.com"))
          (is (.contains (:body response) "quick"))))

      (testing "not-found route"
        (let [response (app (mock/request :get "/invalid"))]
          (is (= (:status response) 404)))))))

(deftest url-validation
  (testing "URL validation"
    (let [response (app (mock/request :get (str "/parse-url?url=not-a-url")))]
      (is (= 200 (:status response)))
      (is (.contains (:body response) "Invalid URL")))))
