RoboAdviceApp.factory('strategyREST', ['$resource', '$cookies', 'TokenHandler',
    function($resource,$cookies, TokenHandler) {
        var baseUrl = "/strategy/"

        return $resource(baseUrl, {}, {
            getCurrent: {
                method: 'POST',
                url: baseUrl + 'getCurrent',
                params: {
                    user_id: '@user_id'
                }
            },
            getHistory: {
                method: 'POST',
                url: baseUrl + 'getFullHistory',
                params: {
                    user_id: '@user_id'
                }
            },
            insert: {
                method: 'POST',
                url: baseUrl + 'insert',
                headers: {'Content-Type': 'application/json; charset=UTF-8' }
            },
            getLastStrategy: {
              method: 'POST',
              url: baseUrl + 'getLast',
              params: {
                    user_id: '@user_id'
              }
            }
        });
    }
]);
