RoboAdviceApp.factory('userREST', ['$resource', '$cookies', 'TokenHandler',
    function($resource, $cookies, TokenHandler) {
        var baseUrl = "/user/"
        var token = $cookies.get("token");

        return $resource(baseUrl, {}, {
            login: {
              method: 'POST',
              url: baseUrl + 'login',
              params: {
                email: '@email',
                password: '@password'
              }
            },
            signup: {
              method: 'POST',
              url: baseUrl + 'signup',
              params: {
                name: '@name',
                surname: '@surname',
                email: '@email',
                password: '@password'
              }
            },
            update: {
              method: 'POST',
              url: baseUrl + 'update',
              params: {
                user_id: '@user_id',
                name: '@name',
                surname: '@surname'
              }
            }
        });
    }
]);
