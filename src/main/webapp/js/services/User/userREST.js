RoboAdviceApp.factory('userREST', ['$resource', '$cookies', 'TokenHandler',
    function($resource, $cookies, TokenHandler) {
        var baseUrl = "http://localhost:8080/user/"
        var token = $cookies.get("token");

        return $resource(baseUrl, {}, {
            login: {
              method: 'POST',
              url: baseUrl + 'login',
              headers: {'Content-Type': 'application/json; charset=UTF-8' }
            },
            signup: {
              method: 'POST',
              url: baseUrl + 'signup',
              headers: {'Content-Type': 'application/json; charset=UTF-8' }
            },
            update: {
              method: 'POST',
              url: baseUrl + 'update',
              headers: {'Content-Type': 'application/json; charset=UTF-8' }
            }
        });
    }
]);
