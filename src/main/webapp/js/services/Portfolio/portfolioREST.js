RoboAdviceApp.service("portfolioREST",
    function($resource,$cookies,TokenHandler){
        var baseUrl = "/portfolio/";
        return $resource(baseUrl, {}, {
            getCurrent: {
                method: 'POST',
                url: baseUrl + 'getCurrent',
                params: {
                    user_id: '@user_id'
                },
                headers : {'Authorization' : TokenHandler.get() }
            },//end getCurrent

            getFullHistory: {
                method: 'POST',
                url: baseUrl + 'getFullHistory',
                params: {
                    user_id: '@user_id'
                },
                headers : {'Authorization' : TokenHandler.get() }
            },

            getPortfolioByDate: {
                method: 'POST',
                url: baseUrl + 'getPortfoliobyDate',
                params: {
                    user_id: '@user_id'
                },
                headers : {'Authorization' : TokenHandler.get() }
            }
        })
    });
