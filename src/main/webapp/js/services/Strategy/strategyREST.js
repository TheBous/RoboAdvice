RoboAdviceApp.factory('strategyREST', ['$resource', '$cookies', 'TokenHandler',
    function($resource,$cookies, TokenHandler) {
        var baseUrl = "/strategy/"

        return $resource(baseUrl, {}, {
            getCurrent: {
                method: 'POST',
                url: baseUrl + 'getCurrent',
                params: {
                    user_id: '@user_id'
                },
                headers : {'Authorization' : TokenHandler.get() }
            },
            getHistory: {
                method: 'POST',
                url: baseUrl + 'getFullHistory',
                params: {
                    user_id: '@user_id'
                },
                headers : {'Authorization' : TokenHandler.get() }
            },
            insert: {
                method: 'POST',
                url: baseUrl + 'insert',
                headers: {'Content-Type': 'application/json; charset=UTF-8', 'Authorization' : TokenHandler.get() }
            },
            getLastStrategy: {
              method: 'POST',
              url: baseUrl + 'getLast',
              params: {
                    user_id: '@user_id'
              },
              headers : {'Authorization' : TokenHandler.get() }
            }
        });
    }
]);
